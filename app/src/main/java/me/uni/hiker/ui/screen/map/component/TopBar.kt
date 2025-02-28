package me.uni.hiker.ui.screen.map.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import me.uni.hiker.ui.theme.AppTheme
import me.uni.hiker.ui.theme.CustomIconButtonColors
import me.uni.hiker.ui.theme.HikeRTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    onClickMenu: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        },
        colors = TopAppBarColors(
            containerColor = AppTheme.colors.bar,
            scrolledContainerColor = AppTheme.colors.bar,
            navigationIconContentColor = AppTheme.colors.onBar,
            titleContentColor = AppTheme.colors.onBar,
            actionIconContentColor = AppTheme.colors.onBar,
        ),
        navigationIcon = {
            IconButton(
                onClick = onClickMenu,
                colors = CustomIconButtonColors,
            ) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Toggle Drawer Menu")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun TopBarPreview() {
    HikeRTheme {
        TopBar(title = "Test Top Bar, but a bit longer than usual", onClickMenu = {})
    }
}