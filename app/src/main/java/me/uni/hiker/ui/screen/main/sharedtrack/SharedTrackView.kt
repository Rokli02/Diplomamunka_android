package me.uni.hiker.ui.screen.main.sharedtrack

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.uni.hiker.model.track.Track
import me.uni.hiker.ui.component.Searchbar
import me.uni.hiker.ui.component.TrackItem
import me.uni.hiker.ui.theme.AppTheme
import me.uni.hiker.ui.theme.HikeRTheme

@Composable
fun SharedTrackView(
    filter: String,
    onFilterChange: (String) -> Unit,
    tracks: List<Track>,
    onItemClick: (String) -> Unit,
) {
    Box {
        LazyColumn (
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Spacer(modifier = Modifier.height(107.dp))
            }

            items(
                count = tracks.size,
                key = { i -> tracks[i].remoteId!! },
            ) { i ->
                val track = tracks[i]

                TrackItem(
                    track = track,
                    onClick = {
                        onItemClick(track.remoteId!!)
                    }
                )

                if (tracks.size - 1 != i) {
                    HorizontalDivider(
                        color = AppTheme.colors.onBackgroundSecondary.copy(alpha = .45f)
                    )
                }
            }
        }

        Searchbar(
            modifier = Modifier
                .fillMaxWidth(.83f)
                .align(Alignment.TopCenter)
                .offset(y = 20.dp),
            filter = filter,
            onFilterChange = onFilterChange,
        )
    }
}

@Preview
@Composable
private fun SharedTrackViewPreview() {
    HikeRTheme {
        SharedTrackView(
            filter = "",
            onFilterChange = {},
            tracks = listOf(),
            onItemClick = {}
        )
    }
}