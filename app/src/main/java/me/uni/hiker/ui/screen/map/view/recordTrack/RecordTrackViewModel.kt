package me.uni.hiker.ui.screen.map.view.recordTrack

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.uni.hiker.db.dao.RecordedLocationDAO
import me.uni.hiker.db.entity.RecordedLocation
import me.uni.hiker.model.point.Point
import me.uni.hiker.ui.screen.map.service.LocationForegroundService
import javax.inject.Inject

@HiltViewModel
class RecordTrackViewModel @Inject constructor(
    private val recordedLocationDAO: RecordedLocationDAO,
) : ViewModel() {
    val recordedPoints = mutableStateListOf<Point>()
    private lateinit var recordedLocationFlow: Flow<RecordedLocation?>

    init {
        Log.d("LocationForegroundService", "View model reinit")
        viewModelScope.launch {
            recordedPoints.addAll(recordedLocationDAO.getAll().map(Point::fromEntity))
            recordedLocationFlow = recordedLocationDAO.getLastInsertedFlow()

            recordedLocationFlow.collect{
                if (it == null) return@collect
                if (recordedPoints.size != 0 && recordedPoints.last().id == it.id) return@collect

                Log.d("LocationForegroundService", "RecordTrackViewModel got $it")
                recordedPoints.add(Point.fromEntity(it))
            }
        }

    }

    fun startLocationService(context: Context) {
        viewModelScope.launch {
            recordedPoints.clear()

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