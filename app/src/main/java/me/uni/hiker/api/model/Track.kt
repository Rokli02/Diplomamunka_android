package me.uni.hiker.api.model

import androidx.compose.runtime.Immutable
import me.uni.hiker.model.point.Point
import me.uni.hiker.model.track.ClusteredTrack
import me.uni.hiker.model.track.Track
import me.uni.hiker.utils.toLocalDate
import java.util.Date

@Immutable
data class RemoteTrack(
    val id: String,
    val userId: String? = null,
    val name: String,
    val lat: Double,
    val lon: Double,
    val length: Float,
    val points: List<Point>? = null,
    val createdAt: Date,
    val updatedAt: Date,
)

typealias GetAllResponse = Page<RemoteTrack>

typealias GetByIdResponse = RemoteTrack

data class MixedClusterTracks(
    val id: String? = null,
    val userId: String? = null,
    val name: String? = null,
    val lat: Double,
    val lon: Double,
    val size: Int? = null,
    val length: Float? = null,
    val points: List<Point>? = null,
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
) {
    fun isCluster(): Boolean {
        return id == null
    }

    fun toTrack(): Track {
        return Track(
            id = null,
            remoteId = id,
            name = name!!,
            lat = lat,
            lon = lon,
            length = length!!,
            createdAt = createdAt!!.toLocalDate(),
            updatedAt = updatedAt!!.toLocalDate()
        )
    }

    fun toCluster(): ClusteredTrack {
        return ClusteredTrack(
            lat = lat,
            lon = lon,
            size = size!!,
        )
    }
}

typealias GetByBoundariesResponse = List<MixedClusterTracks>

data class SaveRequestBody(val name: String, val points: List<Point>)
data class SaveResponse(val id: String)
