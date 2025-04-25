package me.uni.hiker.ui.screen.main.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.uni.hiker.api.service.TrackService
import me.uni.hiker.db.dao.TrackDAO
import me.uni.hiker.model.Profile
import me.uni.hiker.model.track.Track
import javax.inject.Inject

const val ELEMENTS_TO_LOAD = 3

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val trackDAO: TrackDAO,
    private val trackService: TrackService,
    private val profile: Profile,
): ViewModel() {
    private val _localTracksFlow = MutableStateFlow<List<Track>?>(null)
    val localTracksFlow = _localTracksFlow.asStateFlow()
    private val _sharedTracksFlow = MutableStateFlow<List<Track>?>(null)
    val sharedTracksFlow = _sharedTracksFlow.asStateFlow()

    suspend fun loadLocalTracks() {
        _localTracksFlow.update {
            trackDAO.findByRandomOrderFirstX(ELEMENTS_TO_LOAD, profile.user?.remoteId).map(Track::fromEntity)
        }
    }

    suspend fun loadSharedTracks() {
        val sharedTracks = trackService.getAll(page = 0, pageSize = ELEMENTS_TO_LOAD, sort = "id:-1").let {
            if (!it.isSuccessful)
                return@let listOf<Track>()

            it.body()?.data?.map(Track::fromRemoteTrack) ?: listOf()
        }

        _sharedTracksFlow.update { sharedTracks }
    }
}