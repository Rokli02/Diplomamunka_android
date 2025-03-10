package me.uni.hiker.ui.screen.map.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.uni.hiker.R
import me.uni.hiker.db.dao.RecordedLocationDAO
import me.uni.hiker.db.entity.RecordedLocation
import me.uni.hiker.ui.screen.Screen
import me.uni.hiker.ui.screen.map.model.LastLocation
import me.uni.hiker.ui.screen.map.model.MapViewType
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class LocationForegroundService : Service() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val lastLocation: LastLocation = LastLocation()
    @Inject lateinit var recordedLocationDAO: RecordedLocationDAO
    private lateinit var jobs: MutableList<Job>
    private var isGPSEnabled = false

    override fun onCreate() {
        super.onCreate()
        jobs = mutableListOf()
    }

    override fun onStartCommand(intent: Intent?, flags:Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }

        return START_NOT_STICKY
    }

    private fun start() {
        isRunning = true

        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
        startLocationUpdates()
    }

    private fun stop() {
        isRunning = false

        stopForeground(STOP_FOREGROUND_REMOVE)
        saveCurrentLocationAsDestination()
        stopLocationUpdates()
        stopSelf()
    }

    private fun startLocationUpdates() {
        val context = this

        if (!getLocationPermissions(this)) return

        serviceScope.launch {
            val isGPSEnabledFlow = flow {
                val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                while (currentCoroutineContext().isActive) {
                    emit(manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    delay(5000)
                }
            }

            isGPSEnabledFlow.collect {
                if (isGPSEnabled != it) {
                    isGPSEnabled = it

                    (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(
                        NOTIFICATION_ID,
                        if (isGPSEnabled)
                            createNotification()
                        else createNotification(
                            titleId = R.string.location_notification_disabled_gps_title,
                            textId = R.string.location_notification_disabled_gps_text,
                        ),
                    )

                }
            }
        }.also {
            jobs.add(it)
        }

        serviceScope.launch {
            createCurrentLocationFlow(
                context = context,
                interval = 1500L,
                minUpdateInterval = 1500L,
                maxUpdateInterval = 2500L
            ).collect {
                onLocationChanged(it)
            }
        }.also {
            jobs.add(it)
        }
    }

    private fun stopLocationUpdates() {
        jobs.forEach { it.cancel() }
    }

    private suspend fun onLocationChanged(location: Location) {
        lastLocation.addCurrentLocation(location)

        val locationToSave = lastLocation.locationToSave()
        if (locationToSave != null) {
            recordedLocationDAO.insertOne(RecordedLocation(
                lat = locationToSave.latitude,
                lon = locationToSave.longitude,
                createdAt = LocalDateTime.now(),
            ))
        }
    }

    private fun createNotification(
        titleId: Int = R.string.location_notification_title,
        textId: Int = R.string.location_notification_text,
    ): Notification {
        val channelId = createNotificationChannel()

        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            "${Screen.BASE_URI}/map/record".toUri()
        )

        val deepLinkPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(deepLinkIntent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(getString(titleId))
            .setContentText(getString(textId))
            .setSmallIcon(R.drawable.map_icon)
            .setContentIntent(deepLinkPendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel(): String {
        val channelId = "hiker_location_tracker"
        val chan = NotificationChannel(channelId, getString(R.string.location_notification_channel_name), NotificationManager.IMPORTANCE_LOW).apply {
            lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        }
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
            createNotificationChannel(chan)
        }
        return channelId
    }

    @SuppressLint("MissingPermission")
    private fun saveCurrentLocationAsDestination() {
        if (!getLocationPermissions(this)) return

        LocationServices.getFusedLocationProviderClient(this).getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener {
            if (it == null) return@addOnSuccessListener

            runBlocking {
                val canInsertDestination: Boolean = recordedLocationDAO.getLastInserted()?.let { recordedLocation ->
                    val targetLocation = Location("").apply {
                        latitude = recordedLocation.lat
                        longitude = recordedLocation.lon
                    }

                    getDistanceBetweenLocations(it, targetLocation) > LOCATION_CLOSENESS_THRESHOLD
                } ?: true

                if (canInsertDestination) {
                    recordedLocationDAO.insertOne(
                        RecordedLocation(
                            lat = it.latitude,
                            lon = it.longitude,
                            createdAt = LocalDateTime.now(),
                        )
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val NOTIFICATION_ID = 1
        var isRunning = false
    }
}