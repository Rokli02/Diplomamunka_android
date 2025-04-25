package me.uni.hiker.ui.screen.main.localtrack

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import me.uni.hiker.ui.provider.UserContext
import me.uni.hiker.ui.screen.Screen
import androidx.core.net.toUri

@Composable
fun LocalTrackScreen(
    localTrackViewModel: LocalTrackViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val filter by localTrackViewModel.filterFlow.collectAsState()
    val trackFlow by localTrackViewModel.trackFlowState.collectAsState()

    LocalTrackView(
        filter = filter,
        onFilterChange = localTrackViewModel::onFilterChange,
        tracks = trackFlow.collectAsLazyPagingItems(),
        onItemClick = {
            val trackDetailsIntent = Intent(
                Intent.ACTION_VIEW,
                "${Screen.BASE_URI}/map/details?trackId=$it".toUri()
            )

            context.startActivity(trackDetailsIntent)
        }
    )
}