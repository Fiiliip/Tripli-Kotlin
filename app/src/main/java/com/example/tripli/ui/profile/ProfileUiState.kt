package com.example.tripli.ui.profile

import com.example.tripli.data.model.User
import com.example.tripli.data.model.itinerary.Itinerary

data class ProfileUiState(
    val user: User = User(),
    val itineraries: List<Itinerary> = emptyList(),
    val isLoading: Boolean = false,
    val usernameChanged: Boolean = false,
)
