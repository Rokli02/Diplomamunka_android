package me.uni.hiker.ui.screen.main.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import me.uni.hiker.ui.provider.LocalNavController
import me.uni.hiker.ui.provider.UserContext
import me.uni.hiker.ui.screen.Screen

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val navController = LocalNavController
    val userContext = UserContext

    LaunchedEffect(userContext.user) {
        homeViewModel.loadLocalTracks()
        homeViewModel.loadSharedTracks()
    }

    val localTracks by homeViewModel.localTracksFlow.collectAsState()
    val sharedTracks by homeViewModel.sharedTracksFlow.collectAsState()

    HomeView(
        isLoggedIn = userContext.isLoggedIn,
        user = userContext.user,
        localTracks = localTracks,
        sharedTracks = sharedTracks,
        navigateToLogin = { navController.navigate(Screen.Login) },
        navigateToMap = { navController.navigate(Screen.GoogleMap) },
        navigateToLocalTrack = {
            navController.popBackStack()
            navController.navigate(Screen.LocalTrack)
        },
        navigateToSharedTrack = {
            navController.popBackStack()
            navController.navigate(Screen.SharedTrack)
        }
    )
}