package com.example.tripli.ui.itinerary

import com.example.tripli.data.model.itinerary.Itinerary

data class ItineraryUiState(
    val isLoading: Boolean = false,
    val itinerary: Itinerary = Itinerary(),
    val showMap: Boolean = true,
    val isEditMode: Boolean = false,
    val showEditMode: Boolean = false,
    val isNewItinerary: Boolean = false,
    val selectedDay: Int = 0,
    val dayOptions: List<Pair<Int, String>> = listOf(0 to "All days", 1 to "Day 1"),
)

