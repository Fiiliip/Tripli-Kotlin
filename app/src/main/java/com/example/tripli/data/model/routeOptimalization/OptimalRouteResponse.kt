package com.example.tripli.data.model.routeOptimalization

data class OptimalRouteResponse(
    val route: List<RoutePlace>,
    val distanceMeters: Int,
    val durationSeconds: Int
)
