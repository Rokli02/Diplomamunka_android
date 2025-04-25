package me.uni.hiker.ui.screen.map.service

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import me.uni.hiker.db.entity.RecordedLocation
import me.uni.hiker.model.track.Track
import me.uni.hiker.model.track.AbstractTrack
import me.uni.hiker.model.track.ClusteredTrack
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

private const val EARTH_RADIUS_IN_METERS = 6371000.0
/** Bearing difference threshold in degree */
const val BEARING_DIFFERENCE_THRESHOLD = 3.0
/** Location closeness threshold in meter */
const val LOCATION_CLOSENESS_THRESHOLD = 4.99
/** Min travelled distance in meters */
private const val MIN_TRAVELLED_DISTANCE = 500.0
/** Min recorded locaitons*/
private const val MIN_RECORDED_LOCATIONS = 10

fun getLocationPermissions(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
}

/**
 * This function uses the "Haversine formula" to calculate distance between two locations
 */
fun getDistanceBetweenLocations(location1: Location, location2: Location): Double {
    val lat1Rad = Math.toRadians(location1.latitude)
    val lat2Rad = Math.toRadians(location2.latitude)
    val lon1Rad = Math.toRadians(location1.longitude)
    val lon2Rad = Math.toRadians(location2.longitude)

    val deltaLat = lat2Rad - lat1Rad
    val deltaLon = lon2Rad - lon1Rad

    val a = sin(deltaLat / 2).pow(2) + cos(lat1Rad) * cos(lat2Rad) * sin(deltaLon / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return EARTH_RADIUS_IN_METERS * c
}

/**
 * This function uses the "Haversine formula" to calculate distance between two locations
 */
fun getDistanceBetweenLocations(location1: RecordedLocation, location2: RecordedLocation): Double {
    val lat1Rad = Math.toRadians(location1.lat)
    val lat2Rad = Math.toRadians(location2.lat)
    val lon1Rad = Math.toRadians(location1.lon)
    val lon2Rad = Math.toRadians(location2.lon)

    val deltaLat = lat2Rad - lat1Rad
    val deltaLon = lon2Rad - lon1Rad

    val a = sin(deltaLat / 2).pow(2) + cos(lat1Rad) * cos(lat2Rad) * sin(deltaLon / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return EARTH_RADIUS_IN_METERS * c
}

/**
 * Calculates the bearing of line produced by to points..
 * @return Returns a value with type `Double` and is always between `0°` and `360°`
 */
fun calculateBearing(location1: Location, location2: Location): Double {
    val dLon = Math.toRadians(location2.longitude - location1.longitude)
    val loc1Lat = Math.toRadians(location1.latitude)
    val loc2Lat = Math.toRadians(location2.latitude)

    val y = sin(dLon) * cos(loc2Lat)
    val x = cos(loc1Lat) * sin(loc2Lat) -
            sin(loc1Lat) * cos(loc2Lat) * cos(dLon)

    return Math.toDegrees(atan2(y, x)).let {
        (it + 360) % 360
    }
}

@SuppressLint("MissingPermission")
fun createCurrentLocationFlow(
    context: Context,
    interval: Long = 1000,
    minUpdateInterval: Long = interval,
    maxUpdateInterval: Long = 1500L,
): Flow<Location> = callbackFlow {
    val locationProvider = LocationServices.getFusedLocationProviderClient(context)

    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, interval)
        .setWaitForAccurateLocation(false)
        .setMinUpdateIntervalMillis(minUpdateInterval)
        .setMaxUpdateDelayMillis(maxUpdateInterval)
        .build()

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult.lastLocation?.let {
                trySend(it)
            }
        }
    }

    locationProvider.requestLocationUpdates(
        locationRequest,
        locationCallback,
        Looper.getMainLooper()
    )

    awaitClose {
        locationProvider.removeLocationUpdates(locationCallback)
    }
}

fun currentLocationListener(
    context: Context,
    onAction: (Location?) -> Unit
) {
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) { return }

    LocationServices.getFusedLocationProviderClient(context).getCurrentLocation(
        Priority.PRIORITY_HIGH_ACCURACY,
        null
    ).addOnSuccessListener {
        onAction(it)
    }
}

fun isGPSEnabledFlow(context: Context): Flow<Boolean> = flow {
    val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    while (currentCoroutineContext().isActive) {
        emit(manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        delay(5000)
    }
}

@Composable
fun rememberGPSEnabled(locationPermissionGranted: Boolean = true): Boolean {
    var isGpsEnabled by remember { mutableStateOf(true) }
    val gpsStatusFlow = isGPSEnabledFlow(LocalContext.current)

    LaunchedEffect(locationPermissionGranted) {
        if (locationPermissionGranted) {
            gpsStatusFlow.collect {
                if (isGpsEnabled != it) isGpsEnabled = it
            }
        }
    }

    return isGpsEnabled
}

@Composable
fun rememberLocationPermissionAndRequest(onPermissionDenied: () -> Unit = {}): Boolean {
    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(getLocationPermissions(context)) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            hasPermission = when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    true
                }

                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    true
                }

                else -> {
                    onPermissionDenied()

                    false
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    return hasPermission
}

fun areRecordedPointsValid(locations: List<RecordedLocation>): Boolean {
    if (locations.size > MIN_RECORDED_LOCATIONS) return true

    var travelledDistance = 0.0
    for (index in 0..locations.size - 2) {
        travelledDistance += getDistanceBetweenLocations(locations[index], locations[index + 1])

        if (travelledDistance > MIN_TRAVELLED_DISTANCE) return true
    }

    return false
}

fun clusterTracks(tracks: List<Track>, clusterDistanceThreshold: Double, clusteredTracks: MutableList<AbstractTrack> = mutableListOf()): List<AbstractTrack> {
    if (tracks.isEmpty()) return clusteredTracks

    tracks.forEach { track ->
        clusteredTracks.forEachIndexed { i, clusteredTrack ->
            if (distanceOfPoints(track, clusteredTrack) < clusterDistanceThreshold) {
                when (clusteredTrack) {
                    is Track -> {
                        clusteredTracks[i] = ClusteredTrack.fromTracks(track, clusteredTrack)
                    }
                    is ClusteredTrack -> {
                        val weight = clusteredTrack.size

                        clusteredTrack.size++
                        clusteredTrack.lat = (clusteredTrack.lat * weight + track.lat) / clusteredTrack.size
                        clusteredTrack.lon = (clusteredTrack.lon * weight + track.lon) / clusteredTrack.size
                    }
                }

                return@forEach
            }
        }

        clusteredTracks.add(track)
    }


    return clusteredTracks
}

private fun distanceOfPoints(point1: AbstractTrack, point2: AbstractTrack): Double {
    return sqrt((point2.lat - point1.lat).pow(2) + (point2.lon - point1.lon).pow(2))
}