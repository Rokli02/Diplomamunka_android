package me.uni.hiker.ui.screen.map.view.allTracks

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.DefaultMapUiSettings
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import me.uni.hiker.R
import me.uni.hiker.model.track.AbstractTrack
import me.uni.hiker.model.track.ClusteredTrack
import me.uni.hiker.model.track.Track
import me.uni.hiker.ui.theme.HikeRTheme
import me.uni.hiker.utils.MapUtils

private val hungarySoutwestPoint = LatLng(45.76092, 15.81845)
private val hungaryNortheasePoint = LatLng(48.77124, 23.30561)

@Composable
fun BoxScope.AllTracksView(
    tracks: List<AbstractTrack>,
    cameraPositionState: CameraPositionState,
    isCurrentLocationEnabled: Boolean = false,
) {
    val context = LocalContext.current
    
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
        if (tracks.isNotEmpty()) {
            fun onSimpleTrackClick(track: Track) {
                println(track)
            }

            tracks.forEach { track ->
                when (track) {
                    is Track -> {
                        Marker(
                            state = MarkerState(LatLng(track.lat, track.lon)),
                            contentDescription = track.name,
                            title = track.name,
                            tag = track,
                            icon = MapUtils.bitmapDescriptorRes(context, R.drawable.cluster_1),
                            anchor = Offset(.5f, .5f),
                            onClick = {
                                onSimpleTrackClick(it.tag as Track)

                                false
                            }
                        )
                    }
                    is ClusteredTrack -> { TODO("Implement the placing of marks and usage of different icons to visualize their size") }
                }
            }
        }
    }

    // TODO: SwipeUpInfoBlock here
//    Box(modifier = Modifier.fillMaxWidth().height(60.dp).align(Alignment.BottomCenter).background(Color.Red))
}

@Preview
@Composable
private fun AllTracksViewPreview() {
    HikeRTheme {

    }
}