package me.uni.hiker.ui.screen.map.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
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
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var lastLocation: LastLocation? = null

    @Inject lateinit var recordedLocationDAO: RecordedLocationDAO

    override fun onCreate() {
        super.onCreate()
        locationClient = LocationServices.getFusedLocationProviderClient(this)
        serviceScope.launch {
            recordedLocationDAO.prune()
        }
        // TODO: Check is GPS tracker enabled
    }

    override fun onStartCommand(intent: Intent?, flags:Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }

        return START_NOT_STICKY
    }

    private fun start() {
        lastLocation = LastLocation()

        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
        startLocationUpdates()
    }

    private fun stop() {
        if (lastLocation == null) return

        stopForeground(STOP_FOREGROUND_REMOVE)
        stopLocationUpdates()
        stopSelf()
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1500L)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(1500L)
            .setMaxUpdateDelayMillis(2500L)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.locations.let { locations ->
                    for (location in locations) {
                        Log.d("LocationForegroundService", "On Location Changed: $location")

                        onLocationChanged(location)
                    }
                }
            }
        }

        if (checkForLackOfLocationPermission(this)) {
            return
        }

        locationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        locationClient.removeLocationUpdates(locationCallback)
    }

    private fun onLocationChanged(location: Location) {
        lastLocation!!.addCurrentLocation(location)

        val locationToSave = lastLocation!!.locationToSave()
        if (locationToSave != null) {
            Log.d("LocationForegroundService", "Saving location $locationToSave")

            serviceScope.launch {
                recordedLocationDAO.insertOne(RecordedLocation(
                    lat = locationToSave.latitude,
                    lon = locationToSave.longitude,
                    createdAt = LocalDateTime.now(),
                ))
            }
        }
    }

    private fun createNotification(): Notification {
        val channelId = createNotificationChannel()

        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            "${Screen.BASE_URI}/map/${MapViewType.RECORD_TRACK.name}".toUri()
        )

        val deepLinkPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(deepLinkIntent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(getString(R.string.location_notification_title))
            .setContentText(getString(R.string.location_notification_text))
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

    override fun onDestroy() {
        super.onDestroy()

        // Save our current location to DB as a destination
        if (!checkForLackOfLocationPermission(this)) {
            locationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener {
                if (it != null) {
                    //TODO: Ellenőrzéseket berakni, hogy ha pl. túl közeli az utolsó mentett ponthoz, ne mentse

                    serviceScope.launch {
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

        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val NOTIFICATION_ID = 1
    }
}

fun checkForLackOfLocationPermission(context: Context): Boolean {
    return ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED
}