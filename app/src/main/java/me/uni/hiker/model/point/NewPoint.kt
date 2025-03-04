package me.uni.hiker.model.point

import me.uni.hiker.db.entity.Point

data class NewPoint(
    val lat: Double,
    val lon: Double,
) {
    fun toEntity(order: Int, trackId: Long): Point {
        return Point(
            lat = this.lat,
            lon = this.lon,
            order = order,
            trackId = trackId,
        )
    }

    companion object {
        fun toEntityList(points: List<NewPoint>, trackId: Long): List<Point> {
            return points.mapIndexed { index, point ->
                point.toEntity(index, trackId)
            }
        }
    }
}
