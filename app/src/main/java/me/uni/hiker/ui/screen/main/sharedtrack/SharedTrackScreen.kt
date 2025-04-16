package me.uni.hiker.ui.screen.main.sharedtrack

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SharedTrackScreen(sharedTrackViewModel: SharedTrackViewModel = hiltViewModel()) {

    if (!sharedTrackViewModel.isOnline && sharedTrackViewModel.trackList.isEmpty()) {
        Text("RIP, nincs internet kapcsolat")

        return
    }

    SharedTrackView()
}