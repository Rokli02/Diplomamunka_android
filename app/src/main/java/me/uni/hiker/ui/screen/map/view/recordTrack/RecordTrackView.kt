package me.uni.hiker.ui.screen.map.view.recordTrack

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.uni.hiker.model.point.Point

// TODO: Amikor Polyline-al megjelenítjük a bejárt útvonalat a tényleges jelenlegi koordinátánkkal legyen összekötve a rögzített, így nem történik ugrás a felrajzolt "egyenesben"

@Composable
fun RecordTrackView(
    startRecording: () -> Unit,
    stopRecording: () -> Unit,
    locations: List<Point>,
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Blue)
    ) {
        Button(
            modifier = Modifier.align(Alignment.TopStart),
            onClick = startRecording,
        ) {
            Text(text = "Start Notification")
        }
        Button(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = stopRecording,
        ) {
            Text(text = "Stop Notification")
        }

        LazyColumn (
            modifier = Modifier.padding(top = 60.dp).fillMaxSize().background(Color.Magenta)
        ) {
          items(
              locations,
              key = { it.id }
          ) {
              Text(text = "${it.lat} - ${it.lon}")
          }
        }
    }
}