package me.uni.hiker.ui.screen.map

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.DefaultMapUiSettings
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

private val hungarySoutwestPoint =LatLng(45.76092, 15.81845)
private val hungaryNortheasePoint =LatLng(48.77124, 23.30561)

@SuppressLint("UnrememberedMutableState")
@Composable
fun GoogleMapScreen() {
    val miskolc = LatLng(48.0469, 20.7415)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(miskolc, 10f)
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            mapType = MapType.TERRAIN,
            minZoomPreference = 8f,
            latLngBoundsForCameraTarget = LatLngBounds(hungarySoutwestPoint, hungaryNortheasePoint),
        ),
        uiSettings = DefaultMapUiSettings.copy(
            compassEnabled = true,
            myLocationButtonEnabled = true,
            mapToolbarEnabled = true,
        ),
    ) {
        Marker(
            state = MarkerState(position = miskolc),
        )
    }
}