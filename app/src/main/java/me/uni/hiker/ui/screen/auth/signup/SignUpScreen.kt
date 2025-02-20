package me.uni.hiker.ui.screen.auth.signup

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import me.uni.hiker.R
import me.uni.hiker.ui.layout.AuthLayout
import me.uni.hiker.ui.layout.LocalNavController
import me.uni.hiker.ui.layout.TopBarProps

@Composable
fun SignUpScreen() {
    val navController = LocalNavController

    AuthLayout(
        topBarProps = TopBarProps(
            title = stringResource(id = R.string.signup),
            goBack = {
                navController.popBackStack()
            }
        )
    ) {
        Text("Ja")
    }
}