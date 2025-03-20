package me.uni.hiker.ui.provider

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import me.uni.hiker.utils.session.UserSessionViewModel

private val LocalUserState = compositionLocalOf<UserSessionViewModel?>{ null }


val UserContext: UserSessionViewModel
    @Composable
    get() = LocalUserState.current ?: throw Error("LocalUserContext has not been initialized in this scope")

@Composable
fun UserSessionProvider(userSessionViewModel: UserSessionViewModel, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalUserState provides userSessionViewModel) {
        content()
    }
}