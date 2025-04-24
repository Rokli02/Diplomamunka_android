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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.uni.hiker.api.service.TrackService
import me.uni.hiker.model.track.Track
import me.uni.hiker.service.ConnectionService
import me.uni.hiker.ui.screen.main.localtrack.DEBOUNCE_TIME
import me.uni.hiker.utils.debounce
import javax.inject.Inject

@HiltViewModel
class SharedTrackViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val trackService: TrackService,
): ViewModel() {
    private val _filterFlow = MutableStateFlow("")
    private var isLoading by mutableStateOf(false)
    private val onlineObserverFlow = ConnectionService.getOnlineObserverFlow(context)
    private var debounceJob: Job? = null

    var isOnline by mutableStateOf(false)
    val trackList = mutableStateListOf<Track>()
    var totalCount by mutableIntStateOf(PAGINATION_PAGE_SIZE)
    val filterFlow = _filterFlow.asStateFlow()

    init {
        viewModelScope.launch {
            onlineObserverFlow.collect { isOnline = it }
        }

        viewModelScope.launch {
            appendTrackList()
        }
    }

    fun onFilterChange(filter: String) {
        _filterFlow.update { filter }

        debounceJob?.cancel()

        debounceJob = debounce(DEBOUNCE_TIME, viewModelScope) {
            if (!isLoading) {
                val (remoteTracks, remoteTotalCount) = getTrackList()

                trackList.clear()
                trackList.addAll(remoteTracks)
                totalCount = remoteTotalCount
            }
        }
    }

    suspend fun appendTrackList() {
        if (isLoading || totalCount == trackList.size) return

        val (remoteTracks, remoteTotalCount) = getTrackList()

        trackList.addAll(remoteTracks)
        totalCount = remoteTotalCount
    }

    private suspend fun getTrackList(): Pair<List<Track>, Int> {
        try {
            isLoading = true

            val result = trackService.getAll(trackList.size / PAGINATION_PAGE_SIZE, PAGINATION_PAGE_SIZE, filterFlow.value)

            if (result.isSuccessful) {
                result.body()?.also { trackPage ->
                    return Pair(trackPage.data.map(Track::fromRemoteTrack), trackPage.totalCount)
                }
            }

            return Pair(listOf(), 0)
        } catch (exc: Exception) {
            Log.e("STVM", exc.message ?: "Unknown error inside the \"SharedTrackViewModel\"")

            return Pair(listOf(), 0)
        } finally {
            isLoading = false
        }

    }

    companion object {
        private const val PAGINATION_PAGE_SIZE = 10
    }
}