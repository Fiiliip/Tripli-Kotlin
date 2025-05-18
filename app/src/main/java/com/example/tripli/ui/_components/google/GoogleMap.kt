package com.example.tripli.ui._components.google

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.tripli.data.model.itinerary.LatLng
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng as GoogleLatLng
import com.example.tripli.data.model.itinerary.Place
import com.example.tripli.di.PlacesClientProvider
import com.example.tripli.utils.GoogleUtils.createNumberedMarkerIcon
import com.example.tripli.utils.GoogleUtils.formatPlaceDetails
import com.example.tripli.utils.GoogleUtils.getPlaceDetails
import com.example.tripli.utils.GoogleUtils.getPlaceIdFromLatLngHttp
import com.google.android.gms.maps.GoogleMapOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun GoogleMap(
    modifier: Modifier = Modifier,
    markers: List<LatLng>,
    onPlaceSelected: (Place) -> Unit = {},
    onMapLoaded: () -> Unit = {}
) {
    val context = LocalContext.current

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(GoogleLatLng(48.1486, 17.1077), 8f) // center, zoom
    }

    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                isBuildingEnabled = true
            )
        )
    }

    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = false,
                compassEnabled = true
            )
        )
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = uiSettings,
        onMapLoaded = onMapLoaded,
        googleMapOptionsFactory = {
            GoogleMapOptions().mapId("bceefbf673d2c1fd")
        },
        onPOIClick = {
            val placesClient = PlacesClientProvider.getInstance(context)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val placeId = it.placeId
                    val place = getPlaceDetails(placesClient, placeId)

                    withContext(Dispatchers.Main) {
                        onPlaceSelected(
                            formatPlaceDetails(placesClient, place)
                        )
                    }
                } catch (e: Exception) {
                    Log.e("GoogleMap", "Place fetch error", e)
                }
            }
        },
        onMapClick = { latLng ->
            val placesClient = PlacesClientProvider.getInstance(context)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val placeId = getPlaceIdFromLatLngHttp(context, latLng.latitude, latLng.longitude)
                    val place = getPlaceDetails(placesClient, placeId)

                    withContext(Dispatchers.Main) {
                        onPlaceSelected(
                            formatPlaceDetails(placesClient, place)
                        )
                    }
                } catch (e: Exception) {
                    Log.e("GoogleMap", "Place fetch error", e)
                }
            }
        }
    ) {
        val groupedMarkers: Map<LatLng, List<Int>> = markers
            .mapIndexed { index, latLng -> latLng to index + 1 }
            .groupBy({ it.first }, { it.second })

        groupedMarkers.forEach { (latLng, indices) ->
            val label = indices.joinToString(", ")

            Marker(
                state = MarkerState(position = GoogleLatLng(latLng.latitude, latLng.longitude)),
                icon = createNumberedMarkerIcon(label)
            )
        }
    }
}
