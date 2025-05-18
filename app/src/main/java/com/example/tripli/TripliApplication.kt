package com.example.tripli

import android.app.Application
import com.example.tripli.di.AppContainer
import com.example.tripli.di.StringProvider
import com.google.android.libraries.places.api.Places

class TripliApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer()

        StringProvider.init(this)

        if (!Places.isInitialized()) {
            Places.initializeWithNewPlacesApiEnabled(applicationContext, getString(R.string.google_api_key))
        }
    }
}