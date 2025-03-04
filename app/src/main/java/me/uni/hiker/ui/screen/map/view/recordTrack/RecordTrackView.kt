package me.uni.hiker.ui.screen.map.view.recordTrack

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
fun BoxScope.RecordTrackView(
    startRecording: () -> Unit,
    stopRecording: () -> Unit,
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .align(Alignment.BottomStart)
            .padding(start = 12.dp, bottom = 32.dp)
    ) {
        if (isRecording) {
            Button(
                onClick = stopRecording,
            ) {
                Text(text = "Stop Notification")
            }
        } else {
            Button(
                onClick = startRecording,
            ) {
                Text(text = "Start Notification")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecordTrackViewPreview() {
    HikeRTheme {
        Box {
            RecordTrackView(
                startRecording = {},
                stopRecording = {},
                locations = mutableStateListOf(),
                cameraPositionState = rememberCameraPositionState(),
                isGpsEnabled = false,
            )
        }
    }

}