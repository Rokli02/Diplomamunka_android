package me.uni.hiker.ui.theme

import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LightGreen = Color(0xFF6cca74)
val Green = Color(0xFF3EB94C)
val LightRed = Color(0xFFE14545)
val Red = Color(0xFFE14545)
val Black = Color(0xFF14171C)
val Blue = Color(0xFF457ADC)
val Purple = Color(0xFFB354DF)
val LightGray = Color(0xFFB2BDB2)
val BoneWhite = Color(0xFFF8FDEB)

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

@Immutable
data class CustomColor(
    val background: Color,
    val onBackground: Color,
    val onBackgroundSecondary: Color,
    val bar: Color,
    val barSecondary: Color,
    val onBar: Color,
    val surface: Color,
    val onSurface: Color,
    val inputBackground: Color,
    val onInputBackground: Color,
    val error: Color,
    val onError: Color,
    val separator: Color,
    val disabledBackground: Color,
    val disabled: Color,
    val path: Color,
)

val LocalCustomColors = staticCompositionLocalOf {
    CustomColor(
        background = Color.Unspecified,
        onBackground = Color.Unspecified,
        onBackgroundSecondary = Color.Unspecified,
        bar = Color.Unspecified,
        onBar = Color.Unspecified,
        barSecondary = Color.Unspecified,
        surface = Color.Unspecified,
        onSurface = Color.Unspecified,
        inputBackground = Color.Unspecified,
        onInputBackground = Color.Unspecified,
        error = Color.Unspecified,
        onError = Color.Unspecified,
        separator = Color.Unspecified,
        disabledBackground = Color.Unspecified,
        disabled = Color.Unspecified,
        path = Color.Unspecified,
    )
}

val specifiedCustomColors = CustomColor(
    background = BoneWhite,
    onBackground = Black,
    onBackgroundSecondary = LightGreen,
    bar = Green,
    barSecondary = LightGreen,
    onBar = Black,
    surface = LightGray,
    onSurface = Black,
    inputBackground = Color.Unspecified,
    onInputBackground = Black,
    error = Red,
    onError = Black,
    separator = Color.Gray,
    disabledBackground = Black.copy(alpha = .25f),
    disabled = Black.copy(alpha = .6f),
    path = Blue,
)

val CustomIconButtonColors = IconButtonColors(
    containerColor = Color.Transparent,
    contentColor = Black,
    disabledContainerColor = LightGray.copy(alpha = .75f),
    disabledContentColor = Black.copy(alpha = .75f),
)