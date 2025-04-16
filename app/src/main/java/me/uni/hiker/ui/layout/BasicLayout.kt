package me.uni.hiker.ui.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import me.uni.hiker.R
import me.uni.hiker.ui.layout.component.BottomBar
import me.uni.hiker.ui.layout.component.ItemAction
import me.uni.hiker.ui.layout.component.TopBar
import me.uni.hiker.ui.layout.component.TopBarIcon
import me.uni.hiker.ui.layout.component.TopBarTitle
import me.uni.hiker.ui.provider.LocalNavController
import me.uni.hiker.ui.provider.NavigationProvider
import me.uni.hiker.ui.provider.SnackbarProvider
import me.uni.hiker.ui.screen.Screen
import me.uni.hiker.ui.theme.AppTheme
import me.uni.hiker.ui.theme.HikeRTheme

@Composable
fun BasicLayout(
    topBarTitle: TopBarTitle,
    topBarIcons: List<TopBarIcon> = listOf(),
    content: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val navController = LocalNavController

    Column (
        modifier = Modifier
            .fillMaxSize()
            .safeGesturesPadding()
    ) {
        TopBar(title = topBarTitle, icons = topBarIcons)

        Box(
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = AppTheme.colors.background,
                contentColor = AppTheme.colors.onBackground,
            ) {
                content()
            }

            BottomBar(
                homeItem = ItemAction(
                    onClick = {
                        coroutineScope.launch {
                            navController.popBackStack()
                            navController.navigate(Screen.Home)
                        }
                    }
                ),
                localItem = ItemAction(
                    onClick = {
                        coroutineScope.launch {
                            navController.popBackStack()
                            navController.navigate(Screen.LocalTrack)
                        }
                    }
                ),
                mapItem = ItemAction(
                    onClick = {
                        coroutineScope.launch {
                            navController.navigate(Screen.GoogleMap)
                        }
                    }
                ),
                sharedItem = ItemAction(
                    onClick = {
                        coroutineScope.launch {
                            navController.popBackStack()
                            navController.navigate(Screen.SharedTrack)
                        }
                    }
                ),
                settingsItem = ItemAction(
                    onClick = {
                        coroutineScope.launch {
                            navController.popBackStack()
                            navController.navigate(Screen.Others)
                        }
                    }
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BasicLayoutPreview() {
    val context = LocalContext.current

    HikeRTheme {
        NavigationProvider {
            SnackbarProvider {
                BasicLayout (
                    topBarTitle = TopBarTitle(context.getString(R.string.home_page), Icons.Filled.Home)
                ) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 24.dp), contentAlignment = Alignment.TopCenter) {
                        Text("Basic Layout Preview", fontSize = 24.sp)
                    }
                }
            }
        }
    }
}
