package me.uni.hiker.model.track

import androidx.compose.runtime.Immutable

@Immutable
data class ClusteredTrack(
    override var lat: Double,
    override var lon: Double,
    var size: Int,
): AbstractTrack() {
    companion object {
        fun fromTracks(track1: Track, track2: Track): ClusteredTrack {
            return ClusteredTrack(
                lat = (track1.lat + track2.lat) / 2,
                lon = (track1.lon + track2.lon) / 2,
                size = 2,
            )
        }
    }
}
