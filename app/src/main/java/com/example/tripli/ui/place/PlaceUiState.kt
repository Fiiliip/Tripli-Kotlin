package com.example.tripli.ui.place

import com.example.tripli.data.model.itinerary.Place

data class PlaceUiState(
    val place: Place = Place(),
    val showMap: Boolean = true,
    val isEditMode: Boolean = false,
    val showEditMode: Boolean = false,
)
