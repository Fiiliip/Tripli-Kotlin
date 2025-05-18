package com.example.tripli.data.model.itinerary

data class Place(
    val id: String = "",
    val google_place_id: String? = id,
    val title: String = "",
    val description: String? = null,
    val location: LatLng = LatLng(),
    val latitude: Double = location.latitude,
    val longitude: Double = location.longitude,
    val addressComponents: List<AddressComponent> = emptyList(),
    var day: Int = 0,
    val order: Int = 0,
    val images: List<Image>? = emptyList(),
    val additionalInformations: List<AdditionalInformation> = emptyList(),
)