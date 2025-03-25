package me.uni.hiker.ui.screen.main.others

import androidx.compose.runtime.Composable
import me.uni.hiker.ui.provider.LocalNavController
import me.uni.hiker.ui.provider.UserContext
import me.uni.hiker.ui.screen.Screen

@Composable
fun OthersScreen() {
    val navController = LocalNavController
    val userContext = UserContext

    OthersView(
        isLoggedIn = userContext.isLoggedIn,
        user = userContext.user,
        loginUser = { navController.navigate(Screen.Login) },
        logoutUser = userContext::logout,
    )
}