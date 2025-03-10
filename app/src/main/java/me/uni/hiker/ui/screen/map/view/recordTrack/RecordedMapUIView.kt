package me.uni.hiker.ui.screen.map.view.recordTrack

import android.content.Context
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .align(Alignment.BottomStart)
            .padding(start = 12.dp, bottom = 32.dp)
    ) {
        if (isRecording) {
            Button(
                onClick = {
                    stopLocationService(context)
                },
            ) {
                Text(text = "Stop Notification")
            }
        } else if (hasRecordedTrack) {
            Button(onClick = saveRecordedTrack) {
                Text(text = "Save Recorded Track")
            }
            Button(onClick = dropRecordedTrack) {
                Text(text = "Drop Recorded Track")
            }
        } else if (isGPSEnabled) {
            Button(
                onClick = {
                    startLocationService(context)
                },
            ) {
                Text(text = "Start Notification")
            }
        }
    }
}