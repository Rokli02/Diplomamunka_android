package me.uni.hiker.ui.screen.map.view.allTracks

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.DefaultMapUiSettings
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import me.uni.hiker.R
import me.uni.hiker.model.track.AbstractTrack
import me.uni.hiker.model.track.ClusteredTrack
import me.uni.hiker.model.track.Track
import me.uni.hiker.ui.theme.HikeRTheme
import me.uni.hiker.utils.MapUtils

private val hungarySoutwestPoint = LatLng(45.76092, 15.81845)
private val hungaryNortheasePoint = LatLng(48.77124, 23.30561)

@Composable
fun AllTracksView(
    tracks: List<AbstractTrack>,
    cameraPositionState: CameraPositionState,
    focusTrack: (track: Track) -> Unit,
    isCurrentLocationEnabled: Boolean = false,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = isCurrentLocationEnabled,
            mapType = MapType.TERRAIN,
            minZoomPreference = 8f,
            latLngBoundsForCameraTarget = LatLngBounds(hungarySoutwestPoint, hungaryNortheasePoint),
        ),
        uiSettings = DefaultMapUiSettings.copy(
            compassEnabled = true,
            myLocationButtonEnabled = isCurrentLocationEnabled,
            mapToolbarEnabled = false,
            zoomControlsEnabled = true,
        ),
    ) {
        tracks.run {
            if (isEmpty()) return@run

            forEach { track ->
                when (track) {
                    is Track -> {
                        AdvancedMarker(
                            state = MarkerState(LatLng(track.lat, track.lon)),
                            contentDescription = track.name,
                            title = track.name,
                            tag = track,
                            anchor = Offset(.5f, .5f),
                            onInfoWindowClick = {
                                focusTrack(track)
                            },
                        )
                    }
                    is ClusteredTrack -> {
                        val clusterIcon = when {
                            track.size < 10 -> MapUtils.bitmapDescriptorRes(context, R.drawable.cluster_1)
                            track.size < 25 -> MapUtils.bitmapDescriptorRes(context, R.drawable.cluster_1)
                            else -> MapUtils.bitmapDescriptorRes(context, R.drawable.cluster_1)
                        }

                        Marker(
                            state = MarkerState(LatLng(track.lat, track.lon)),
                            tag = track,
                            icon = clusterIcon,
                            anchor = Offset(.5f, .5f),
                            onClick = {
                                coroutineScope.launch {
                                    try {
                                        cameraPositionState.animate(
                                            update = CameraUpdateFactory.newLatLngZoom(it.position, cameraPositionState.position.zoom + 2f),
                                            durationMs = 500
                                        )
                                    } catch (exc: CancellationException) {
                                        Log.w("CancellationException", "Zooming to a clustered point has been cancelled")
                                    }
                                }

                                true
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AllTracksViewPreview() {
    HikeRTheme {

    }
}