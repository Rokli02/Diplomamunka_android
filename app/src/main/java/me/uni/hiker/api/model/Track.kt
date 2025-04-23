package me.uni.hiker.api.model

import androidx.compose.runtime.Immutable
import me.uni.hiker.model.point.Point
import me.uni.hiker.model.track.AbstractTrack
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

//FIXME: Hibát dob, mert a szerverről visszatért AbstractTrack nem teljesen ugyan az, mint az itt tárolt
typealias GetByBoundariesResponse = AbstractTrack

data class SaveRequestBody(val name: String, val points: List<Point>)
data class SaveResponse(val id: String)
