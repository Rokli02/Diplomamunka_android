package me.uni.hiker.ui.screen.map.view.recordTrack

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import me.uni.hiker.R
import me.uni.hiker.ui.provider.LocalSnackbarContext
import me.uni.hiker.ui.provider.SnackbarAction
import me.uni.hiker.ui.screen.map.service.rememberGPSEnabled
import me.uni.hiker.ui.screen.map.service.rememberLocationPermissionAndRequest


@Composable
fun BoxScope.RecordTrackScreen(
    goBack: () -> Unit,
    recordTrackViewModel: RecordTrackViewModel = hiltViewModel(),
) {
    val snackbar = LocalSnackbarContext
    val context = LocalContext.current
    val hasLocationPermission = rememberLocationPermissionAndRequest {
        snackbar.showSnackbar(
            message = "${context.getString(R.string.permission_denied)}\n${context.getString(R.string.please_grant_permission)}",
            duration = SnackbarDuration.Long,
            action = SnackbarAction(
                label = context.getString(R.string.grant),
                onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", "me.uni.hiker", null)
                    intent.setData(uri)
                    context.startActivity(intent)
                }
            ),
            withDismissAction = false,
        )
        goBack()
    }
    val isGPSEnabled = rememberGPSEnabled(hasLocationPermission)

    RecordTrackView(
        startRecording = { recordTrackViewModel.startLocationService(context) },
        stopRecording = { recordTrackViewModel.stopLocationService(context) },
        locations = recordTrackViewModel.recordedPoints,
        cameraPositionState = recordTrackViewModel.cameraPositionState,
        isRecording = recordTrackViewModel.isRecording,
        isGpsEnabled = isGPSEnabled && hasLocationPermission,
    )
}