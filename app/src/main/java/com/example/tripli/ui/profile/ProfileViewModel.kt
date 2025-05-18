package com.example.tripli.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.tripli.R
import com.example.tripli.data.model.FilterData
import com.example.tripli.data.repository.AuthRepository
import com.example.tripli.data.repository.ItineraryRepository
import com.example.tripli.di.SnackbarEventBus
import com.example.tripli.di.StringProvider
import com.example.tripli.domain.AuthStore
import com.example.tripli.ui.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authStore: AuthStore,
    private val authRepository: AuthRepository,
    private val itineraryRepository: ItineraryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        val user = authStore.getUser()
        if (user != null) {
            _uiState.update { it.copy(user = user) }
            loadItineraries()
        }
    }

    private fun loadItineraries() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val result = itineraryRepository.getItineraries(FilterData(owner = uiState.value.user.id))
                _uiState.update { it.copy(itineraries = result) }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error loading itineraries: ${e.message}")
                showMessage(e.message ?: StringProvider.get(R.string.error_failed_get_itineraries))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun logout(navController: NavHostController) {
        viewModelScope.launch {
            try {
                authRepository.logout()
                showMessage(StringProvider.get(R.string.logged_out_successfully))
                navController.navigate(Screen.Home.route) {
                    popUpTo(0) { inclusive = true }
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error logging out: ${e.message}")
                showMessage(e.message ?: StringProvider.get(R.string.error_logging_out))
            }
        }
    }

    fun updateUsername(username: String) {
        _uiState.update { it.copy(
            user = it.user.copy(username = username),
            usernameChanged = _uiState.value.user.username != username
        ) }
    }

    fun saveChanges() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                authRepository.updateUserInfo(mapOf("username" to _uiState.value.user.username))
                showMessage(StringProvider.get(R.string.profile_updated_successfully))
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error updating profile: ${e.message}")
                showMessage(e.message ?: StringProvider.get(R.string.error_updating_profile))
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