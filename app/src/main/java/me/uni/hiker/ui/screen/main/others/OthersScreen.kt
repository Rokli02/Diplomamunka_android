package me.uni.hiker.ui.screen.main.others

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import me.uni.hiker.R
import me.uni.hiker.ui.component.MenuItem
import me.uni.hiker.ui.layout.BasicLayout
import me.uni.hiker.ui.layout.component.TopBarIcon
import me.uni.hiker.ui.provider.LocalNavController
import me.uni.hiker.ui.provider.NavigationProvider
import me.uni.hiker.ui.layout.component.TopBarTitle
import me.uni.hiker.ui.provider.UserContext
import me.uni.hiker.ui.screen.Screen
import me.uni.hiker.ui.theme.HikeRTheme

@Composable
fun OthersScreen() {
    val context = LocalContext.current
    val navController = LocalNavController
    val userContext = UserContext

    val topBarIcons = remember (userContext.isLoggedIn) {
        val result = mutableListOf<TopBarIcon>()

        if (userContext.isLoggedIn) {
            result.add(
                TopBarIcon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    description = context.getString(R.string.logout),
                    onClick = {
                        userContext.logout()
                    }
                )
            )
        }

        result
    }

    BasicLayout (
        topBarTitle = TopBarTitle(
            stringResource(id = R.string.settings_page),
            imageVector = Icons.Default.Settings
        ),
        topBarIcons = topBarIcons,
    ) {
        Column {
            if (userContext.isLoggedIn) {
                MenuItem(
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    text = context.getString(R.string.logout),
                    onClick = {
                        userContext.logout()
                    },
                )
            } else {
                MenuItem(
                    icon = Icons.Default.AccountCircle,
                    text = stringResource(id = R.string.login),
                    onClick = {
                        navController.navigate(Screen.Login)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OtherScreenPreview() {
    HikeRTheme {
        NavigationProvider {
            OthersScreen()
        }
    }
}