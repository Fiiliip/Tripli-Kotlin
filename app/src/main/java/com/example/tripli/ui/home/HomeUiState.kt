package com.example.tripli.ui.home

import com.example.tripli.data.model.FilterData
import com.example.tripli.data.model.itinerary.Itinerary

data class HomeUiState(
    val isLoading: Boolean = false,
    val itineraries: List<Itinerary> = emptyList(),
    val filters: FilterData? = null
)