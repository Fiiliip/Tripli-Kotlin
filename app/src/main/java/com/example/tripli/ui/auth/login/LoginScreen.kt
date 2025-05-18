package com.example.tripli.ui.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.tripli.ui.navigation.Screen

@Composable
fun LoginScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel
) {
    val uiState by loginViewModel.uiState.collectAsState()

    LoginContent(
        uiState = uiState,
        onBackClick = { navController.popBackStack() },
        onPrefillClick = loginViewModel::prefill,
        onEmailChange = loginViewModel::onEmailChange,
        onPasswordChange = loginViewModel::onPasswordChange,
        onTogglePasswordVisibility = loginViewModel::togglePasswordVisibility,
        onLoginClick = {
            loginViewModel.login(
                onSuccess = { navController.navigate(Screen.Home.route) { popUpTo(Screen.Login.route) { inclusive = true } } },
            )
        },
        onNavigateToRegister = { navController.navigate(Screen.Register.route) { popUpTo(Screen.Login.route) { inclusive = true } } }
    )
}
