package com.example.tripli.utils

import com.example.tripli.data.model.itinerary.LatLng
import com.example.tripli.data.model.itinerary.Place
import com.example.tripli.data.model.routeOptimalization.RouteMatrixRequest
import com.example.tripli.data.model.routeOptimalization.Waypoint
import com.example.tripli.data.model.routeOptimalization.WaypointLocation
import com.example.tripli.data.model.routeOptimalization.WaypointLocationLatLng
import java.text.DecimalFormat
import java.util.Locale

object RouteOptimalizationUtils {

    fun formatForRouteMatrix(places: List<Place>): RouteMatrixRequest {
        val origins = mutableListOf<Waypoint>()
        val destinations = mutableListOf<Waypoint>()
        val startingPlaceId = places[0].id
        val endingPlaceId = places.last().id

        val length = if (startingPlaceId == endingPlaceId) places.size - 1 else places.size
        for (i in 0 until length) {
            val waypoint = Waypoint(
                id = places[i].id,
                waypoint = WaypointLocation(
                    location = WaypointLocationLatLng(
                        latLng = LatLng(places[i].location.latitude, places[i].location.longitude)
                    )
                )
            )
            origins.add(waypoint)
            destinations.add(waypoint)
        }

        return RouteMatrixRequest(
            origins = origins,
            destinations = destinations,
            startingPlaceId = startingPlaceId,
            endingPlaceId = endingPlaceId,
            travelMode = "DRIVE"
        )
    }

    fun formatForReadableDistance(distanceInMeters: Int): String {
        val decimalFormat = DecimalFormat("#.##")
        return if (distanceInMeters < 1000) {
            "$distanceInMeters m"
        } else {
            "${decimalFormat.format(distanceInMeters / 1000.0)} km"
        }
    }

    fun formatForReadableDuration(durationInSeconds: Int): String {
        return when {
            durationInSeconds < 60 -> "$durationInSeconds s"
            durationInSeconds < 3600 -> "${durationInSeconds / 60} min"
            durationInSeconds < 86400 -> String.format(Locale.US, "%.2f h", durationInSeconds / 3600.0)
            else -> String.format(Locale.US, "%.2f days", durationInSeconds / 86400.0)
        }
    }
}