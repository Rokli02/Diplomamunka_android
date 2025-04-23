package me.uni.hiker.ui.screen.main.sharedtrack

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import me.uni.hiker.api.service.TrackService
import me.uni.hiker.model.track.Track
import me.uni.hiker.service.ConnectionService
import javax.inject.Inject

@HiltViewModel
class SharedTrackViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val trackService: TrackService,
): ViewModel() {
    var isOnline by mutableStateOf(false)
    val trackList = mutableStateListOf<Track>()

    private var totalCount by mutableIntStateOf(1)
    private val onlineObserverFlow = ConnectionService.getOnlineObserverFlow(context)

    init {
        viewModelScope.launch {
            onlineObserverFlow.collect { isOnline = it }
        }

        viewModelScope.launch {
            appendTrackList()
        }
    }

    private suspend fun appendTrackList() {
        if (totalCount <= trackList.size) return

        try {
            val result = trackService.getAll(trackList.size / PAGINATION_PAGE_SIZE, PAGINATION_PAGE_SIZE)

            if (result.isSuccessful) {
                result.body()?.also { trackPage ->
                    totalCount = trackPage.totalCount

                    trackList.addAll(trackPage.data.map(Track::fromRemoteTrack))
                }
            }
        } catch (exc: Exception) {
            Log.e("STVM", exc.message ?: "Unknown error inside the \"SharedTrackViewModel\"")

            totalCount = 0
        }
    }

    companion object {
        private const val PAGINATION_PAGE_SIZE = 10
    }
}