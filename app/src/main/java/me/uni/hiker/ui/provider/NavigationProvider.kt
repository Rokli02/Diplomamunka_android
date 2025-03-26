package me.uni.hiker.ui.provider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

private val LocalNavHostController: ProvidableCompositionLocal<NavHostController?> = staticCompositionLocalOf { null }

val LocalNavController: NavHostController
    @Composable
    get() = LocalNavHostController.current ?: throw Error("NavHostController has not been initialized in this scope")

@Composable
fun NavigationProvider(content: @Composable () -> Unit) {
    val localNavController = rememberNavController()

    CompositionLocalProvider(LocalNavHostController provides localNavController) {
        content()
    }
}