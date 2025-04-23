package me.uni.hiker.ui.screen.main.sharedtrack

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import me.uni.hiker.ui.provider.UserContext

@Composable
fun SharedTrackScreen(sharedTrackViewModel: SharedTrackViewModel = hiltViewModel()) {
    val userContext = UserContext

    if (!sharedTrackViewModel.isOnline && sharedTrackViewModel.trackList.isEmpty()) {
        Text("RIP, nincs internet kapcsolat")

        return
    }

    if (!userContext.isLoggedIn) {
        Text("Nem vagy bejelentkezve, itt egy link, lépj be hogy lásd a megosztott túrákat")

        return
    }

    SharedTrackView(
        filter = "",
        onFilterChange = {},
        tracks = sharedTrackViewModel.trackList,
        onItemClick = {}
    )
}