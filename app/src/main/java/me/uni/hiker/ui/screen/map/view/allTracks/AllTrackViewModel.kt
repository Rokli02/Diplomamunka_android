package me.uni.hiker.ui.screen.map.view.allTracks

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.uni.hiker.R
import me.uni.hiker.api.model.SaveRequestBody
import me.uni.hiker.api.service.TrackService
import me.uni.hiker.db.HikerDatabase
import me.uni.hiker.db.dao.PointDAO
import me.uni.hiker.db.dao.TrackDAO
import me.uni.hiker.exception.AlreadyExistingDataException
import me.uni.hiker.exception.InvalidDataException
import me.uni.hiker.model.Profile
import me.uni.hiker.model.point.Point
import me.uni.hiker.model.track.AbstractTrack
import me.uni.hiker.model.track.Track
import me.uni.hiker.ui.screen.map.service.clusterTracks
import javax.inject.Inject
import kotlin.math.min

const val TRACK_LOAD_DEBOUNCE_TIME = 250L
const val CLUSTER_DISTANCE_DIVISOR = 6
private val middleOfHungary = LatLng(47.48856, 19.04892)

@HiltViewModel
class AllTrackViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val profile: Profile,
    private val hikerDatabase: HikerDatabase,
    private val trackDAO: TrackDAO,
    private val pointDAO: PointDAO,
    private val trackService: TrackService,
): ViewModel() {
    val clusteredTracks = mutableStateListOf<AbstractTrack>()
    val cameraPositionState = CameraPositionState(CameraPosition.fromLatLngZoom(middleOfHungary, 14f))
    private var trackLoadJob: Job? = null

    fun cancelTrackLoad() { trackLoadJob?.cancel() }

    fun loadTracks(bounds: LatLngBounds, userId: String?) {
        trackLoadJob?.cancel()

        trackLoadJob = viewModelScope.launch {
            delay(TRACK_LOAD_DEBOUNCE_TIME)

            clusteredTracks.clear()

            val dbTracks = trackDAO.findAll(
                minLat = bounds.southwest.latitude,
                maxLat = bounds.northeast.latitude,
                minLon = bounds.southwest.longitude,
                maxLon = bounds.northeast.longitude,
                userId = userId,
            ).map(Track::fromEntity)

            val clusterDistanceThreshold = min(
                bounds.northeast.latitude - bounds.southwest.latitude,
                bounds.northeast.longitude - bounds.southwest.longitude) / CLUSTER_DISTANCE_DIVISOR

            Log.d("ATVM", "Loading ${dbTracks.size} tracks from device | $bounds")

            val remoteClusteredTracks: MutableList<AbstractTrack> = mutableListOf()

            val idString = dbTracks.filter { it.remoteId != null }.map { it.remoteId!! }

            try {
                val response = trackService.getByBoundaries(
                    minLat = bounds.southwest.latitude,
                    maxLat = bounds.northeast.latitude,
                    minLon = bounds.southwest.longitude,
                    maxLon = bounds.northeast.longitude,
                    clusterDistanceDivisor = clusterDistanceThreshold.toFloat(),
                    ids = idString,
                )

                if (response.isSuccessful) {
                    response.body()?.also { remoteTrack ->
                        remoteTrack.map {
                            if (it.isCluster())
                                it.toCluster()
                            else
                                it.toTrack()
                        }.also {
                            remoteClusteredTracks.addAll(it)
                        }
                    }
                }
            } catch (exc: Exception) {
                Log.e("ATVM", exc.message ?: "Couldn't load tracks from server")
            }

            clusteredTracks.addAll(clusterTracks(dbTracks, clusterDistanceThreshold, remoteClusteredTracks))
        }
    }

    suspend fun shareTrack(track: Track): String {
        if (track.id == null) {
            throw InvalidDataException(context.getString(R.string.unable_to_save))
        }

        val points = pointDAO.findAllByTrack(track.id)

        if (points.isEmpty()) {
            throw InvalidDataException(context.getString(R.string.track_does_not_contain_points))
        }

        val response = trackService.save(track = SaveRequestBody(track.name, Point.fromEntityList(points)))

        if (!response.isSuccessful) {
            Log.e("ATVM", "${response.code()} | ${response.message()}")

            throw RuntimeException()
        }

        response.body()?.run {
            val trackFromDB = trackDAO.findById(track.id, profile.user?.remoteId)
            trackFromDB?.copy(remoteId = id)?.also { copiedTrackFromDB ->
                trackDAO.updateOne(copiedTrackFromDB)
            }

            return id
        }

        throw RuntimeException()
    }

    suspend fun saveTrack(track: Track): Long {
        if (track.remoteId == null) {
            throw InvalidDataException(context.getString(R.string.unable_to_save))
        }

        val similarTrackFromDB = trackDAO.findSimilar(track.lat, track.lon)
        if (similarTrackFromDB != null) {
            if (
                similarTrackFromDB.userId == null ||
                similarTrackFromDB.userId == profile.user?.remoteId
            ) throw AlreadyExistingDataException(context.getString(R.string.similar_track_already_on_device))
        }

        var points: List<Point>? = null
        val response = trackService.getById(track.remoteId)
        if (response.isSuccessful) {
            val trackFromServer = response.body()
                ?: throw InvalidDataException(context.getString(R.string.unable_to_save))

            points = trackFromServer.points
        }

        if (points.isNullOrEmpty()) {
            throw InvalidDataException(context.getString(R.string.track_does_not_contain_points))
        }

        val trackId = hikerDatabase.withTransaction {
            val trackId = trackDAO.insertOne(track.toEntity(profile.user?.remoteId))

            pointDAO.insertAll(points.mapIndexed { index, point -> point.toEntity(trackId, index)})

            trackId
        }

        return trackId
    }
}