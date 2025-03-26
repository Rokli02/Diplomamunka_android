package me.uni.hiker.ui.screen

import kotlinx.serialization.Serializable

sealed class Screen {
    // Main Screens
    @Serializable data object Home : Screen()
    @Serializable data object Others: Screen()
    @Serializable data object LocalTrack: Screen()

    // Auth Screens
    @Serializable data object Auth: Screen()
    @Serializable data object Login : Screen()
    @Serializable data object SignUp : Screen()

    // Map Screens
    @Serializable data object GoogleMap: Screen()
    @Serializable data object AllTrackMap: Screen()
    @Serializable data object RecordTrackMap: Screen()
    @Serializable data class TrackDetailsMap(val trackId: Long, val isRemote: Boolean = false): Screen()

    companion object {
        const val BASE_URI = "hiker://main_activity"
        const val TRACK_DETAILS_URI = "${BASE_URI}/map/details?trackId={trackId}&isRemote={isRemote}"
        const val RECORD_TRACK_URI = "${BASE_URI}/map/record"
    }
}