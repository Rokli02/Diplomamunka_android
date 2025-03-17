package me.uni.hiker.ui.screen.map.view.trackDetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.DefaultMapProperties
import com.google.maps.android.compose.DefaultMapUiSettings
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Polyline
import me.uni.hiker.model.point.Point
import me.uni.hiker.ui.theme.AppTheme

@Composable
fun TrackDetailsView(
    cameraPositionState: CameraPositionState,
    isGpsEnabled: Boolean,
    points: List<Point>,
) {
    val polyPoints = remember ("${points.size}") {
        points.map { LatLng(it.lat, it.lon) }
    }

    GoogleMap(
        modifier = Modifier,
        cameraPositionState = cameraPositionState,
        properties = DefaultMapProperties.copy(
            mapType = MapType.TERRAIN,
            minZoomPreference = 10f,
            isMyLocationEnabled = isGpsEnabled,
        ),
        uiSettings = DefaultMapUiSettings.copy(
            mapToolbarEnabled = false,
            scrollGesturesEnabled = true,
            myLocationButtonEnabled = isGpsEnabled,
        ),
    ) {
        Polyline(
            points = polyPoints,
            width = 16f,
            color = AppTheme.colors.path,
            jointType = JointType.BEVEL,
        )
    }
}