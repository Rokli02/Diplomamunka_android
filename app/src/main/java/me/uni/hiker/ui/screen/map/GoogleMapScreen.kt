package me.uni.hiker.ui.screen.map

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
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import me.uni.hiker.R
import me.uni.hiker.ui.provider.LocalNavController
import me.uni.hiker.ui.screen.Screen
import me.uni.hiker.ui.screen.map.component.MapDrawer
import me.uni.hiker.ui.screen.map.component.TopBar
import me.uni.hiker.ui.screen.map.model.ActionType
import me.uni.hiker.ui.screen.map.view.allTracks.AllTracksScreen
import me.uni.hiker.ui.screen.map.view.recordTrack.RecordTrackScreen
import me.uni.hiker.ui.screen.map.view.trackDetails.TrackDetailsScreen

@Composable
fun GoogleMapScreen() {
    val navController = LocalNavController
    val context = LocalContext.current
    val mapNavController = rememberNavController()
    var topBarTitle by remember { mutableStateOf(context.getString(R.string.app_name)) }

    fun changeTopBarTitle(title: String) {
        topBarTitle = "${context.getString(R.string.app_name)}-$title"
    }

    MapDrawer (
        modifier = Modifier.safeContentPadding(),
        goBack = {
            navController.popBackStack()
        },
        onItemClick = { actionType ->
            when (actionType) {
                ActionType.RECORD_TRACK -> {
                    if(!mapNavController.popBackStack(Screen.RecordTrackMap, inclusive = false))
                        mapNavController.navigate(Screen.RecordTrackMap)
                }
                ActionType.VIEW_TRACKS -> {
                    if (!mapNavController.popBackStack(Screen.AllTrackMap, inclusive = false))
                        mapNavController.navigate(Screen.AllTrackMap)
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
                NavHost(navController = mapNavController, startDestination = Screen.AllTrackMap) {
                    composable<Screen.AllTrackMap> {
                        LifecycleStartEffect(Unit) {
                            changeTopBarTitle(context.getString(R.string.view_tracks))

                            onStopOrDispose {}
                        }

                        AllTracksScreen(
                            mapNavController = mapNavController,
                        )
                    }
                    composable<Screen.RecordTrackMap>(
                        deepLinks = listOf(
                            navDeepLink {
                                uriPattern = "${Screen.BASE_URI}/map/record"
                            },
                        )
                    ) {
                        LifecycleStartEffect(Unit) {
                            changeTopBarTitle(context.getString(R.string.record_track))

                            onStopOrDispose {}
                        }

                        RecordTrackScreen(goBack = {
                            if (!mapNavController.popBackStack(route = Screen.AllTrackMap, inclusive = false)) {
                                mapNavController.navigate(route = Screen.AllTrackMap)
                            }
                        })
                    }
                    composable<Screen.TrackDetailsMap> { entry ->
                        val tdm = entry.toRoute<Screen.TrackDetailsMap>()

                        LifecycleStartEffect(Unit) {
                            changeTopBarTitle(context.getString(R.string.track_details))

                            onStopOrDispose {}
                        }

                        TrackDetailsScreen(
                            trackId = tdm.trackId,
                            isRemote = tdm.isRemote,
                        )
                    }
                }
            }
        }
    }
}