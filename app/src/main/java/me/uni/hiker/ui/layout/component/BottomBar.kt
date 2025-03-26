package me.uni.hiker.ui.layout.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.uni.hiker.R
import me.uni.hiker.ui.theme.AppTheme
import me.uni.hiker.ui.theme.HikeRTheme

@Composable
fun BottomBar(
    homeItem: ItemAction,
    localItem: ItemAction,
    mapItem: ItemAction,
    sharedItem: ItemAction,
    settingsItem: ItemAction,
) {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 9.dp),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Row (
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .background(AppTheme.colors.barSecondary)
                .padding(horizontal = 8.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            BottomBarItem(
                imageVector = Icons.Default.Home,
                text = stringResource(id = R.string.home_page),
                onClick = homeItem.onClick
            )

            Spacer(modifier = Modifier.width(8.dp))

            BottomBarItem(
                imageVector = Icons.Default.Place,
                text = stringResource(id = R.string.local_tracks),
                onClick = localItem.onClick
            )

            // Map Button PlaceHolder
            Spacer(modifier = Modifier.weight(1f))

            BottomBarItem(
                imageVector = Icons.Default.Share,
                text = stringResource(id = R.string.shared_tracks),
                onClick = sharedItem.onClick
            )

            Spacer(modifier = Modifier.width(8.dp))

            BottomBarItem(
                imageVector = Icons.Default.Settings,
                text = stringResource(id = R.string.settings_page),
                onClick = settingsItem.onClick
            )
        }

        BottomBarMapItem(
            onClick = mapItem.onClick
        )
    }
}

@Composable
private fun BottomBarItem(
    imageVector: ImageVector,
    text: String? = null,
    onClick: () -> Unit = {},
) {
    Column (
        modifier = Modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 3.dp, vertical = 3.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = imageVector,
            contentDescription = text,
            tint = AppTheme.colors.onBar
        )

        if (text != null) {
            Text(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .widthIn(min = 56.dp, max= 74.dp),
                text = text,
                textAlign = TextAlign.Center,
                fontSize = 11.sp,
                lineHeight = 12.sp,
                color = AppTheme.colors.onBar,
            )
        }
    }
}

@Composable
private fun BottomBarMapItem(onClick: () -> Unit) {
    Box(modifier = Modifier
        .offset(y = -(6.dp))
        .shadow(elevation = 4.dp, shape = CircleShape)
    ) {
        Column (
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(AppTheme.colors.bar)
                .clickable(onClick = onClick),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = R.drawable.map_icon),
                contentDescription = stringResource(id = R.string.map),
                tint = AppTheme.colors.onBar,
            )

            Text(
                text = stringResource(id = R.string.map),
                fontSize = 11.sp,
                lineHeight = 12.sp,
                color = AppTheme.colors.onBar,
            )
        }
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