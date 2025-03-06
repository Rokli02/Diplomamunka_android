package me.uni.hiker.ui.screen.map

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import me.uni.hiker.R
import me.uni.hiker.ui.provider.LocalNavController
import me.uni.hiker.ui.screen.map.component.MapDrawer
import me.uni.hiker.ui.screen.map.component.TopBar
import me.uni.hiker.ui.screen.map.model.ActionType
import me.uni.hiker.ui.screen.map.model.MapViewType
import me.uni.hiker.ui.screen.map.view.allTracks.AllTracksScreen
import me.uni.hiker.ui.screen.map.view.recordTrack.RecordTrackScreen

@Composable
fun GoogleMapScreen(initialScreenType: MapViewType) {
    val navController = LocalNavController
    val context = LocalContext.current

    var mapType by remember (initialScreenType) { mutableStateOf(initialScreenType) }
    val topBarTitle = remember(mapType) { "${context.getString(R.string.app_name)}-${getTitleByMapType(mapType, context)}" }

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
                    title = topBarTitle,
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
                        RecordTrackScreen(goBack = {
                            mapType = MapViewType.ALL_TRACKS
                        })
                    }
                }

            }
        }
    }
}

fun getTitleByMapType(mapType: MapViewType, context: Context): String {
    return when (mapType) {
        MapViewType.ALL_TRACKS -> context.getString(R.string.view_tracks)
        MapViewType.TRACK_DETAILS -> TODO()
        MapViewType.RECORD_TRACK -> context.getString(R.string.record_track)
    }
}