package com.example.tripli.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val screen: String,
    val icon: ImageVector,
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home.route, Icons.Default.Home),
    BottomNavItem(Screen.Itinerary.createRoute(0), Icons.Default.List),
    BottomNavItem(Screen.Profile.route, Icons.Default.Person)
)

