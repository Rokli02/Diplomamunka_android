package me.uni.hiker.model.track

data class ClusteredTrack(
    override val lat: Double,
    override val lon: Double,
    val size: Int,
): AbstractTrack()
