package me.uni.hiker.ui.screen.main.localtrack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import me.uni.hiker.db.dao.TrackDAO
import me.uni.hiker.model.track.Track
import me.uni.hiker.model.user.User
import me.uni.hiker.utils.debounce
import javax.inject.Inject

const val DEBOUNCE_TIME = 600L

@HiltViewModel
class LocalTrackViewModel @Inject constructor(
    private val trackDAO: TrackDAO,
): ViewModel() {
    private val _filterFlow = MutableStateFlow("")
    private var _trackFlowState: MutableStateFlow<Flow<PagingData<Track>>> = MutableStateFlow(trackPagingFlow(_filterFlow.value))
    private var debounceJob: Job? = null

    var currentUser: User? = null
    val filterFlow = _filterFlow.asStateFlow()
    val trackFlowState = _trackFlowState.asStateFlow()

    fun onFilterChange(filter: String) {
        _filterFlow.update { filter }

        debounceJob?.cancel()

        debounceJob = debounce(DEBOUNCE_TIME, viewModelScope) {
            _trackFlowState.update { trackPagingFlow(filterFlow.value) }
        }
    }

    private fun trackPagingFlow(filter: String): Flow<PagingData<Track>> {
        return Pager(
            config = PagingConfig(2),
            initialKey = 1,
            pagingSourceFactory = { trackDAO.findByFilterPagingSource(filter.ifBlank { null }, currentUser?.id) }
        ).flow.map { data ->
            data.map(Track::fromEntity)
        }.cachedIn(viewModelScope)
    }
}