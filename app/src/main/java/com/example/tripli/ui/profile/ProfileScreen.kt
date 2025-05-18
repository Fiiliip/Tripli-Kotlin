package com.example.tripli.ui.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.tripli.ui.navigation.Screen

@Composable
fun ProfileScreen(
    navController: NavHostController,
    profileViewModel: ProfileViewModel,
) {
    val uiState by profileViewModel.uiState.collectAsState()

    ProfileContent(
        uiState = uiState,
        onBackClick = { navController.popBackStack() },
        onLogoutClick = { profileViewModel.logout(navController) },
        onUsernameChange = { profileViewModel.updateUsername(it) },
        onItineraryClick = { navController.navigate(Screen.Itinerary.createRoute(it)) },
        onSaveChangesClick = { profileViewModel.saveChanges() },
    )
}