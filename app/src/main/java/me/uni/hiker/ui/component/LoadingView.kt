package me.uni.hiker.ui.component

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.uni.hiker.ui.theme.HikeRTheme

@Composable
fun Loading(
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = .33f)),
    iconModifier: Modifier = Modifier,
) {
    val rotate = rememberInfiniteTransition("infinite loading animation").run {
        animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(tween(
                durationMillis = 800,
                easing = LinearEasing,
            )),
            label = "infinite loading animation float"
        )
    }

    Box(
        modifier = modifier.clickable {},
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = iconModifier
                .fillMaxSize(.2f)
                .defaultMinSize(24.dp)
                .rotate(rotate.value),
            imageVector = Icons.Default.Refresh,
            contentDescription = "Loading Circle",
        )
    }
}

@Preview
@Composable
private fun LoadingPreview() {
    HikeRTheme {
        Loading()
    }
}