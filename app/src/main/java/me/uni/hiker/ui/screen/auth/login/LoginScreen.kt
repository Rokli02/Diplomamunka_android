package me.uni.hiker.ui.screen.auth.login

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import me.uni.hiker.R
import me.uni.hiker.ui.layout.AuthLayout
import me.uni.hiker.ui.layout.LocalNavController
import me.uni.hiker.ui.layout.TopBarProps

@Composable
fun LoginScreen() {
    val navController = LocalNavController

    AuthLayout(
        topBarProps = TopBarProps(
            title = stringResource(id = R.string.login),
            goBack = {
                navController.popBackStack()
            }
        )
    ) {
        Text("Ja")
    }
}