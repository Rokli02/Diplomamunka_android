package me.uni.hiker.ui.provider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import me.uni.hiker.R
import me.uni.hiker.ui.screen.Screen
import me.uni.hiker.utils.session.UserSessionViewModel
import me.uni.hiker.utils.session.clearUserData

private val LocalUserState = compositionLocalOf<UserSessionViewModel?>{ null }


val UserContext: UserSessionViewModel
    @Composable
    get() = LocalUserState.current ?: throw Error("LocalUserContext has not been initialized in this scope")

@Composable
fun UserSessionProvider(userSessionViewModel: UserSessionViewModel, content: @Composable () -> Unit) {
    val snackbarContext = LocalSnackbarContext
    val navController = LocalNavController
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val unsubscribeProfileChanges = userSessionViewModel.profile?.addSubscriber { userFromProfile ->
            userSessionViewModel.user = userFromProfile

            if (userFromProfile == null) {
                snackbarContext.showSnackbar(
                    message = context.getString(R.string.got_logged_out),
                    action = SnackbarAction(context.getString(R.string.login)) {
                        navController.popBackStack()
                        navController.navigate(Screen.Login)
                    }
                )
                userSessionViewModel.userSharedPreferences.clearUserData()
            }
        }

        onDispose {
            unsubscribeProfileChanges?.invoke()
        }
    }

    CompositionLocalProvider(LocalUserState provides userSessionViewModel) {
        content()
    }
}