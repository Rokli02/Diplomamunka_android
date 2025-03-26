package me.uni.hiker.ui.theme

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CardColors
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LightGreen = Color(0xFF6cca74)
val Green = Color(0xFF3EB94C)
val GrayishGreen = Color(0xFF7BA780)
val LightRed = Color(0xFFF89393)
val Red = Color(0xFFE14545)
val GrayishRed = Color(0xFFBD6D6D)
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
    val link: Color,
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
        link = Color.Unspecified,
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
    inputBackground = Color.Transparent,
    onInputBackground = Black,
    error = LightRed,
    onError = Black,
    separator = Color.Gray.copy(alpha = .6f),
    disabledBackground = Black.copy(alpha = .25f),
    disabled = Black.copy(alpha = .6f),
    path = Blue,
    link = LightGreen,
)

val CustomIconButtonColors = IconButtonColors(
    containerColor = Color.Transparent,
    contentColor = Black,
    disabledContainerColor = LightGray.copy(alpha = .75f),
    disabledContentColor = Black.copy(alpha = .75f),
)

val CustomTextFieldColors = TextFieldColors(
    cursorColor = specifiedCustomColors.onInputBackground,
    focusedContainerColor = specifiedCustomColors.inputBackground,
    focusedTextColor = specifiedCustomColors.onInputBackground,
    focusedLabelColor = specifiedCustomColors.onInputBackground.copy(.6f),
    focusedIndicatorColor = specifiedCustomColors.onInputBackground.copy(.6f),
    focusedPrefixColor = specifiedCustomColors.onInputBackground,
    focusedSuffixColor = specifiedCustomColors.onInputBackground,
    focusedPlaceholderColor = specifiedCustomColors.onInputBackground.copy(.6f),
    focusedSupportingTextColor = specifiedCustomColors.onInputBackground,
    focusedTrailingIconColor = specifiedCustomColors.onInputBackground,
    focusedLeadingIconColor = specifiedCustomColors.onInputBackground,

    unfocusedContainerColor = specifiedCustomColors.inputBackground,
    unfocusedTextColor = specifiedCustomColors.onInputBackground,
    unfocusedLabelColor = specifiedCustomColors.onInputBackground.copy(.6f),
    unfocusedIndicatorColor = specifiedCustomColors.onInputBackground.copy(.6f),
    unfocusedPrefixColor = specifiedCustomColors.onInputBackground,
    unfocusedSuffixColor = specifiedCustomColors.onInputBackground,
    unfocusedPlaceholderColor = specifiedCustomColors.onInputBackground.copy(.6f),
    unfocusedSupportingTextColor = specifiedCustomColors.onInputBackground,
    unfocusedTrailingIconColor = specifiedCustomColors.onInputBackground,
    unfocusedLeadingIconColor = specifiedCustomColors.onInputBackground,

    disabledContainerColor = specifiedCustomColors.disabledBackground,
    disabledTextColor = specifiedCustomColors.disabled,
    disabledLabelColor = specifiedCustomColors.disabled.copy(.6f),
    disabledIndicatorColor = specifiedCustomColors.disabled.copy(.6f),
    disabledPrefixColor = specifiedCustomColors.disabled,
    disabledSuffixColor = specifiedCustomColors.disabled,
    disabledPlaceholderColor = specifiedCustomColors.disabled.copy(.6f),
    disabledSupportingTextColor = specifiedCustomColors.disabled,
    disabledTrailingIconColor = specifiedCustomColors.disabled,
    disabledLeadingIconColor = specifiedCustomColors.disabled,

    errorContainerColor = specifiedCustomColors.error,
    errorTextColor = specifiedCustomColors.onError,
    errorLabelColor = specifiedCustomColors.onError.copy(.6f),
    errorIndicatorColor = specifiedCustomColors.onError.copy(.6f),
    errorPrefixColor = specifiedCustomColors.onError,
    errorSuffixColor = specifiedCustomColors.onError,
    errorPlaceholderColor = specifiedCustomColors.onError.copy(.6f),
    errorSupportingTextColor = specifiedCustomColors.onError,
    errorTrailingIconColor = specifiedCustomColors.onError,
    errorLeadingIconColor = specifiedCustomColors.onError,
    errorCursorColor = Red,
    textSelectionColors = TextSelectionColors(
        handleColor = Green,
        backgroundColor = Green.copy(.45f),
    ),
)

val SearchbarColors = TextFieldColors(
    cursorColor = specifiedCustomColors.onInputBackground,
    errorCursorColor = Red,

    focusedContainerColor = specifiedCustomColors.barSecondary,
    focusedTextColor = specifiedCustomColors.onInputBackground,
    focusedLabelColor = specifiedCustomColors.onInputBackground.copy(.6f),
    focusedIndicatorColor = specifiedCustomColors.onInputBackground.copy(.6f),
    focusedPrefixColor = specifiedCustomColors.onInputBackground,
    focusedSuffixColor = specifiedCustomColors.onInputBackground,
    focusedPlaceholderColor = specifiedCustomColors.onInputBackground.copy(.6f),
    focusedSupportingTextColor = specifiedCustomColors.onInputBackground,
    focusedTrailingIconColor = specifiedCustomColors.onInputBackground,
    focusedLeadingIconColor = specifiedCustomColors.onInputBackground,

    unfocusedContainerColor = specifiedCustomColors.barSecondary,
    unfocusedTextColor = specifiedCustomColors.onInputBackground,
    unfocusedLabelColor = specifiedCustomColors.onInputBackground.copy(.6f),
    unfocusedIndicatorColor = specifiedCustomColors.onInputBackground.copy(.6f),
    unfocusedPrefixColor = specifiedCustomColors.onInputBackground,
    unfocusedSuffixColor = specifiedCustomColors.onInputBackground,
    unfocusedPlaceholderColor = specifiedCustomColors.onInputBackground.copy(.6f),
    unfocusedSupportingTextColor = specifiedCustomColors.onInputBackground,
    unfocusedTrailingIconColor = specifiedCustomColors.onInputBackground,
    unfocusedLeadingIconColor = specifiedCustomColors.onInputBackground,

    disabledContainerColor = specifiedCustomColors.disabledBackground,
    disabledTextColor = specifiedCustomColors.disabled,
    disabledLabelColor = specifiedCustomColors.disabled.copy(.6f),
    disabledIndicatorColor = specifiedCustomColors.disabled.copy(.6f),
    disabledPrefixColor = specifiedCustomColors.disabled,
    disabledSuffixColor = specifiedCustomColors.disabled,
    disabledPlaceholderColor = specifiedCustomColors.disabled.copy(.6f),
    disabledSupportingTextColor = specifiedCustomColors.disabled,
    disabledTrailingIconColor = specifiedCustomColors.disabled,
    disabledLeadingIconColor = specifiedCustomColors.disabled,

    errorContainerColor = specifiedCustomColors.error,
    errorTextColor = specifiedCustomColors.onError,
    errorLabelColor = specifiedCustomColors.onError.copy(.6f),
    errorIndicatorColor = specifiedCustomColors.onError.copy(.6f),
    errorPrefixColor = specifiedCustomColors.onError,
    errorSuffixColor = specifiedCustomColors.onError,
    errorPlaceholderColor = specifiedCustomColors.onError.copy(.6f),
    errorSupportingTextColor = specifiedCustomColors.onError,
    errorTrailingIconColor = specifiedCustomColors.onError,
    errorLeadingIconColor = specifiedCustomColors.onError,

    textSelectionColors = TextSelectionColors(
        handleColor = Green,
        backgroundColor = Green.copy(.45f),
    ),
)

val AcceptButtonColors = ButtonColors(
    containerColor = Green,
    contentColor = Black,
    disabledContainerColor = GrayishGreen,
    disabledContentColor = specifiedCustomColors.disabled,
)

val CancelButtonColors = ButtonColors(
    containerColor = Red,
    contentColor = Black,
    disabledContainerColor = GrayishRed,
    disabledContentColor = specifiedCustomColors.disabled,
)

val DefaultButtonColors = ButtonColors(
    containerColor = Blue,
    contentColor = Black,
    disabledContainerColor = Blue.copy(.8f),
    disabledContentColor = specifiedCustomColors.disabled,
)

val CustomCardColors = CardColors(
    containerColor = specifiedCustomColors.surface,
    contentColor = specifiedCustomColors.onSurface,
    disabledContainerColor = specifiedCustomColors.disabledBackground,
    disabledContentColor = specifiedCustomColors.disabled,
)