package com.example.tripli.ui.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripli.R
import com.example.tripli.data.model.auth.LoginRequest
import com.example.tripli.data.repository.AuthRepository
import com.example.tripli.di.SnackbarEventBus
import com.example.tripli.di.StringProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun prefill() {
        _uiState.value = _uiState.value.copy(
            email = "fiiliip@tripli.sk",
            password = "password"
        )
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(isPasswordVisible = !_uiState.value.isPasswordVisible)
    }

    fun login(onSuccess: () -> Unit) {
        val (email, password) = _uiState.value

        if (email.isBlank() || password.isBlank()) {
            showMessage(StringProvider.get(R.string.fill_all_fields))
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.isBlank()) {
            showMessage(StringProvider.get(R.string.error_invalid_email_password))
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                authRepository.login(LoginRequest(email, password))
                showMessage(StringProvider.get(R.string.login_successful))
                onSuccess()
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Login error: ${e.message}")
                showMessage(e.message ?: StringProvider.get(R.string.error_login_failed))
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
