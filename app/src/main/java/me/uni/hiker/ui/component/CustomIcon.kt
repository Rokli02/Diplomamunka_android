package me.uni.hiker.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Custom Icon displayer
 * @param [icon] Can be any of `ImageVector`, `Painter` or `null`.
 *  If unknown type or null was passed only a Spacer element gets displayed.
 * @param contentDescription text used by accessibility services to describe what this icon
 *   represents. This should always be provided unless this icon is used for decorative purposes,
 *   and does not represent a meaningful action that a user can take. This text should be localized,
 *   such as by using [androidx.compose.ui.res.stringResource] or similar
 * @param modifier the [Modifier] to be applied to this icon
 * @param tint tint to be applied to `painter` or `imageVector`. If [Color.Unspecified] is provided, then no tint is
 *   applied.
 */
@Composable
fun CustomIcon(
    modifier: Modifier = Modifier,
    icon: (Any)? = null,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current
) {
    if (icon == null) {
        Spacer(modifier = modifier)
    } else {
        when (icon) {
            is ImageVector -> {
                Icon(
                    modifier = modifier,
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = tint,
                )
            }
            is Painter -> {
                Icon(
                    modifier = modifier,
                    painter = icon,
                    contentDescription = contentDescription,
                    tint = tint,
                )
            }
            else -> {
                Spacer(modifier = modifier)
            }
        }
    }
}
