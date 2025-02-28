package me.uni.hiker.ui.screen.map.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
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
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

@Composable
fun isCurrentLocationEnabled(): Boolean {
    val context = LocalContext.current

    var hasPermission by remember { mutableStateOf(getLocationPermissions(context)) }
    var isGpsEnabled by remember { mutableStateOf(false) }
    val gpsStatusFlow = flow {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        while (currentCoroutineContext().isActive) {
            emit(manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            delay(3000)
        }
    }

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

        gpsStatusFlow.collect {
            if (isGpsEnabled != it) isGpsEnabled = it
        }
    }

    return hasPermission && isGpsEnabled
}

private fun getLocationPermissions(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
}