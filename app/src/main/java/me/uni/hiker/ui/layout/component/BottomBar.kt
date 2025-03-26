package me.uni.hiker.ui.layout.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.uni.hiker.R
import me.uni.hiker.ui.theme.AppTheme
import me.uni.hiker.ui.theme.CustomIconButtonColors
import me.uni.hiker.ui.theme.HikeRTheme

@Composable
fun BottomBar(
    homeItem: ItemAction,
    localItem: ItemAction,
    mapItem: ItemAction,
    sharedItem: ItemAction,
    settingsItem: ItemAction,
) {
    Surface (
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color.Transparent),
        color = AppTheme.colors.barSecondary,
        contentColor = AppTheme.colors.onBar,
    ) {
        Row (
            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Top
        ) {
            BottomBarItem(
                imageVector = Icons.Default.Home,
                text = stringResource(id = R.string.home_page),
                onClick = homeItem.onClick
            )

            BottomBarItem(
                imageVector = Icons.Default.Place,
                text = stringResource(id = R.string.local_tracks),
                onClick = localItem.onClick
            )

            BottomBarMapItem(
                onClick = mapItem.onClick
            )

            BottomBarItem(
                imageVector = Icons.Default.Clear,
                text = "[Placeholder]",
                onClick = sharedItem.onClick
            ) 

            BottomBarItem(
                imageVector = Icons.Default.Settings,
                text = stringResource(id = R.string.settings_page),
                onClick = settingsItem.onClick
            )
        }
    }
}

@Composable
private fun BottomBarItem(
    imageVector: ImageVector,
    text: String? = null,
    onClick: () -> Unit = {},
) {
    Column (
        modifier = Modifier.padding(start = 5.dp, end = 5.dp, top = 3.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconButton(
            modifier = Modifier.size(28.dp),
            onClick = onClick,
            colors = CustomIconButtonColors,
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = imageVector,
                contentDescription = text,
                tint = AppTheme.colors.onBar
            )
        }

        if (text != null) {
            Text(
                text = text,
                fontSize = 11.sp,
                lineHeight = 12.sp,
                color = AppTheme.colors.onBar,
            )
        }
    }
}

@Composable
private fun BottomBarMapItem(onClick: () -> Unit) {
    Column (
        modifier = Modifier.padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconButton(
            modifier = Modifier.size(32.dp),
            colors = CustomIconButtonColors,
            onClick = onClick,
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = R.drawable.map_icon),
                contentDescription = stringResource(id = R.string.map),
            )
        }

        Text(
            text = stringResource(id = R.string.map),
            fontSize = 11.sp,
            lineHeight = 12.sp,
            color = AppTheme.colors.onBar,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomBarPreview() {
    val mockItemAction = ItemAction(
        onClick = {}
    )

    HikeRTheme {
        BottomBar(
            homeItem = mockItemAction,
            localItem = mockItemAction,
            mapItem = mockItemAction,
            sharedItem = mockItemAction,
            settingsItem = mockItemAction,
        )
    }
}

@Immutable
data class ItemAction(val onClick: () -> Unit)