package me.uni.hiker.ui.screen.map.view.trackDetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.LatLng
import me.uni.hiker.ui.component.Loading
import me.uni.hiker.ui.provider.UserContext
import me.uni.hiker.ui.screen.Screen
import me.uni.hiker.ui.screen.map.service.rememberGPSEnabled
import me.uni.hiker.ui.screen.map.service.rememberLocationPermissionAndRequest

@Composable
fun TrackDetailsScreen(
    trackId: Long?,
    remoteId: String?,
    mapNavController: NavHostController,
    trackDetailsViewModel: TrackDetailsViewModel = hiltViewModel(),
) {
    val isLocationEnabled = rememberLocationPermissionAndRequest()
    val isGpsEnabled = rememberGPSEnabled(isLocationEnabled)
    val userContext = UserContext

    LaunchedEffect(key1 = trackId, key2 = remoteId) {
        if (remoteId != null) {
            trackDetailsViewModel.getRemoteTrackDetails(remoteId)
        } else {
            trackDetailsViewModel.getTrackDetails(trackId!!, userContext.user?.remoteId)
        }.also {
            trackDetailsViewModel.track?.also {
                trackDetailsViewModel.focusOnPoint(LatLng(it.lat, it.lon))
            }
        }

    }

    TrackDetailsView(
        trackId = trackId ?: remoteId!!,
        cameraPositionState = trackDetailsViewModel.cameraPositionState,
        isGpsEnabled = isLocationEnabled && isGpsEnabled,
        points = trackDetailsViewModel.points,
    )

    if (trackDetailsViewModel.track == null) {
        Loading()
    }

    TrackDetailsUIView(
        goBack = {
            if (!mapNavController.popBackStack(Screen.AllTrackMap, inclusive = false)) mapNavController.navigate(Screen.AllTrackMap)
            //TODO: Navigate to the exact location where the track is
        }
    )
}