package me.uni.hiker.model.point

import androidx.compose.runtime.Immutable

@Immutable
data class Point(
    val id: Long,
    val lat: Double,
    val lon: Double,
) {
    companion object {
        private fun fromEntity(entity: me.uni.hiker.db.entity.Point): Point {
            return Point(
                id = entity.id!!,
                lat = entity.lat,
                lon = entity.lon,
            )
        }

        fun fromEntityList(list: List<me.uni.hiker.db.entity.Point>): List<Point> {
            var lastOrderIndex = -1

            return list.map {
                if (it.order < lastOrderIndex) throw RuntimeException("Point list was not returned into a correct sequence")

                lastOrderIndex = it.order

                fromEntity(it)
            }
        }
    }
}
