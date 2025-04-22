package me.uni.hiker.model.track

import androidx.compose.runtime.Immutable
import java.time.LocalDate

@Immutable
data class Track (
    val id: Long? = null,
    val remoteId: String? = null,
    val name: String,
    override val lat: Double,
    override val lon: Double,
    val length: Float,
    val createdAt: LocalDate,
    val updatedAt: LocalDate,
): AbstractTrack() {
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
    }
}
