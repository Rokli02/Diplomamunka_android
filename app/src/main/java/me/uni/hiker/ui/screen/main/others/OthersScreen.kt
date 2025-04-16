package me.uni.hiker.ui.screen.main.others

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import me.uni.hiker.ui.provider.LocalNavController
import me.uni.hiker.ui.provider.UserContext
import me.uni.hiker.ui.screen.Screen

@Composable
fun OthersScreen(othersViewModel: OthersViewModel = hiltViewModel()) {
    val navController = LocalNavController
    val userContext = UserContext

    val isServerAvailable by othersViewModel.isServerAvailable.collectAsState()

    OthersView(
        isLoggedIn = userContext.isLoggedIn,
        user = userContext.user,
        loginUser = { navController.navigate(Screen.Login) },
        logoutUser = userContext::logout,
        isServerAvailable = isServerAvailable,
    )
}