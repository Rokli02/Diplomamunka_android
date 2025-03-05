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

fun isGPSEnabledFlow(context: Context): Flow<Boolean> = flow {
    val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    while (currentCoroutineContext().isActive) {
        emit(manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        delay(5000)
    }
}

@Composable
fun rememberGPSEnabled(locationPermissionGranted: Boolean = true): Boolean {
    var isGpsEnabled by remember { mutableStateOf(false) }
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