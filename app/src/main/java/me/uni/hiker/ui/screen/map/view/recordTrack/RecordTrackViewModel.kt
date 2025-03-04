package me.uni.hiker.ui.screen.map.view.recordTrack

import android.content.Context
import android.content.Intent
import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import me.uni.hiker.db.dao.RecordedLocationDAO
import me.uni.hiker.db.entity.RecordedLocation
import me.uni.hiker.model.point.Point
import me.uni.hiker.ui.screen.map.service.LocationForegroundService
import me.uni.hiker.ui.screen.map.service.createCurrentLocationFlow
import me.uni.hiker.ui.screen.map.service.getLocationPermissions
import javax.inject.Inject

private val middleOfHungary = LatLng(47.48856, 19.04892)

@HiltViewModel
class RecordTrackViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val recordedLocationDAO: RecordedLocationDAO,
) : ViewModel() {
    var isRecording by mutableStateOf(LocationForegroundService.isRunning)
    val recordedPoints = mutableStateListOf<Point>()
    val cameraPositionState = CameraPositionState(CameraPosition.fromLatLngZoom(middleOfHungary, 14f))
    private val initJobs: MutableList<Job> = mutableListOf()
    private var currentLocationFlow: Flow<Location>? = null
    private var recordedPointsFlow: Flow<RecordedLocation?>? = null

    init {
        if (LocationForegroundService.isRunning) {
            isRecording = true

            startFlowCollection(false)
        } else {
            viewModelScope.launch {
                recordedPoints.addAll(
                    async { recordedLocationDAO.getAll() }.await().map(Point::fromEntity)
                )
            }
        }
    }

    fun startLocationService(context: Context) {
        if (LocationForegroundService.isRunning) return

        isRecording = true
        recordedPoints.clear()

        startFlowCollection()

        viewModelScope.launch {
            val intent = Intent(context, LocationForegroundService::class.java).apply {
                action = LocationForegroundService.ACTION_START
            }
            context.startForegroundService(intent)
        }
    }

    fun stopLocationService(context: Context) {
        if (!LocationForegroundService.isRunning) return

        recordedPointsFlow = null
        currentLocationFlow = null
        initJobs.forEach { it.cancel() }
        viewModelScope.launch {
            isRecording = false
            val intent = Intent(context, LocationForegroundService::class.java).apply {
                action = LocationForegroundService.ACTION_STOP
            }
            context.startForegroundService(intent)
        }
    }

    private fun startFlowCollection(pruneRecordedLocationTable: Boolean = true) {
        recordedPointsFlow = recordedLocationDAO.getLastInsertedFlow()
        viewModelScope.launch {
            if (pruneRecordedLocationTable) {
                async { recordedLocationDAO.prune() }.await()
            }
            recordedPoints.addAll(
                async { recordedLocationDAO.getAll() }.await().map(Point::fromEntity)
            )

            recordedPointsFlow!!.collect{
                if (it == null) return@collect
                if (recordedPoints.size != 0 && recordedPoints.last().id == it.id) return@collect

                recordedPoints.add(Point.fromEntity(it))
            }
        }.apply {
            initJobs.add(this)
        }

        if (getLocationPermissions(context)) {
            currentLocationFlow = createCurrentLocationFlow(context)

            viewModelScope.launch {
                currentLocationFlow!!.collect {
                    if (isRecording) {
                        cameraPositionState.move(
                            CameraUpdateFactory.newLatLng(
                                LatLng(
                                    it.latitude,
                                    it.longitude
                                )
                            )
                        )
                    }
                }
            }.apply {
                initJobs.add(this)
            }
        }
    }

    fun saveRecordedTrack() {
        TODO("Implement")
    }

    fun dropRecordedTrack() {
        TODO("Implement")
    }

    //TODO: Felhasználni az UI-ban
    fun hasRecordedTrack(): Boolean {
        return recordedPoints.size != 0
    }

    override fun onCleared() {
        super.onCleared()

        initJobs.forEach { it.cancel() }
    }
}