package me.uni.hiker.api.model

import me.uni.hiker.model.point.Point
import me.uni.hiker.model.track.AbstractTrack
import me.uni.hiker.model.track.Track

typealias GetAllResponse = Page<Track>

typealias GetByIdResponse = Track

typealias GetByBoundariesResponse = AbstractTrack

data class SaveRequestBody(val name: String, val points: List<Point>)
data class SaveResponse(val id: String)
