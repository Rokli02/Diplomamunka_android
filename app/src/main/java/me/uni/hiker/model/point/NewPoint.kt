package me.uni.hiker.model.point

import me.uni.hiker.db.entity.Point
import me.uni.hiker.db.entity.RecordedLocation

data class NewPoint(
    val lat: Double,
    val lon: Double,
) {
    companion object {
        fun toEntityList(points: List<RecordedLocation>, trackId: Long): List<Point> {
            return points.mapIndexed { index, point -> Point(
                    lat = point.lat,
                    lon = point.lon,
                    order = index,
                    trackId = trackId,
                )
            }
        }
    }
}
