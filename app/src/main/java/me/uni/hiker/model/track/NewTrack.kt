package me.uni.hiker.model.track

import me.uni.hiker.db.entity.RecordedLocation
import me.uni.hiker.db.entity.Track
import me.uni.hiker.ui.screen.map.service.getDistanceBetweenLocations
import java.time.LocalDate

data class NewTrack(
    var name: String,
    var lat: Double,
    var lon: Double,
    var length: Float,
) {
    fun toEntity(userId: Long?): Track {
        return Track(
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

    companion object {
        fun fromRecordedPoints(name: String, locations: List<RecordedLocation>): NewTrack {
            var latSum = 0.0
            var lonSum = 0.0
            var trackLength = 0.0

            for (index in 0..locations.size - 2) {
                latSum += locations[index].lat
                lonSum += locations[index].lon

                trackLength += getDistanceBetweenLocations(locations[index], locations[index + 1])
            }

            latSum += locations.last().lat
            lonSum += locations.last().lon

            return NewTrack (
                name = name,
                lat = latSum / locations.size,
                lon = lonSum / locations.size,
                length = trackLength.toFloat(),
            )
        }
    }
}
