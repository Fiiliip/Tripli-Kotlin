package com.example.tripli.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Itinerary : Screen("itinerary/{id}") {
        fun createRoute(id: Int) = "itinerary/$id"
    }
    object Place : Screen("place_detail/{order}") {
        fun createRoute(order: Int) = "place_detail/$order"
    }
}