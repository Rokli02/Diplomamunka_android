package me.uni.hiker.model.track

import me.uni.hiker.db.entity.Track
import java.time.LocalDate

data class NewTrack(
    var name: String,
    var lat: Double,
    var lon: Double,
    var length: Float,
) {
    fun toEntity(userId: Long): Track {
        return Track(
            id = null,
            remoteId = null,
            userId = userId,
            name = this.name,
            lat = this.lat,
            lon = this.lon,
            length = this.length,
            createdAt = LocalDate.now(),
            updatedAt = LocalDate.now(),
        )
    }
}
