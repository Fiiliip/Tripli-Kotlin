package com.fiiliip.tripli

import com.google.gson.annotations.SerializedName

data class ItineraryItem(
    val image: String,
    val title: String,
    @field:SerializedName("author_nickname")
    val authorNickname: String,
    val price: Double,
    val rating: Float
)