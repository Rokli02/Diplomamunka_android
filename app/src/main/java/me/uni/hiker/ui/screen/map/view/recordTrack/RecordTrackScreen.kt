package me.uni.hiker.ui.screen.map.view.recordTrack

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RecordTrackScreen(recordTrackViewModel: RecordTrackViewModel = hiltViewModel()) {
    val context = LocalContext.current

    RecordTrackView(
        startRecording = { recordTrackViewModel.startLocationService(context) },
        stopRecording = { recordTrackViewModel.stopLocationService(context) },
        locations = recordTrackViewModel.recordedPoints,
    )
}