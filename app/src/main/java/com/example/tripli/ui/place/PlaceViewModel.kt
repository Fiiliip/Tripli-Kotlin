package com.example.tripli.ui.place

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.tripli.data.model.itinerary.LatLng
import com.example.tripli.data.model.itinerary.Place
import com.example.tripli.ui.itinerary.ItineraryViewModel
import com.example.tripli.data.model.itinerary.AdditionalInformation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlaceViewModel(
    private val itineraryViewModel: ItineraryViewModel,
    private val placeOrder: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlaceUiState())
    val uiState: StateFlow<PlaceUiState> = _uiState.asStateFlow()

    init {
        val currentPlace = itineraryViewModel.uiState.value.itinerary.places.firstOrNull { place -> place.order == placeOrder }
        //val currentPlace = itineraryViewModel.uiState.value.itinerary.places.getOrNull(placeIndex)
        if (currentPlace != null) {
            _uiState.value = PlaceUiState(
                place = currentPlace.copy(),
                showMap = itineraryViewModel.uiState.value.showMap,
                isEditMode = itineraryViewModel.uiState.value.isEditMode,
                showEditMode = itineraryViewModel.uiState.value.showEditMode
            )
        }
    }

    fun getMarkers(): List<LatLng> {
        return listOf(uiState.value.place.location)
    }

    fun selectPlace(place: Place) {
        _uiState.update { it.copy(place = place) }
    }

    fun onBackClick(navController: NavHostController) {
        saveChanges()
        navController.popBackStack()
    }

    fun toggleMapImages() {
        _uiState.update { it.copy(showMap = !it.showMap) }
    }

    fun toggleEditMode() {
        _uiState.update { it.copy(isEditMode = !it.isEditMode) }
    }

    fun updatePlaceTitle(title: String) {
        _uiState.update { it.copy(place = it.place.copy(title = title)) }
    }

    fun updateDay(day: String) {
        if (day.isEmpty()) return
        _uiState.update { it.copy(place = it.place.copy(day = day.toInt())) }
    }

    fun updatePlaceDescription(description: String) {
        _uiState.update { it.copy(place = it.place.copy(description = description)) }
    }

    fun updateAdditionalInformationType(index: Int, type: String) {
        val currentList = uiState.value.place.additionalInformations.toMutableList()
        if (index in currentList.indices) {
            currentList[index] = currentList[index].copy(type = type)
            val updatedPlace = uiState.value.place.copy(additionalInformations = currentList)
            _uiState.update { it.copy(place = updatedPlace) }
        }
    }

    fun updateAdditionalInformationValue(index: Int, value: String) {
        val currentList = uiState.value.place.additionalInformations.toMutableList()
        if (index in currentList.indices) {
            currentList[index] = currentList[index].copy(value = value)
            val updatedPlace = uiState.value.place.copy(additionalInformations = currentList)
            _uiState.update { it.copy(place = updatedPlace) }
        }
    }

    fun addAdditionalInformation() {
        val currentList = uiState.value.place.additionalInformations
        val nextOrder = (currentList.maxOfOrNull { it.order } ?: 0) + 1
        val newInfo = AdditionalInformation(order = nextOrder)
        val updatedList = currentList + newInfo
        val updatedPlace = uiState.value.place.copy(additionalInformations = updatedList)
        _uiState.update { it.copy(place = updatedPlace) }
    }

    fun removeAdditionalInformation(index: Int) {
        val currentList = uiState.value.place.additionalInformations.toMutableList()
        if (index in currentList.indices) {
            currentList.removeAt(index)
            val updatedPlace = uiState.value.place.copy(additionalInformations = currentList)
            _uiState.update { it.copy(place = updatedPlace) }
        }
    }

    fun saveChanges() {
        val updatedPlace = uiState.value.place

        val currentState = itineraryViewModel.uiState.value
        val updatedPlaces = currentState.itinerary.places.toMutableList()

        val index = currentState.itinerary.places.indexOfFirst { it.order == placeOrder }
        if (index == -1) return
        updatedPlaces[index] = updatedPlace

        val updatedItinerary = currentState.itinerary.copy(places = updatedPlaces)

        val newItineraryUiState = currentState.copy(
            itinerary = updatedItinerary,
            showMap = uiState.value.showMap,
            isEditMode = uiState.value.isEditMode,
            showEditMode = uiState.value.showEditMode
        )

        itineraryViewModel.setUiState(newItineraryUiState)
    }
}

