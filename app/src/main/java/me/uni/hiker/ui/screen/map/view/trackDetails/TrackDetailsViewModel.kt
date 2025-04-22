package me.uni.hiker.ui.screen.map.view.trackDetails

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import me.uni.hiker.db.dao.PointDAO
import me.uni.hiker.db.dao.TrackDAO
import me.uni.hiker.model.point.Point
import me.uni.hiker.model.track.Track
import javax.inject.Inject

private val middleOfHungary = LatLng(47.48856, 19.04892)

@HiltViewModel
class TrackDetailsViewModel @Inject constructor(
    private val trackDao: TrackDAO,
    private val pointDAO: PointDAO,
): ViewModel() {
    val cameraPositionState: CameraPositionState = CameraPositionState(CameraPosition.fromLatLngZoom(middleOfHungary, 14f))
    var track by mutableStateOf<Track?>(null)
        private set
    var points = mutableStateListOf<Point>()
        private set

    suspend fun getRemoteTrackDetails(trackId: String) {
        TODO("Http kérés a szerver felé")
    }
    suspend fun getTrackDetails(trackId: Long, userId: Long?) {
        delay(750)

        track = trackDao.findById(id = trackId, userId = userId)?.let{ Track.fromEntity(it) }
        if (track != null) {
            points.run {
                clear()
                addAll(pointDAO.findAllByTrack(trackId).let(Point::fromEntityList))
            }
        }
    }
    suspend fun focusOnPoint(point: LatLng) {
        try {
            cameraPositionState.animate(CameraUpdateFactory.newLatLng(point), 1000)
        } catch (exc: CancellationException) {
            Log.d("CancellationException", "Camera position animation got canceled when zoomed on track details")
        }
    }
}