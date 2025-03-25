package me.uni.hiker.ui.screen.map.view.trackDetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import me.uni.hiker.ui.theme.CustomIconButtonColors
import me.uni.hiker.ui.theme.HikeRTheme

@Composable
fun TrackDetailsUIView(
    goBack: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        IconButton(
            modifier = Modifier.align(Alignment.TopStart),
            onClick = goBack,
            colors = CustomIconButtonColors,
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "goBack")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TrackDetailsUIViewPreview() {
    HikeRTheme {
        TrackDetailsUIView(
            goBack = {},
        )
    }
}