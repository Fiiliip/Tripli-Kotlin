package com.example.tripli.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripli.data.model.FilterData
import com.example.tripli.data.repository.ItineraryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val itineraryRepository: ItineraryRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadItineraries()
    }

    fun updateFilters(newFilters: FilterData?) {
        _uiState.value = _uiState.value.copy(filters = newFilters)
        loadItineraries()
    }

    fun loadItineraries() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val result = itineraryRepository.getItineraries(filters = _uiState.value.filters)
                _uiState.value = _uiState.value.copy(itineraries = result)
            } catch (e: Exception) {
                TODO()
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}