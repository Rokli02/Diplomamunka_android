package me.uni.hiker.ui.provider

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private val LocalSnackbarState: ProvidableCompositionLocal<SnackbarContext?> = staticCompositionLocalOf { null }

val LocalSnackbarContext: SnackbarContext
    @Composable
    get() = if (LocalSnackbarState.current == null) throw Error("LocalSnackbarState has not been initialized in this scope") else LocalSnackbarState.current!!


@Composable
fun SnackbarProvider(modifier: Modifier = Modifier, content: @Composable (PaddingValues) -> Unit) {
    val state = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold (
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = state) },
        content = {
            CompositionLocalProvider(LocalSnackbarState provides SnackbarContext(state, coroutineScope)) {
                content(it)
            }
        },
    )
}

@Immutable
data class SnackbarContext(
    private val state: SnackbarHostState,
    private val coroutineScope: CoroutineScope,
) {
    fun showSnackbar(
        message: String,
        duration: SnackbarDuration = SnackbarDuration.Short,
        action: SnackbarAction? = null,
        dismissAction: (() -> Unit)? = {},
    ) {
        coroutineScope.launch {
            state.showSnackbar(
                message = message,
                duration = duration,
                actionLabel = action?.label,
                withDismissAction = dismissAction != null,
            ).also {
                when (it) {
                    SnackbarResult.ActionPerformed -> {
                        action?.onClick?.invoke()
                    }
                    SnackbarResult.Dismissed -> {
                        dismissAction?.invoke()
                    }
                }
            }
        }
    }
}

@Immutable
data class SnackbarAction(
    val label: String,
    val onClick: () -> Unit
)