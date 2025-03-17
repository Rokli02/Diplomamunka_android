package me.uni.hiker.ui.screen.map.view.trackDetails

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.LatLng
import me.uni.hiker.ui.component.Loading
import me.uni.hiker.ui.screen.map.service.rememberGPSEnabled
import me.uni.hiker.ui.screen.map.service.rememberLocationPermissionAndRequest

@Composable
fun TrackDetailsScreen(
    trackId: Long,
    isRemote: Boolean,
    trackDetailsViewModel: TrackDetailsViewModel = hiltViewModel(),
) {
    val isLocationEnabled = rememberLocationPermissionAndRequest()
    val isGpsEnabled = rememberGPSEnabled(isLocationEnabled)

    LaunchedEffect(key1 = trackId, key2 = isRemote) {
        if (isRemote) {
            trackDetailsViewModel.getRemoteTrackDetails(trackId)
        } else {
            trackDetailsViewModel.getTrackDetails(trackId)
        }.also {
            Log.d("TrackDetailsViewModel", "")
            trackDetailsViewModel.track?.also {
                trackDetailsViewModel.focusOnPoint(LatLng(it.lat, it.lon))
            }
        }

    }

    if (trackDetailsViewModel.track == null) {
        Loading()

        return
    }

    TrackDetailsView(
        cameraPositionState = trackDetailsViewModel.cameraPositionState,
        isGpsEnabled = isLocationEnabled && isGpsEnabled,
        points = trackDetailsViewModel.points,
    )

    //TODO: TrackDetailsUIView
    //          A kezdő pont és a cél legyen megjelölve és start és egy cél zászlóval
    //          Csak akkor lehessen elindítani egy útvonalat, ha kellő közelségben vagyunk a start ponthoz
}