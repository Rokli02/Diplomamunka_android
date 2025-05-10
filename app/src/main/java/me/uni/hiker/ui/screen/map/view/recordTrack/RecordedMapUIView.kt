package me.uni.hiker.ui.screen.map.view.recordTrack

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.uni.hiker.R
import me.uni.hiker.ui.theme.AcceptButtonColors
import me.uni.hiker.ui.theme.CancelButtonColors
import me.uni.hiker.ui.theme.DefaultButtonColors
import me.uni.hiker.ui.theme.HikeRTheme

@Composable
fun BoxScope.RecordedMapUIView(
    hasRecordedTrack: Boolean,
    isRecording: Boolean,
    isGPSEnabled: Boolean,
    stopLocationService: (Context) -> Unit,
    startLocationService: (Context) -> Unit,
    saveRecordedTrack: () -> Unit,
    dropRecordedTrack: () -> Unit,
) {
    val context = LocalContext.current

    val buttonShape = remember { RoundedCornerShape(12.dp) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .align(Alignment.BottomStart)
            .padding(start = 12.dp, bottom = 32.dp)
    ) {
        when {
            isRecording -> {
                Button(
                    onClick = {
                        stopLocationService(context)
                    },
                    shape = buttonShape,
                    colors = AcceptButtonColors,
                ) {
                    Icon(imageVector = Icons.Default.Stop, contentDescription = null)
                    Text(text = context.getString(R.string.notification_stop))
                }
            }
            hasRecordedTrack -> {
                Button(
                    onClick = {
                        startLocationService(context)
                    },
                    shape = buttonShape,
                    colors = AcceptButtonColors,
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                    Text(text = context.getString(R.string.notification_resume))
                }
                Button(
                    onClick = saveRecordedTrack,
                    shape = buttonShape,
                    colors = DefaultButtonColors,
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = null)
                    Text(text = context.getString(R.string.notification_save))
                }
                Button(
                    onClick = dropRecordedTrack,
                    shape = buttonShape,
                    colors = CancelButtonColors,
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    Text(text = context.getString(R.string.notification_drop))
                }
            }
            isGPSEnabled -> {
                Button(
                    onClick = {
                        startLocationService(context)
                    },
                    shape = buttonShape,
                    colors = AcceptButtonColors,
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                    Text(text = context.getString(R.string.notification_start))
                }
            }
        }
    }
}

@Preview
@Composable
private fun RecordedMapUIViewPreview() {
    HikeRTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            RecordedMapUIView(
                hasRecordedTrack = true,
                isRecording = false,
                isGPSEnabled = true,
                stopLocationService = {},
                startLocationService = {},
                saveRecordedTrack = {},
                dropRecordedTrack = {},
            )
        }
    }
}