package me.uni.hiker.ui.screen.main.others

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import me.uni.hiker.R
import me.uni.hiker.ui.layout.BasicLayout
import me.uni.hiker.ui.provider.LocalNavController
import me.uni.hiker.ui.provider.NavigationProvider
import me.uni.hiker.ui.layout.component.TopBarTitle
import me.uni.hiker.ui.screen.Screen
import me.uni.hiker.ui.theme.HikeRTheme

@Composable
fun OthersScreen() {
    val navController = LocalNavController

    BasicLayout (
        topBarTitle = TopBarTitle(
            stringResource(id = R.string.settings_page),
            imageVector = Icons.Default.Settings
        ),
        topBarIcons = listOf(
//            TopBarIcon(
//                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
//                description = stringResource(id = R.string.logout),
//                onClick = logout
//            )
        )
    ) {
        Column {
            OthersItem(
                icon = Icons.Default.AccountCircle,
                text = stringResource(id = R.string.login)
            ) {
                navController.navigate(Screen.Login)
            }

//            OthersItem(
//                icon = Icons.AutoMirrored.Filled.ExitToApp,
//                text = stringResource(id = R.string.logout),
//                onClick = logout,
//            )
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