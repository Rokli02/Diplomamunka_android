package me.uni.hiker.ui.screen.map.view.trackDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.uni.hiker.ui.theme.AppTheme
import me.uni.hiker.ui.theme.CustomIconButtonColors
import me.uni.hiker.ui.theme.HikeRTheme

@Composable
fun TrackDetailsUIView(
    isRotated: Boolean = false,
    goBack: () -> Unit,
    zoomToTrack: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val goBackButtonModifier = remember (isRotated) {
            if (isRotated) {
                Modifier.align(Alignment.TopStart).offset(x = 46.dp, y = 5.dp)
            } else {
                Modifier.align(Alignment.TopStart)
            }
        }

        IconButton(
            modifier = goBackButtonModifier,
            onClick = goBack,
            colors = CustomIconButtonColors,
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "goBack")
        }

        Icon(
            imageVector = Icons.Default.PinDrop,
            contentDescription = "ZoomToTrack",
            tint = AppTheme.colors.onBackground.copy(.65f),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = -(12.dp), y = 58.dp)
                .size(38.dp)
                .border(1.dp, Color.DarkGray.copy(.25f), shape = RoundedCornerShape(4.dp))
                .clip(RoundedCornerShape(4.dp))
                .clickable(
                    onClick = zoomToTrack,
                    enabled = true,
                    role = Role.Button,
                    interactionSource = null,
                    indication = ripple(color = Color.Black)
                )
                .background(color = Color.White.copy(.74f))
                .padding(5.dp)
            ,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TrackDetailsUIViewPreview() {
    HikeRTheme {
        TrackDetailsUIView(
            isRotated = true,
            goBack = {},
            zoomToTrack = {},
        )
    }
}