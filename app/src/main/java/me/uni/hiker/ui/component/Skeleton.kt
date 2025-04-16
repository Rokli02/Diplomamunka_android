package me.uni.hiker.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.uni.hiker.ui.theme.HikeRTheme

@Composable
fun Skeleton(
    modifier: Modifier = Modifier,
    durationMillis: Int = 2000,
) {
    val transition = rememberInfiniteTransition(label = "")

    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "skeleton_animation",
    )

    Box(modifier = modifier.fillMaxWidth().fillMaxHeight().background(brush = Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 0.15f),
            Color.LightGray.copy(alpha = 0.5f),
            Color.LightGray.copy(alpha = 0.15f),
        ),
        start = Offset(x = translateAnimation - 150, y = translateAnimation - 250),
        end = Offset(x = translateAnimation, y = translateAnimation),
    )))
}

@Preview
@Composable
private fun SkeletonPreview() {
    HikeRTheme {
        Skeleton(modifier = Modifier.width(200.dp).height(160.dp))
    }
}