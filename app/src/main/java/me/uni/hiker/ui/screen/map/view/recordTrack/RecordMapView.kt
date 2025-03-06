package me.uni.hiker.ui.screen.map.view.recordTrack

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.DefaultMapProperties
import com.google.maps.android.compose.DefaultMapUiSettings
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import me.uni.hiker.model.point.Point
import me.uni.hiker.ui.theme.AppTheme
import me.uni.hiker.ui.theme.HikeRTheme

@Composable
fun RecordMapView(
    locations: SnapshotStateList<Point>,
    cameraPositionState: CameraPositionState,
    isRecording: Boolean = false,
    isGpsEnabled: Boolean = false,
) {
    val polyPoints = remember ("${locations.size}_$isRecording") {
        locations.map { LatLng(it.lat, it.lon) }
    }
    val currentAndLastPolyPoints = remember(
        "${cameraPositionState.position.target.latitude}_${cameraPositionState.position.target.longitude}_${locations.size}"
    ) {
        locations.lastOrNull()?.also {
            return@remember listOf(
                LatLng(it.lat, it.lon),
                LatLng(
                    cameraPositionState.position.target.latitude,
                    cameraPositionState.position.target.longitude
                )
            )
        }

        return@remember listOf<LatLng>()
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
            scrollGesturesEnabled = !isRecording,
            myLocationButtonEnabled = !isRecording && isGpsEnabled,
        ),
    ) {
        Polyline(
            points = polyPoints,
            width = 16f,
            color = AppTheme.colors.path,
            jointType = JointType.BEVEL,
        )
        if (isRecording) {
            Polyline(
                points = currentAndLastPolyPoints,
                width = 16f,
                color = AppTheme.colors.path,
                jointType = JointType.BEVEL,
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
private fun RecordTrackViewPreview() {
    HikeRTheme {
        Box {
            RecordMapView(
                locations = mutableStateListOf(),
                cameraPositionState = rememberCameraPositionState(),
                isGpsEnabled = false,
            )
        }
    }

}