package me.uni.hiker.ui.screen.map.view.allTracks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.maps.android.compose.CameraMoveStartedReason
import me.uni.hiker.model.track.Track
import me.uni.hiker.ui.screen.Screen
import me.uni.hiker.ui.screen.map.service.rememberGPSEnabled
import me.uni.hiker.ui.screen.map.service.rememberLocationPermissionAndRequest


@Composable
fun AllTracksScreen(
    mapNavController: NavHostController,
    allTrackViewModel: AllTrackViewModel = hiltViewModel(),
) {
    val hasLocationPermission = rememberLocationPermissionAndRequest()
    val isGPSEnabled = rememberGPSEnabled(hasLocationPermission)
    var focusedTrack by remember { mutableStateOf<Track?>(null) }

    val focusTrack = { track: Track -> focusedTrack = track }
    val unfocusTrack = { focusedTrack = null }

    LaunchedEffect(allTrackViewModel.cameraPositionState.isMoving) {
        allTrackViewModel.cameraPositionState.also { cps ->
            if (!cps.isMoving) {
                if (cps.cameraMoveStartedReason != CameraMoveStartedReason.UNKNOWN) {
                    cps.projection?.also { proj ->
                        allTrackViewModel.loadTracks(proj.visibleRegion.latLngBounds)
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
    )

    AllTrackUIView(
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
}
