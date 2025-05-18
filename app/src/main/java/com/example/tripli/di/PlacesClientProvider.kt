package com.example.tripli.di

import android.content.Context
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient

object PlacesClientProvider {

    @Volatile
    private var client: PlacesClient? = null

    fun getInstance(context: Context): PlacesClient {
        return client ?: synchronized(this) {
            client ?: createClient(context.applicationContext).also { client = it }
        }
    }

    private fun createClient(context: Context): PlacesClient {
        return Places.createClient(context)
    }
}
