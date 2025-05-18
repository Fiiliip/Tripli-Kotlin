package com.example.tripli.ui.itinerary

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.tripli.R
import com.example.tripli.data.model.itinerary.AdditionalInformation
import com.example.tripli.data.model.itinerary.Itinerary
import com.example.tripli.data.model.itinerary.LatLng
import com.example.tripli.data.model.itinerary.Place
import com.example.tripli.data.repository.ItineraryRepository
import com.example.tripli.di.SnackbarEventBus
import com.example.tripli.di.StringProvider
import com.example.tripli.domain.AuthStore
import com.example.tripli.ui.navigation.Screen
import com.example.tripli.utils.RouteOptimalizationUtils.formatForRouteMatrix
import com.example.tripli.utils.RouteOptimalizationUtils.formatForReadableDistance
import com.example.tripli.utils.RouteOptimalizationUtils.formatForReadableDuration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ItineraryViewModel(
    private val authStore: AuthStore,
    private val navController: NavHostController,
    private val itineraryRepository: ItineraryRepository,
    private val itineraryId: Int,
): ViewModel() {

    private val _uiState = MutableStateFlow(ItineraryUiState())
    val uiState: StateFlow<ItineraryUiState> = _uiState.asStateFlow()

    init {
        if (itineraryId != 0) {
            loadItinerary()
        } else {
            _uiState.update {
                it.copy(
                    isNewItinerary = true,
                    isEditMode = true,
                    showEditMode = true,
                )
            }
        }
    }

    private fun loadItinerary() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val result = itineraryRepository.getItineraryById(itineraryId)
                _uiState.update { it.copy(
                    itinerary = result ?: Itinerary(),
                    showEditMode = result?.owner?.id == authStore.getUser()?.id,
                    dayOptions = createDayOptions(itinerary = it.itinerary)
                )}
            } catch (e: Exception) {
                Log.e("ItineraryViewModel", "Error loading itinerary: ${e.message}")
                showMessage(e.message ?: StringProvider.get(R.string.error_loading_itinerary))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun selectPlace(place: Place) {
        _uiState.update {
            val updatedPlaces = it.itinerary.places.toMutableList().apply {
                val updatePlace = place.copy(
                    day = if (uiState.value.selectedDay == 0) uiState.value.dayOptions.size - 1 else uiState.value.selectedDay,
                    order = it.itinerary.places.size + 1
                )
                add(updatePlace)
            }
            it.copy(itinerary = it.itinerary.copy(places = updatedPlaces))
        }
    }

    fun showPlaceDetail(placeOrder: Int, navController: NavHostController) {
        navController.navigate(Screen.Place.createRoute(placeOrder))
    }

    fun setUiState(newState: ItineraryUiState) {
        _uiState.value = newState
    }

    fun getMarkers(): List<LatLng> {
        val markers = _uiState.value.itinerary.places.map { place ->
            LatLng(place.location.latitude, place.location.longitude)
        }
        return markers
    }

    fun toggleMapImages() {
        _uiState.update { it.copy(showMap = !it.showMap) }
    }

    fun toggleEditMode() {
        _uiState.update { it.copy(isEditMode = !it.isEditMode) }
    }

    fun updateItineraryTitle(title: String) {
        _uiState.update { it.copy(itinerary = it.itinerary.copy(title = title)) }
    }

    fun updateItineraryCategory(categoryId: Int) {
        _uiState.update { it.copy(itinerary = it.itinerary.copy(category = categoryId)) }
    }

    fun updateItineraryDescription(description: String) {
        _uiState.update { it.copy(itinerary = it.itinerary.copy(description = description)) }
    }

    fun addAdditionalInformation() {
        _uiState.update {
            val currentList = it.itinerary.additionalInformations
            val nextOrder = (currentList.maxOfOrNull { info -> info.order } ?: 0) + 1
            val newInfo = AdditionalInformation(order = nextOrder)
            val updatedList = currentList + newInfo
            it.copy(itinerary = it.itinerary.copy(additionalInformations = updatedList))
        }
    }

    fun removeAdditionalInformation(index: Int) {
        _uiState.update {
            val updatedList = it.itinerary.additionalInformations.toMutableList().apply { removeAt(index) }
            it.copy(itinerary = it.itinerary.copy(additionalInformations = updatedList))
        }
    }

    fun updateAdditionalInformationType(index: Int, type: String) {
        _uiState.update {
            val updatedList = it.itinerary.additionalInformations.toMutableList().apply {
                this[index] = this[index].copy(type = type)
            }
            it.copy(itinerary = it.itinerary.copy(additionalInformations = updatedList))
        }
    }

    fun updateAdditionalInformationValue(index: Int, value: String) {
        _uiState.update {
            val updatedList = it.itinerary.additionalInformations.toMutableList().apply {
                this[index] = this[index].copy(value = value)
            }
            it.copy(itinerary = it.itinerary.copy(additionalInformations = updatedList))
        }
    }

    fun saveChanges() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val itinerary = _uiState.value.itinerary.copy()
                val newItinerary = itinerary.copy(
                    category_id = 1,
                    places = itinerary.places.mapIndexed { index, place ->
                        place.copy(latitude = place.location.latitude, longitude = place.location.longitude)
                    },
                )
                if (_uiState.value.isNewItinerary) {
                    val response = itineraryRepository.createItinerary(newItinerary)
                    if (response != null) {
                        _uiState.update { it.copy(itinerary = response) }
                        showMessage(StringProvider.get(R.string.itinerary_created_success))
                        navController.navigate(Screen.Itinerary.createRoute(uiState.value.itinerary.id)) {
                            popUpTo(Screen.Itinerary.route) { inclusive = true }
                        }
                    }
                } else {
                    if (itineraryRepository.updateItinerary(newItinerary)) {
                        showMessage(StringProvider.get(R.string.itinerary_updated_success))
                    }
                }
            } catch (e: Exception) {
                Log.e("ItineraryViewModel", "Error saving itinerary: ${e.message}")
                showMessage(StringProvider.get(R.string.error_saving_itinerary))
            } finally {
                _uiState.update { it.copy(
                    isLoading = false,
                    isEditMode = false,
                ) }
            }
        }
    }

    private fun createDayOptions(itinerary: Itinerary?): List<Pair<Int, String>> {
        val maxDay = itinerary?.places?.maxOfOrNull { it.day } ?: 1
//        return listOf(0 to "All days") + (1..maxDay).map { it to "Day $it" }
        return listOf(0 to StringProvider.get(R.string.all_days)) +
                (1..maxDay).map { it to StringProvider.get(R.string.day) + " $it" }
    }

    fun selectDay(day: Int) {
        _uiState.update { it.copy(selectedDay = day) }
    }

    fun addDay() {
        _uiState.update {
            val newDay = (it.dayOptions.maxOfOrNull { o -> o.first } ?: 0) + 1
            it.copy(dayOptions = it.dayOptions + (newDay to "${StringProvider.get(R.string.day)} $newDay"))
        }
    }

    fun removeDay() {
        _uiState.update {
            if (it.dayOptions.size > 2) {
                val newOptions = it.dayOptions.dropLast(1)
                val newPlaces = it.itinerary.places.filter { place -> place.day < newOptions.size }
                it.copy(
                    dayOptions = newOptions,
                    itinerary = it.itinerary.copy(places = newPlaces)
                )
            } else it
        }
    }

    fun removePlace(placeId: String) {
        _uiState.update {
            val filteredPlaces = it.itinerary.places.filter { place -> place.id != placeId }
            val reorderedPlaces = filteredPlaces.mapIndexed { index, place ->
                place.copy(order = index + 1)
            }
            it.copy(itinerary = it.itinerary.copy(places = reorderedPlaces))
        }
    }

    fun deleteItinerary() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                itineraryRepository.deleteItinerary(itineraryId)
                showMessage(StringProvider.get(R.string.itinerary_deleted_success))
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Itinerary.route) { inclusive = true }
                }
            } catch (e: Exception) {
                Log.e("ItineraryViewModel", "Error deleting itinerary: ${e.message}")
                showMessage(e.message ?: StringProvider.get(R.string.error_deleting_itinerary))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun optimalizeRoute() {
        if (_uiState.value.itinerary.places.size <= 2) {
            showMessage(StringProvider.get(R.string.cannot_optimize_few_places))
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val result = itineraryRepository.optimalizeRoute(formatForRouteMatrix(_uiState.value.itinerary.places))
                if (result != null) {
                    val route = result.route
                    _uiState.update {
                        val updatedItinerary = it.itinerary.copy(
                            places = it.itinerary.places.map { place ->
                                // Aktualizujeme poradie miesta podľa optimálnej trasy
                                val placeIndex = route.indexOfFirst { it.placeId == place.id }
                                place.copy(order = route[placeIndex].order)
                            }.sortedBy { it.order } // Zoradíme miesta podľa nového poradia
                        )

                        // Pridáme informácie o vzdialenosti a trvaní
                        val updatedItineraryWithInfo = updatedItinerary.copy(
                            additionalInformations = updatedItinerary.additionalInformations.filter {
                                it.type != StringProvider.get(R.string.distance) && it.type != StringProvider.get(R.string.duration)
                            } + listOf(
                                AdditionalInformation(
                                    type = StringProvider.get(R.string.distance),
                                    value = formatForReadableDistance(result.distanceMeters),
                                    order = updatedItinerary.additionalInformations.size + 1
                                ),
                                AdditionalInformation(
                                    type = StringProvider.get(R.string.duration),
                                    value = formatForReadableDuration(result.durationSeconds),
                                    order = updatedItinerary.additionalInformations.size + 2
                                )
                            )
                        )

                        it.copy(itinerary = updatedItineraryWithInfo)
                    }
                }
            } catch (e: Exception) {
                Log.e("ItineraryViewModel", "Error optimizing route: ${e.message}")
                showMessage(StringProvider.get(R.string.error_optimizing_route))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun showMessage(message: String) {
        viewModelScope.launch {
            SnackbarEventBus.showMessage(message)
        }
    }
}