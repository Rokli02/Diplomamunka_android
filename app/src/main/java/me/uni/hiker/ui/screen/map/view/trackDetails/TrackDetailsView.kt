package me.uni.hiker.ui.screen.map.view.trackDetails

import android.util.Size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.DefaultMapProperties
import com.google.maps.android.compose.DefaultMapUiSettings
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberMarkerState
import me.uni.hiker.R
import me.uni.hiker.model.point.Point
import me.uni.hiker.ui.theme.AppTheme
import me.uni.hiker.utils.MapUtils
import kotlin.math.atan2

@Composable
fun TrackDetailsView(
    cameraPositionState: CameraPositionState,
    isGpsEnabled: Boolean,
    points: List<Point>,
) {
    val context = LocalContext.current

    val polyPoints = remember (points.size) {
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
        if (points.isNotEmpty()) {
            val startingPointState = rememberMarkerState(key = points.first().id.toString(), position = polyPoints.first())
            val finishPointState = rememberMarkerState(key = points.last().id.toString(), position = polyPoints.last())
            val finishRotation = remember (points.last().id, cameraPositionState.position.bearing) {
                val lastPoint = points[points.size - 1]
                val beforeLastPoint = points[points.size - 2]

                Math.toDegrees(atan2(lastPoint.lon - beforeLastPoint.lon, lastPoint.lat - beforeLastPoint.lat))
                    .toFloat() - cameraPositionState.position.bearing
            }

            Polyline(
                points = polyPoints,
                width = 16f,
                color = AppTheme.colors.path,
                jointType = JointType.BEVEL,
            )

            Marker(
                title = context.getString(R.string.start),
                state = startingPointState,
                anchor = Offset(.5f, 0.8425f),
                icon = MapUtils.bitmapDescriptorRes(context, R.drawable.flag, Size(96, 96))
            )

            Marker(
                title = context.getString(R.string.finish),
                state = finishPointState,
                anchor = Offset(.5f, .5f), // y = .8333f
                icon = MapUtils.bitmapDescriptorRes(context, R.drawable.finish_flag, Size(96, 96)),
                rotation = finishRotation,
            )
        }
    }
}