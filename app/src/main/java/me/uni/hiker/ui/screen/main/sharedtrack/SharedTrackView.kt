package me.uni.hiker.ui.screen.main.sharedtrack

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.uni.hiker.R
import me.uni.hiker.model.track.Track
import me.uni.hiker.ui.component.Loading
import me.uni.hiker.ui.component.Searchbar
import me.uni.hiker.ui.component.TrackItem
import me.uni.hiker.ui.theme.AppTheme
import me.uni.hiker.ui.theme.HikeRTheme
import java.time.LocalDate

@Composable
fun SharedTrackView(
    filter: String,
    onFilterChange: (String) -> Unit,
    tracks: List<Track>,
    totalCount: Int,
    onLoadMore: () -> Unit,
    onItemClick: (String) -> Unit,
) {
    val context = LocalContext.current

    Box {
        LazyColumn (
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                Spacer(modifier = Modifier.height(107.dp))
            }

            items(
                count = tracks.size,
                key = { i -> i }, //tracks[i].remoteId!!
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
                } else {
                    Spacer(modifier = Modifier.height(20.dp))

                    if (tracks.size < totalCount) {
                        onLoadMore()
                    }
                }
            }

            item {
                if (totalCount == 0 && tracks.isEmpty()) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = context.getString(R.string.no_track_yet),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W400,
                        color = AppTheme.colors.onBackgroundSecondary
                    )
                } else if (tracks.size < totalCount) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Loading(modifier = Modifier.align(Alignment.Center), iconModifier = Modifier.fillMaxSize())
                    }

                } else {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = context.getString(R.string.end_of_list),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W400,
                        color = AppTheme.colors.onBackgroundSecondary
                    )
                }

                Spacer(modifier = Modifier.height(120.dp))
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
            tracks = listOf(
//                Track(
//                    id = 0L,
//                    remoteId = "remoteId",
//                    name = "Track",
//                    lat = 27.0,
//                    lon = 46.2,
//                    length = 1740.25f,
//                    createdAt = LocalDate.now(),
//                    updatedAt = LocalDate.now(),
//                )
            ),
            totalCount = 0,
            onLoadMore = {},
            onItemClick = {},
        )
    }
}