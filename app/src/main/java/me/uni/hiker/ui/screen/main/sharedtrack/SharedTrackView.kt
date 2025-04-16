package me.uni.hiker.ui.screen.main.sharedtrack

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import me.uni.hiker.ui.theme.HikeRTheme

@Composable
fun SharedTrackView() {
    Text("Lecc gooooo")
}

@Preview
@Composable
private fun SharedTrackViewPreview() {
    HikeRTheme {
        SharedTrackView()
    }
}