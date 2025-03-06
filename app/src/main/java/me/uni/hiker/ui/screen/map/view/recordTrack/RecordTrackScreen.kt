package me.uni.hiker.ui.screen.map.view.recordTrack

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.uni.hiker.R
import me.uni.hiker.exception.InvalidDataException
import me.uni.hiker.ui.component.Loading
import me.uni.hiker.ui.provider.LocalSnackbarContext
import me.uni.hiker.ui.provider.SnackbarAction
import me.uni.hiker.ui.screen.map.service.rememberGPSEnabled
import me.uni.hiker.ui.screen.map.service.rememberLocationPermissionAndRequest
import me.uni.hiker.ui.theme.AcceptButtonColors
import me.uni.hiker.ui.theme.AppTheme
import me.uni.hiker.ui.theme.CancelButtonColors


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
            dismissAction = null,
        )
        goBack()
    }
    val isGPSEnabled = rememberGPSEnabled(hasLocationPermission)
    val coroutineScope = rememberCoroutineScope()
    var isSaveModalOpen by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    if (!isGPSEnabled) {
        AlertDialog(
            onDismissRequest = goBack,
            title = { Text(text = context.getString(R.string.turn_on_gps)) },
            text = { Text(text = context.getString(R.string.turn_on_gps_text)) },
            containerColor = AppTheme.colors.surface,
            textContentColor = AppTheme.colors.onSurface,
            titleContentColor = AppTheme.colors.onSurface,
            iconContentColor = AppTheme.colors.onSurface,
            confirmButton = {
                Button(
                    onClick = {
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        context.startActivity(intent)
                    },
                    colors = AcceptButtonColors,
                ) {
                    Text(text = context.getString(R.string.grant))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        recordTrackViewModel.stopLocationService(context)

                        goBack()
                    },
                    colors = CancelButtonColors,
                ) {
                    Text(text = context.getString(R.string.cancel))
                }
            }
        )
    }

    RecordMapView(
        locations = recordTrackViewModel.recordedPoints,
        cameraPositionState = recordTrackViewModel.cameraPositionState,
        isRecording = recordTrackViewModel.isRecording,
        isGpsEnabled = isGPSEnabled && hasLocationPermission,
    )

    RecordedMapUIView(
        hasRecordedTrack = recordTrackViewModel.hasRecordedTrack(),
        isRecording = recordTrackViewModel.isRecording,
        isGPSEnabled = isGPSEnabled,
        stopLocationService = {
            recordTrackViewModel.stopLocationService(it)

            isLoading = true
            coroutineScope.launch {
                delay(1000)

                isSaveModalOpen = true
                isLoading = false
            }
        },
        startLocationService = recordTrackViewModel::startLocationService,
        saveRecordedTrack = {
            isSaveModalOpen = true
        },
        dropRecordedTrack = {
            coroutineScope.launch { recordTrackViewModel.dropRecordedTrack() }
        }
    )

    if (isLoading) {
        Loading()
    }

    if (isSaveModalOpen) {
        SaveNewTrackModal(
            onDismissRequest = { isSaveModalOpen = false },
            onSave = {
                coroutineScope.launch {
                    try {
                        recordTrackViewModel.saveRecordedTrack(it)
                        recordTrackViewModel.dropRecordedTrack()
                    } catch(exc: InvalidDataException) {
                        snackbar.showSnackbar(
                            exc.message ?: context.getString(R.string.unknown_exception),
                        )
                    } catch (exc: RuntimeException) {
                        Log.e("Track Save", exc.message ?: "Error occurred when tried to save a track")

                        snackbar.showSnackbar(
                            context.getString(R.string.unknown_exception),
                        )
                    } finally {
                        isSaveModalOpen = false
                    }
                }
            }
        )
    }
}