package me.uni.hiker.ui.screen.main.sharedtrack

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import me.uni.hiker.model.track.Track
import me.uni.hiker.service.ConnectionService
import javax.inject.Inject

@HiltViewModel
class SharedTrackViewModel @Inject constructor(
    @ApplicationContext context: Context,
): ViewModel() {
    var isOnline by mutableStateOf(false)
    val trackList = mutableStateListOf<Track>()

    private val onlineObserverFlow = ConnectionService.getOnlineObserverFlow(context)

    init {
        viewModelScope.launch {
            onlineObserverFlow.collect { isOnline = it }
        }
    }
}