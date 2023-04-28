package com.fiiliip.tripli

import android.media.Image

data class ItineraryItem(
    val imageUri: String,
    val title: String,
    val authorNickname: String,
    val price: Double,
    val rating: Float
)