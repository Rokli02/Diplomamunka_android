package me.uni.hiker.ui.screen

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable data object Auth: Screen()

    // Main Screens
    @Serializable data object Home : Screen()
    @Serializable data object Others: Screen()
    @Serializable data object MainMap: Screen()

    // Auth Screens
    @Serializable data object Login : Screen()
    @Serializable data object SignUp : Screen()
}