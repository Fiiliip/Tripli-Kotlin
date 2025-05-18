package com.example.tripli.data.model.itinerary

import com.example.tripli.data.model.User

data class Itinerary(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val owner: User? = null,
    val category: Int = 0,
    val category_id: Int = 0,
    val places: List<Place> = emptyList(),
    val additionalInformations: List<AdditionalInformation> = emptyList(),
    val rating: Float = 0f,
)
