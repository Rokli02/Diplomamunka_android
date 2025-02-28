package me.uni.hiker.ui.screen.map.view.recordTrack

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.uni.hiker.ui.screen.map.service.LocationForegroundService

class RecordTrackViewModel : ViewModel() {
    fun startLocationService(context: Context) {
        viewModelScope.launch {
            val intent = Intent(context, LocationForegroundService::class.java).apply {
                action = LocationForegroundService.ACTION_START
            }
            context.startForegroundService(intent)
        }
    }

    fun stopLocationService(context: Context) {
        viewModelScope.launch {
            val intent = Intent(context, LocationForegroundService::class.java).apply {
                action = LocationForegroundService.ACTION_STOP
            }
            context.startForegroundService(intent)
        }
    }
}