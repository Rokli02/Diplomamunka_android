package me.uni.hiker.ui.screen.main.localtrack

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
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.flow
import me.uni.hiker.model.track.Track
import me.uni.hiker.ui.component.Searchbar
import me.uni.hiker.ui.component.Skeleton
import me.uni.hiker.ui.component.TrackItem
//import me.uni.hiker.ui.provider.BasicLayoutProvider
import me.uni.hiker.ui.theme.AppTheme
import me.uni.hiker.ui.theme.HikeRTheme

@Composable
fun LocalTrackView(
    filter: String,
    onFilterChange: (String) -> Unit,
    tracks: LazyPagingItems<Track>,
    onItemClick: (Long) -> Unit,
) {
    Box(modifier = Modifier) {
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                Spacer(modifier = Modifier.height(107.dp))
            }


            items(
                count = tracks.itemCount,
                key = tracks.itemKey { it.id!! }
            ) { index ->
                val item = tracks[index]

                item?.run {
                    TrackItem(
                        track = this,
                        onClick = {
                            onItemClick(id!!)
                        }
                    )

                    if (tracks.itemCount - 1 != index) {
                        HorizontalDivider(
                            color = AppTheme.colors.onBackgroundSecondary.copy(alpha = .45f)
                        )
                    }
                }
            }

            if (tracks.loadState.append == LoadState.Loading) {
                items (3) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Skeleton(modifier = Modifier.fillMaxWidth().height(72.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

            item {
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
private fun LocalTrackViewPreview() {
    val e = flow<PagingData<Track>> {

    }.collectAsLazyPagingItems()

    HikeRTheme {
        LocalTrackView(
            filter = "",
            onFilterChange = {},
            tracks = e,
            onItemClick = {},
        )
    }
}