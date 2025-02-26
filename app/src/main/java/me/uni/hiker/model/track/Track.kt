package me.uni.hiker.model.track

import androidx.compose.runtime.Immutable
import java.time.LocalDate

@Immutable
data class Track(
    val id: Long?,
    val remoteId: Long?,
    val name: String,
    val lat: Double,
    val lon: Double,
    val length: Float,
    val createdAt: LocalDate,
    val updatedAt: LocalDate,
) {
    fun isShared(): Boolean { return remoteId != null }

    fun toEntity(userId: Long): me.uni.hiker.db.entity.Track {
        return me.uni.hiker.db.entity.Track(
            id = this.id,
            name = this.name,
            remoteId = this.remoteId,
            userId = userId,
            lat = this.lat,
            lon = this.lon,
            length = this.length,
            createdAt = this.createdAt,
            updatedAt = LocalDate.now(),
        )
    }
}
