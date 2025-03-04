package me.uni.hiker.ui.screen.map.view.recordTrack

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import me.uni.hiker.ui.screen.map.service.isCurrentLocationEnabled

@Composable
fun BoxScope.RecordTrackScreen(recordTrackViewModel: RecordTrackViewModel = hiltViewModel()) {
    val context = LocalContext.current

    val isGpsEnabled = isCurrentLocationEnabled()

    RecordTrackView(
        startRecording = { recordTrackViewModel.startLocationService(context) },
        stopRecording = { recordTrackViewModel.stopLocationService(context) },
        locations = recordTrackViewModel.recordedPoints,
        cameraPositionState = recordTrackViewModel.cameraPositionState,
        isRecording = recordTrackViewModel.isRecording,
        isGpsEnabled = isGpsEnabled,
    )
}