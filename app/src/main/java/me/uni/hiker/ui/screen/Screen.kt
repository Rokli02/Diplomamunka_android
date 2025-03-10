package me.uni.hiker.ui.screen

import kotlinx.serialization.Serializable
import me.uni.hiker.ui.screen.map.model.MapViewType

sealed class Screen {
    // Main Screens
    @Serializable data object Home : Screen()
    @Serializable data object Others: Screen()
    @Serializable data class MainMap(val initialScreen: String = MapViewType.ALL_TRACKS.name): Screen()

    // Auth Screens
    @Serializable data object Auth: Screen()
    @Serializable data object Login : Screen()
    @Serializable data object SignUp : Screen()

    // Map Screens
    @Serializable data object AllTrackMap: Screen()
    @Serializable data object RecordTrackMap: Screen()
    @Serializable data class TrackDetailsMap(val trackId: Long): Screen()

    companion object {
        const val BASE_URI = "hiker://main_activity"
    }
}