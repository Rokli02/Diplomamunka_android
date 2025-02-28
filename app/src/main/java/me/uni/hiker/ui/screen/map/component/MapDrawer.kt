package me.uni.hiker.ui.screen.map.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import me.uni.hiker.R
import me.uni.hiker.ui.component.MenuItem
import me.uni.hiker.ui.screen.map.model.ActionType
import me.uni.hiker.ui.theme.AppTheme
import me.uni.hiker.ui.theme.HikeRTheme

@Composable
fun MapDrawer(
    modifier: Modifier = Modifier,
    goBack: () -> Unit,
    onItemClick: (ActionType) -> Unit,
    initialState: DrawerValue = DrawerValue.Closed,
    content: @Composable (() -> Unit) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = initialState)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        modifier = modifier,
        drawerContent = {
            ModalDrawerSheet (
                drawerContainerColor = AppTheme.colors.surface,
                drawerContentColor = AppTheme.colors.onSurface,
            ) {
                Column (
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
                ) {
                    // Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier.padding(top = 8.dp, bottom = 20.dp, start = 12.dp),
                            text = stringResource(id = R.string.map_drawer_title),
                            fontSize = 24.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )

                        Icon(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(32.dp)
                                .clip(CircleShape)
                                .clickable {
                                    coroutineScope.launch {
                                        drawerState.close()
                                    }
                                },
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close map drawer",
                            tint = AppTheme.colors.onSurface.copy(alpha = .65f)
                        )
                    }
                    HorizontalDivider(
                        color = AppTheme.colors.separator,
                    )

                    // Body
                    MenuItem(
                        modifier = Modifier.padding(top = 12.dp, bottom = 4.dp),
                        text = stringResource(id = R.string.view_tracks),
                        icon = Icons.Default.Place
                    ) {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        onItemClick(ActionType.VIEW_TRACKS)
                    }
                    MenuItem(
                        modifier = Modifier.padding(vertical = 4.dp),
                        text = stringResource(id = R.string.record_track),
                        icon = Icons.Default.PlayArrow  // TODO: Replace with a camera icon
                    ) {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        onItemClick(ActionType.RECORD_TRACK)
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    // Footer
                    HorizontalDivider(
                        color = AppTheme.colors.separator,
                    )
                    MenuItem(
                        modifier = Modifier.padding(top = 8.dp),
                        text = stringResource(id = R.string.exit_the_map),
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        onClick = goBack,
                    )
                }
            }
        },
        gesturesEnabled = drawerState.isOpen,
        drawerState = drawerState,
    ) {
        content {
            coroutineScope.launch {
                drawerState.open()
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun MapDrawerPreview() {
    HikeRTheme {
        MapDrawer (initialState = DrawerValue.Open, goBack = {}, onItemClick = {}) {}
    }
}
