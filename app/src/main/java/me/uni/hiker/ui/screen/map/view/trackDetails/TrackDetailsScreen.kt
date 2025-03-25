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
    trackId: Long,
    isRemote: Boolean,
    mapNavController: NavHostController,
    trackDetailsViewModel: TrackDetailsViewModel = hiltViewModel(),
) {
    val isLocationEnabled = rememberLocationPermissionAndRequest()
    val isGpsEnabled = rememberGPSEnabled(isLocationEnabled)
    val userContext = UserContext

    LaunchedEffect(key1 = trackId, key2 = isRemote) {
        if (isRemote) {
            trackDetailsViewModel.getRemoteTrackDetails(trackId)
        } else {
            trackDetailsViewModel.getTrackDetails(trackId, userContext.user?.id)
        }.also {
            trackDetailsViewModel.track?.also {
                trackDetailsViewModel.focusOnPoint(LatLng(it.lat, it.lon))
            }
        }

    }

    TrackDetailsView(
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
        }
    )
    //TODO: TrackDetailsUIView
    //          A kezdő pont és a cél legyen megjelölve és start és egy cél zászlóval
    //          Csak akkor lehessen elindítani egy útvonalat, ha kellő közelségben vagyunk a start ponthoz
}