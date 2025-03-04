package me.uni.hiker.ui.screen.map.view.allTracks

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.DefaultMapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import me.uni.hiker.model.track.Track
import me.uni.hiker.ui.screen.map.service.isCurrentLocationEnabled
import java.time.LocalDate

private val middleOfHungary = LatLng(47.48856, 19.04892)

@Composable
fun BoxScope.AllTracksScreen() {
    // TODO: GoogleMap ViewModel
    val isCurrentLocationEnabled = isCurrentLocationEnabled()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(middleOfHungary, 8f)
    }
    val mapUiSettings = remember (isCurrentLocationEnabled) { DefaultMapUiSettings.copy(
        compassEnabled = true,
        myLocationButtonEnabled = isCurrentLocationEnabled,
        mapToolbarEnabled = false,
        zoomControlsEnabled = false,
    ) }

    AllTracksView(
        tracks = listOf(
            Track(name = "Bal-Alsó", lat = 45.76092, lon = 15.81845, updatedAt = LocalDate.now(), createdAt = LocalDate.now(), length = 0f, remoteId = null, id = 1),
            Track(name = "Jobb-Felsó", lat = 48.77124, lon = 23.30561, updatedAt = LocalDate.now(), createdAt = LocalDate.now(), length = 0f, remoteId = null, id = 2),
        ),
        cameraPositionState = cameraPositionState,
        mapUiSettings = mapUiSettings,
        isMyLocationEnabled = isCurrentLocationEnabled,
    )
}
