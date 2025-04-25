package me.uni.hiker.model.track

import androidx.compose.runtime.Immutable
import me.uni.hiker.api.model.RemoteTrack
import me.uni.hiker.model.point.Point
import me.uni.hiker.utils.toLocalDate
import java.time.LocalDate

@Immutable
data class Track (
    val id: Long? = null,
    val remoteId: String? = null,
    val name: String,
    override val lat: Double,
    override val lon: Double,
    val length: Float,
    val points: List<Point>? = null,
    val createdAt: LocalDate,
    val updatedAt: LocalDate,
): AbstractTrack() {
    fun toEntity(userId: String?): me.uni.hiker.db.entity.Track {
        return me.uni.hiker.db.entity.Track(
            remoteId = remoteId,
            name = name,
            lat = lat,
            lon = lon,
            userId = userId,
            length = length,
            createdAt = LocalDate.now(),
            updatedAt = LocalDate.now(),
        )
    }

    companion object {
        fun fromEntity(entity: me.uni.hiker.db.entity.Track): Track {
            return Track(
                id = entity.id,
                remoteId = entity.remoteId,
                name = entity.name,
                lat = entity.lat,
                lon = entity.lon,
                length = entity.length,
                createdAt = entity.createdAt!!,
                updatedAt = entity.updatedAt!!,
            )
        }

        fun fromRemoteTrack(remoteTrack: RemoteTrack): Track {
            return Track(
                id = null,
                remoteId = remoteTrack.id,
                name = remoteTrack.name,
                lat = remoteTrack.lat,
                lon = remoteTrack.lon,
                length = remoteTrack.length,
                points = remoteTrack.points,
                createdAt = remoteTrack.createdAt.toLocalDate(),
                updatedAt = remoteTrack.updatedAt.toLocalDate(),
            )
        }
    }
}
