package me.uni.hiker.ui.screen.map.view.allTracks

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraMoveStartedReason
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import me.uni.hiker.R
import me.uni.hiker.exception.AlreadyExistingDataException
import me.uni.hiker.exception.InvalidDataException
import me.uni.hiker.model.track.Track
import me.uni.hiker.ui.component.Loading
import me.uni.hiker.ui.provider.LocalSnackbarContext
import me.uni.hiker.ui.provider.UserContext
import me.uni.hiker.ui.screen.Screen
import me.uni.hiker.ui.screen.map.service.currentLocationListener
import me.uni.hiker.ui.screen.map.service.rememberGPSEnabled
import me.uni.hiker.ui.screen.map.service.rememberLocationPermissionAndRequest


@SuppressLint("MissingPermission")
@Composable
fun AllTracksScreen(
    mapNavController: NavHostController,
    allTrackViewModel: AllTrackViewModel = hiltViewModel(),
    initialLocation: LatLng?,
) {
    val userContext = UserContext
    val context = LocalContext.current
    val snackbarContext = LocalSnackbarContext
    val coroutineScope = rememberCoroutineScope()
    val hasLocationPermission = rememberLocationPermissionAndRequest()
    val isGPSEnabled = rememberGPSEnabled(hasLocationPermission)
    var focusedTrack by remember { mutableStateOf<Track?>(null) }
    var loading by remember { mutableStateOf(true) }

    val focusTrack = { track: Track -> focusedTrack = track }
    val unfocusTrack = { focusedTrack = null }

    LaunchedEffect(allTrackViewModel.cameraPositionState.isMoving) {
        allTrackViewModel.cameraPositionState.also { cps ->
            if (!cps.isMoving) {
                if (cps.cameraMoveStartedReason != CameraMoveStartedReason.UNKNOWN) {
                    cps.projection?.also { proj ->
                        allTrackViewModel.loadTracks(proj.visibleRegion.latLngBounds, userContext.user?.remoteId)
                    }
                }
            } else {
                allTrackViewModel.cancelTrackLoad()
            }
        }
    }

    val tracks by allTrackViewModel.clusteredTracksFlow.collectAsState()

    AllTracksView(
        tracks = tracks,
        cameraPositionState = allTrackViewModel.cameraPositionState,
        focusTrack = focusTrack,
        isCurrentLocationEnabled = isGPSEnabled,
        onMapLoaded = {
            loading = false

            if (initialLocation == null) {
                currentLocationListener(context) {
                    it?.run {
                        coroutineScope.launch {
                            try {
                                allTrackViewModel
                                    .cameraPositionState
                                    .animate(
                                        CameraUpdateFactory.newLatLng(LatLng(latitude, longitude)),
                                        1000,
                                    )
                            } catch (_: CancellationException) {}
                        }
                    }
                }
            } else {
                coroutineScope.launch {
                    try {
                        allTrackViewModel
                            .cameraPositionState
                            .animate(
                                CameraUpdateFactory.newLatLng(initialLocation),
                                1000,
                            )
                    } catch (_: CancellationException) {}
                }
            }

            allTrackViewModel.cameraPositionState.projection?.also { proj ->
                allTrackViewModel.loadTracks(proj.visibleRegion.latLngBounds, userContext.user?.remoteId)
            }
        }
    )

    AllTrackUIView(
        isLoggedIn = userContext.isLoggedIn,
        focusedTrack = focusedTrack,
        unfocusTrack = unfocusTrack,
        onShareTrack = {
            try {
                focusedTrack?.also {
                    val remoteId = allTrackViewModel.shareTrack(it)

                    focusedTrack = it.copy(remoteId = remoteId)
                }
            } catch (exc: InvalidDataException) {
                Log.e("ATS", exc.message ?: "Unknown Error")

                snackbarContext.showSnackbar(message = exc.message ?: "Unknown Error", alignment = Alignment.TopCenter)
            } catch (exc: Exception) {
                Log.e("ATS", exc.message ?: "Unknown Error")

                snackbarContext.showSnackbar(message = context.getString(R.string.unable_to_save), alignment = Alignment.TopCenter)
            }
        },
        onSaveTrack = {
            try {
                focusedTrack?.also {
                    val id = allTrackViewModel.saveTrack(it)

                    focusedTrack = it.copy(id = id)
                }
            } catch (exc: AlreadyExistingDataException) {
                Log.e("ATS", exc.message ?: "Unknown Error")

                snackbarContext.showSnackbar(message = exc.message ?: "Unknown Error", alignment = Alignment.TopCenter)
            } catch (exc: InvalidDataException) {
                Log.e("ATS", exc.message ?: "Unknown Error")

                snackbarContext.showSnackbar(message = exc.message ?: "Unknown Error", alignment = Alignment.TopCenter)
            }
        },
        goToDetails = { track ->
            if (track.id != null || track.remoteId != null) {
                mapNavController.navigate(Screen.TrackDetailsMap(track.id, track.remoteId))
            }
        }
    )

    if (loading) {
        Loading()
    }
}
