package me.uni.hiker.ui.provider

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private val LocalSnackbarState: ProvidableCompositionLocal<SnackbarContext?> = staticCompositionLocalOf { null }

val LocalSnackbarContext: SnackbarContext
    @Composable
    get() = if (LocalSnackbarState.current == null) throw Error("LocalSnackbarState has not been initialized in this scope") else LocalSnackbarState.current!!


@Composable
fun SnackbarProvider(modifier: Modifier = Modifier, content: @Composable (PaddingValues) -> Unit) {
    val state = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val snackbarContext = remember { SnackbarContext(state, coroutineScope) }

    CompositionLocalProvider(LocalSnackbarState provides snackbarContext) {
        content(PaddingValues())
    }

    Box(modifier = Modifier.fillMaxSize().safeDrawingPadding().safeContentPadding()) {
        SnackbarHost(
            modifier = Modifier.align(Alignment.BottomCenter).offset(y = -(24.dp)),
            hostState = state,
        )
    }
}

@Immutable
data class SnackbarContext(
    private val state: SnackbarHostState,
    private val coroutineScope: CoroutineScope,
) {
    private var showJob: Job? = null

    fun showSnackbar(
        message: String,
        duration: SnackbarDuration = SnackbarDuration.Short,
        action: SnackbarAction? = null,
        dismissAction: (() -> Unit)? = {},
    ) {
        showJob?.cancel()

        showJob = coroutineScope.launch {
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

                showJob = null
            }
        }
    }
}

@Immutable
data class SnackbarAction(
    val label: String,
    val onClick: () -> Unit
)