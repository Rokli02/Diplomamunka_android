package me.uni.hiker.ui.screen.map.model

import android.location.Location
import android.util.Log
import me.uni.hiker.ui.screen.map.service.BEARING_DIFFERENCE_THRESHOLD
import me.uni.hiker.ui.screen.map.service.LOCATION_CLOSENESS_THRESHOLD
import me.uni.hiker.ui.screen.map.service.calculateBearing
import me.uni.hiker.ui.screen.map.service.getDistanceBetweenLocations
import kotlin.math.abs

data class LastLocation(
    /** Stores up to 3 locations.
     * The one on index 2 must be null or our current location, while on index 0 we have the last saved location.
     * */
    private val locations: MutableList<Location> = mutableListOf(),
    /** Bearing that connects to the last saved location */
    private var bearingOfLastSavedLocation: Double? = null,
) {
    private fun movedEnoughDistance(): Boolean {
        val distanceBetweenLocations = getDistanceBetweenLocations(locations[0], locations[2])

        Log.d("LocationForegroundService", "Distance between locations is $distanceBetweenLocations")

        return distanceBetweenLocations > LOCATION_CLOSENESS_THRESHOLD
    }

    private fun getBearingOrNull(): Double? {
        if (bearingOfLastSavedLocation == null) {
            return calculateBearing(locations[0], locations[1])
        }

        val bearingOfCurrentLocation = calculateBearing(locations[1], locations[2])
        var bearingDifference = abs(bearingOfCurrentLocation - bearingOfLastSavedLocation!!)

        if (bearingDifference > BEARING_DIFFERENCE_THRESHOLD) {
            return bearingOfCurrentLocation
        }

        val bearingOfPreviousLocation = calculateBearing(locations[0], locations[1])
        bearingDifference = abs(bearingOfPreviousLocation - bearingOfCurrentLocation)

        if (bearingDifference > BEARING_DIFFERENCE_THRESHOLD) {
            return bearingOfPreviousLocation
        }

        return null
    }

    private fun shiftLocations() {
        locations[1] = locations.removeAt(2)
    }

    fun addCurrentLocation(current: Location) {
        if (locations.size < 3) {
            locations.add(current)
        } else {
            locations[2] = current
        }
    }

    fun locationToSave(): Location? {
        // If there is only 1 location, tracking just begun and we need to save the starting location
        if (locations.size == 1) {
            return locations[0]
        }

        // If there is no 3 locations, keep collecting them
        if (locations.size != 3) return null

        // Didn't move enough to save the location
        if (!movedEnoughDistance()) {
            shiftLocations()

            return null
        }

        // If "bearing" is null, the direction hasn't changed, we don't need to save until there is a value
        val bearing = getBearingOrNull()
        Log.d("LocationForegroundService", "Current bearing is $bearing")
        if (bearing == null) {
            shiftLocations()

            return null
        }

        bearingOfLastSavedLocation = bearing
        locations.removeAt(0)

        return locations[0]

    }
}
