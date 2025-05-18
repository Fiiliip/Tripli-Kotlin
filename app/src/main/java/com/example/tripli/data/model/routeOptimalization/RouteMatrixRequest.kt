package com.example.tripli.data.model.routeOptimalization

data class RouteMatrixRequest(
    val origins: List<Waypoint>,
    val destinations: List<Waypoint>,
    val startingPlaceId: String,
    val endingPlaceId: String,
    val travelMode: String
)


