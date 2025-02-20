package me.uni.hiker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

@Composable
fun HikeRTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalCustomColors provides specifiedCustomColors) {
        MaterialTheme(
          colorScheme = DarkColorScheme,
          typography = Typography,
          content = content
        )
    }
}

object AppTheme {
    val colors: CustomColor
        @Composable
        get() = LocalCustomColors.current
}