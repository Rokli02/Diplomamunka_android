package me.uni.hiker.ui.screen.main.localtrack

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import me.uni.hiker.ui.provider.UserContext

@Composable
fun LocalTrackScreen(
    localTrackViewModel: LocalTrackViewModel = hiltViewModel(),
) {
    val userContext = UserContext

    LaunchedEffect(userContext.user) {
        localTrackViewModel.currentUser = userContext.user
    }

    val filter by localTrackViewModel.filterFlow.collectAsState()
    val trackFlow by localTrackViewModel.trackFlowState.collectAsState()

    LocalTrackView(
        filter = filter,
        onFilterChange = localTrackViewModel::onFilterChange,
        tracks = trackFlow.collectAsLazyPagingItems(),
        onItemClick = {
            Log.d("Track Items", "Clicked on id($it)")
            // TODO("Továbbítson a TrackDetails screen-re")
        }
    )
}