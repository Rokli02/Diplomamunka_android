package me.uni.hiker.ui.screen.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.utsman.osmandcompose.MapProperties
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.rememberCameraState
import org.osmdroid.util.GeoPoint

@Composable
fun OSMScreen() {
    val cameraState = rememberCameraState {
        geoPoint = GeoPoint(48.0469, 20.7415)
        zoom = 12.0
    }

    // add node
    OpenStreetMap(
        modifier = Modifier.fillMaxSize(),
        cameraState = cameraState,
        properties = MapProperties(
            minZoomLevel = 8.0,
        )
    ) {
    }
}