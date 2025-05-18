package com.example.tripli.ui.auth.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripli.R
import com.example.tripli.data.model.auth.RegisterRequest
import com.example.tripli.data.repository.AuthRepository
import com.example.tripli.di.SnackbarEventBus
import com.example.tripli.di.StringProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun prefill() {
        _uiState.value = _uiState.value.copy(
            username = "Fiiliip",
            email = "fiiliip@tripli.sk",
            password = "password",
            confirmPassword = "password"
        )
    }

    fun onUsernameChange(value: String) {
        _uiState.value = _uiState.value.copy(username = value)
    }

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value)
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = value)
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(isPasswordVisible = !_uiState.value.isPasswordVisible)
    }

    fun register(onSuccess: () -> Unit) {
        val (username, email, password, confirmPassword) = _uiState.value

        if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            showMessage(StringProvider.get(R.string.fill_all_fields))
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showMessage(StringProvider.get(R.string.invalid_email_format))
            return
        }
        if (password.length < 8) {
            showMessage(StringProvider.get(R.string.password_length_error))
            return
        }
        if (password != confirmPassword) {
            showMessage(StringProvider.get(R.string.passwords_not_match))
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                authRepository.register(RegisterRequest(username, email, password))
                showMessage(StringProvider.get(R.string.register_success))
                onSuccess()
            } catch (e: Exception) {
                Log.e("RegisterViewModel", "Registration error: ${e.message}")
                showMessage(e.message ?: StringProvider.get(R.string.register_failed))
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun showMessage(message: String) {
        viewModelScope.launch {
            SnackbarEventBus.showMessage(message)
        }
    }
}

