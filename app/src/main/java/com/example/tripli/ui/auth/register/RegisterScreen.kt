package com.example.tripli.ui.auth.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.tripli.ui.navigation.Screen

@Composable
fun RegisterScreen(
    navController: NavController,
    registerViewModel: RegisterViewModel
) {
    val uiState by registerViewModel.uiState.collectAsState()

    RegisterContent(
        uiState = uiState,
        onBackClick = { navController.popBackStack() },
        onPrefillClick = registerViewModel::prefill,
        onUsernameChange = registerViewModel::onUsernameChange,
        onEmailChange = registerViewModel::onEmailChange,
        onPasswordChange = registerViewModel::onPasswordChange,
        onConfirmPasswordChange = registerViewModel::onConfirmPasswordChange,
        onTogglePasswordVisibility = registerViewModel::togglePasswordVisibility,
        onRegisterClick = { registerViewModel.register { navController.navigate(Screen.Home.route) { popUpTo(Screen.Register.route) { inclusive = true } } } },
        onNavigateToLoginClick = { navController.navigate(Screen.Login.route) { popUpTo(Screen.Register.route) { inclusive = true } } },
    )
}
