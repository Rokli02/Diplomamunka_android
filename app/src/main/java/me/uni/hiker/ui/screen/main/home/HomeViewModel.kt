package me.uni.hiker.ui.screen.main.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.uni.hiker.db.dao.TrackDAO
import me.uni.hiker.model.track.Track
import me.uni.hiker.model.user.User
import javax.inject.Inject

const val ELEMENTS_TO_LOAD = 3

@HiltViewModel
class HomeViewModel @Inject constructor(
    val trackDAO: TrackDAO,
): ViewModel() {
    private val _localTracksFlow = MutableStateFlow<List<Track>?>(null)
    val localTracksFlow = _localTracksFlow.asStateFlow()
    var user: User? = null

    suspend fun loadLocalTracks() {
        _localTracksFlow.update {
            trackDAO.findByRandomOrderFirstX(ELEMENTS_TO_LOAD, user?.id).map(Track::fromEntity)
        }
    }
}