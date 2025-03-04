package me.uni.hiker.ui.screen.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import me.uni.hiker.R
import me.uni.hiker.ui.provider.LocalNavController
import me.uni.hiker.ui.screen.map.component.MapDrawer
import me.uni.hiker.ui.screen.map.component.TopBar
import me.uni.hiker.ui.screen.map.model.ActionType
import me.uni.hiker.ui.screen.map.model.MapViewType
import me.uni.hiker.ui.screen.map.view.allTracks.AllTracksScreen
import me.uni.hiker.ui.screen.map.view.recordTrack.RecordTrackScreen
import me.uni.hiker.ui.screen.map.view.recordTrack.RecordTrackView
import me.uni.hiker.ui.screen.map.view.recordTrack.RecordTrackViewModel

@Composable
fun GoogleMapScreen(initialScreenType: MapViewType) {
    val context = LocalContext.current
    val navController = LocalNavController
    var mapType by remember {
        mutableStateOf(initialScreenType)
    }

    MapDrawer (
        modifier = Modifier.safeContentPadding(),
        goBack = {
            navController.popBackStack()
        },
        onItemClick = { actionType ->
            mapType = when (actionType) {
                ActionType.RECORD_TRACK -> {
                    MapViewType.RECORD_TRACK
                }
                ActionType.VIEW_TRACKS -> {
                    MapViewType.ALL_TRACKS
                }
            }
        }
    ) { closeDrawer ->
        Scaffold (
            topBar = {
                TopBar(
                    title = stringResource(id = R.string.app_name),
                    onClickMenu = closeDrawer,
                )
            },

        ) {
            Box(modifier = Modifier.padding(it)) {
                when (mapType) {
                    MapViewType.ALL_TRACKS -> {
                        AllTracksScreen()
                    }
                    MapViewType.TRACK_DETAILS -> {
                        TODO()
                    }
                    MapViewType.RECORD_TRACK -> {
                        RecordTrackScreen()
                    }
                }

            }
        }
    }
}