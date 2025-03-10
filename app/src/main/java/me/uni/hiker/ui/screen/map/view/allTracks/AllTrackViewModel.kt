package me.uni.hiker.ui.screen.map.view.allTracks

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.uni.hiker.db.dao.TrackDAO
import me.uni.hiker.model.track.AbstractTrack
import me.uni.hiker.model.track.Track
import me.uni.hiker.ui.screen.map.service.clusterTracks
import javax.inject.Inject
import kotlin.math.min

const val TRACK_LOAD_DEBOUNCE_TIME = 350L
const val CLUSTER_DISTANCE_DIVISOR = 6
private val middleOfHungary = LatLng(47.48856, 19.04892)

@HiltViewModel
class AllTrackViewModel @Inject constructor(
    private val trackDAO: TrackDAO,
): ViewModel() {
    val clusteredTracks = mutableStateListOf<AbstractTrack>()
    val cameraPositionState = CameraPositionState(CameraPosition.fromLatLngZoom(middleOfHungary, 14f))
    private var trackLoadJob: Job? = null

    init {
        viewModelScope.launch {
            delay(500)

            cameraPositionState.projection?.also { proj ->
                loadTracks(proj.visibleRegion.latLngBounds)
            }
        }
    }

    fun cancelTrackLoad() { trackLoadJob?.cancel() }

    fun loadTracks(bounds: LatLngBounds) {
        trackLoadJob?.cancel()

        trackLoadJob = viewModelScope.launch {
            delay(TRACK_LOAD_DEBOUNCE_TIME)

            clusteredTracks.clear()

            val dbTracks = trackDAO.findAll(
                minLat = bounds.southwest.latitude,
                maxLat = bounds.northeast.latitude,
                minLon = bounds.southwest.longitude,
                maxLon = bounds.northeast.longitude,
                userId = null
            ).map(Track::fromEntity)

            val clusterDistanceThreshold = min(
                bounds.northeast.latitude - bounds.southwest.latitude,
                bounds.northeast.longitude - bounds.southwest.longitude) / CLUSTER_DISTANCE_DIVISOR

            Log.d("AllTrackViewModel", "Loading ${dbTracks.size} tracks from device | $bounds")

            clusteredTracks.addAll(clusterTracks(dbTracks, clusterDistanceThreshold))
        }
    }
}