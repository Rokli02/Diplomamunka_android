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
import me.uni.hiker.api.service.TrackService
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
    private val trackService: TrackService,
): ViewModel() {
    val cameraPositionState: CameraPositionState = CameraPositionState(CameraPosition.fromLatLngZoom(middleOfHungary, 14f))
    var track by mutableStateOf<Track?>(null)
        private set
    var points = mutableStateListOf<Point>()
        private set

    suspend fun getRemoteTrackDetails(trackId: String) {
        try {
            val response = trackService.getById(trackId)

            if (response.isSuccessful) {
                response.body()?.also { remoteTrack ->
                    points.clear()

                    track = Track.fromRemoteTrack(remoteTrack)
                    remoteTrack.points?.mapIndexed { index, point ->
                        point.copy(id = index.toLong())
                    }?.also {
                        points.addAll(it)
                    }
                }
            }
        } catch (exc: Exception) {
            Log.w("TDVM", exc.message ?: "Couldn't get track details from a remote source!")
        }
    }
    suspend fun getTrackDetails(trackId: Long, userId: String?) {
        track = trackDao.findById(id = trackId, userId = userId)?.let{ Track.fromEntity(it) }
        if (track != null) {
            points.run {
                clear()
                addAll(pointDAO.findAllByTrack(trackId).let(Point::fromEntityList))
            }
        }
    }
    suspend fun focusOnPoint(point: LatLng, durationMs: Int = 1000) {
        try {
            cameraPositionState.animate(CameraUpdateFactory.newLatLng(point), durationMs)
        } catch (exc: CancellationException) {
            Log.d("CancellationException", "Camera position animation got canceled when zoomed on track details")
        }
    }
}