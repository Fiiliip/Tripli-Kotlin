package com.example.tripli.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import com.example.tripli.R
import com.example.tripli.data.model.itinerary.AdditionalInformation
import com.example.tripli.data.model.itinerary.AddressComponent
import com.example.tripli.data.model.itinerary.Image
import com.example.tripli.data.model.itinerary.LatLng
import com.example.tripli.data.model.itinerary.Place
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchResolvedPhotoUriRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import com.google.android.libraries.places.api.model.Place as GooglePlace

object GoogleUtils {

    suspend fun formatPlaceDetails(placesClient: PlacesClient, place: GooglePlace): Place = coroutineScope {
        val location = place.latLng?.let { LatLng(it.latitude, it.longitude) } ?: LatLng(0.0, 0.0)

        val addressComponents = place.addressComponents?.asList()?.mapNotNull { component ->
            component.shortName?.let {
                AddressComponent(
                    id = 0,
                    long_name = component.name,
                    short_name = it,
                    type = component.types.firstOrNull() ?: ""
                )
            }
        } ?: emptyList()

        val photos: List<Image>? = if (!place.photoMetadatas.isNullOrEmpty()) {
            supervisorScope {
                val deferredPhotos = place.photoMetadatas.take(3).mapIndexed { index, metadata ->
                    async {
                        try {
                            withTimeout(3000L) {
                                val response = placesClient.fetchResolvedPhotoUri(
                                    FetchResolvedPhotoUriRequest.builder(metadata)
                                        .setMaxWidth(400)
                                        .setMaxHeight(400)
                                        .build()
                                ).await()

                                response.uri?.toString()?.let { uri ->
                                    Image(url = uri, order = index + 1)
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("PhotoFetch", "Failed to fetch photo", e)
                            null
                        }
                    }
                }
                deferredPhotos.awaitAll().filterNotNull()
            }
        } else {
            place.latLng?.let {
                getStreetViewImage(it.latitude, it.longitude)
            }
        }

        val additionalInformations = mutableListOf<AdditionalInformation>()
        place.address?.let {
            additionalInformations.add(
                AdditionalInformation(
                    type = "address",
                    value = it,
                    order = 1
                )
            )
        }

        Place(
            id = place.id ?: "",
            title = place.name ?: "",
            description = "",
            location = location,
            addressComponents = addressComponents,
            day = 0,
            order = 0,
            images = photos,
            additionalInformations = additionalInformations
        )
    }


    suspend fun getPlaceDetails(placesClient: PlacesClient, placeId: String): GooglePlace = suspendCoroutine { cont ->
        val request = FetchPlaceRequest.builder(
            placeId,
            listOf(
                GooglePlace.Field.ID,
                GooglePlace.Field.NAME,
                GooglePlace.Field.ADDRESS,
                GooglePlace.Field.ADDRESS_COMPONENTS,
                GooglePlace.Field.LAT_LNG,
                GooglePlace.Field.TYPES,
                GooglePlace.Field.PHOTO_METADATAS
            )
        ).build()

        placesClient.fetchPlace(request)
            .addOnSuccessListener { cont.resume(it.place) }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    suspend fun getPlaceIdFromLatLngHttp(context: Context, lat: Double, lng: Double): String = withContext(Dispatchers.IO) {
        val apiKey = context.getString(R.string.google_web_api_key)
        val url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=$lat,$lng&key=$apiKey"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        val response = client.newCall(request).execute()
        val body = response.body?.string() ?: throw Exception("Empty response")

        val json = JSONObject(body)
        val results = json.getJSONArray("results")
        if (results.length() > 0) {
            val placeId = results.getJSONObject(0).getString("place_id")
            placeId
        } else {
            throw Exception("No results found")
        }
    }

    fun getStreetViewImage(lat: Double, lng: Double): List<Image>? {
        return null // TODO()
    }

    fun createNumberedMarkerIcon(text: String): BitmapDescriptor {
        val paint = Paint().apply {
            textSize = 42f
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
            typeface = Typeface.DEFAULT_BOLD
        }

        val width = 120
        val height = 120

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val circlePaint = Paint().apply {
            color = Color.RED
            isAntiAlias = true
        }
        canvas.drawCircle(width / 2f, height / 2f, width / 2.2f, circlePaint)

        val yPos = height / 2f - ((paint.descent() + paint.ascent()) / 2)
        canvas.drawText(text, width / 2f, yPos, paint)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}