package me.uni.hiker.ui.screen.map.view.allTracks

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraMoveStartedReason
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import me.uni.hiker.model.track.Track
import me.uni.hiker.ui.component.Loading
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
) {
    val userContext = UserContext
    val context = LocalContext.current
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
                        allTrackViewModel.loadTracks(proj.visibleRegion.latLngBounds, userContext.user?.id)
                    }
                }
            } else {
                allTrackViewModel.cancelTrackLoad()
            }
        }
    }

    AllTracksView(
        tracks = allTrackViewModel.clusteredTracks,
        cameraPositionState = allTrackViewModel.cameraPositionState,
        focusTrack = focusTrack,
        isCurrentLocationEnabled = hasLocationPermission && isGPSEnabled,
        onMapLoaded = {
            loading = false

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

            allTrackViewModel.cameraPositionState.projection?.also { proj ->
                allTrackViewModel.loadTracks(proj.visibleRegion.latLngBounds, userContext.user?.id)
            }
        }
    )

    AllTrackUIView(
        isLoggedIn = userContext.isLoggedIn,
        focusedTrack = focusedTrack,
        unfocusTrack = unfocusTrack,
        goToDetails = { track ->
            var isRemote = false

            val id = if (track.id != null) { track.id } else {
                isRemote = true

                track.remoteId
            }

            if (id != null) {
                mapNavController.navigate(Screen.TrackDetailsMap(id, isRemote))
            }
        }
    )

    if (loading) {
        Loading()
    }
}
