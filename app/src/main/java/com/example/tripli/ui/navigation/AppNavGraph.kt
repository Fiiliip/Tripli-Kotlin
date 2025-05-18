package com.example.tripli.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tripli.di.AppContainer
import com.example.tripli.ui.itinerary.ItineraryScreen
import com.example.tripli.ui.itinerary.ItineraryViewModel
import com.example.tripli.ui.place.PlaceScreen
import com.example.tripli.ui.place.PlaceViewModel
import com.example.tripli.ui.home.HomeScreen
import com.example.tripli.ui.home.HomeViewModel
import com.example.tripli.ui.auth.login.LoginScreen
import com.example.tripli.ui.auth.login.LoginViewModel
import com.example.tripli.ui.auth.register.RegisterScreen
import com.example.tripli.ui.auth.register.RegisterViewModel
import com.example.tripli.ui.profile.ProfileScreen
import com.example.tripli.ui.profile.ProfileViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    appContainer: AppContainer
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        var currentItineraryViewModel : ItineraryViewModel? = null

        composable(Screen.Login.route) {
            val loginViewModel = remember { LoginViewModel(appContainer.authRepository) }
            LoginScreen(navController, loginViewModel)
        }

        composable(Screen.Register.route) {
            val registerViewModel = remember { RegisterViewModel(appContainer.authRepository) }
            RegisterScreen(navController, registerViewModel)
        }

        composable(Screen.Home.route) {
            val homeViewModel = remember { HomeViewModel(appContainer.itineraryRepository) }
            HomeScreen(navController, homeViewModel)
        }

        composable(Screen.Profile.route) {
            if (appContainer.authStore.isLoggedIn()) {
                val profileViewModel = remember { ProfileViewModel(appContainer.authStore, appContainer.authRepository, appContainer.itineraryRepository) }
                ProfileScreen(navController, profileViewModel)
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            }
        }

        composable(Screen.Itinerary.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val itineraryId = backStackEntry.arguments?.getInt("id") ?: 0
            if (itineraryId == 0 && !appContainer.authStore.isLoggedIn()) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            } else {
                val itineraryViewModel = remember(itineraryId) {
                    if (currentItineraryViewModel != null && currentItineraryViewModel?.uiState?.value?.itinerary?.id == itineraryId) {
                        currentItineraryViewModel
                    } else {
                        ItineraryViewModel(appContainer.authStore, navController, appContainer.itineraryRepository, itineraryId)
                    }
                }
                currentItineraryViewModel = itineraryViewModel
                currentItineraryViewModel?.let {
                    ItineraryScreen(navController, it)
                }
            }
        }

        composable(Screen.Place.route,
            arguments = listOf(navArgument("order") { type = NavType.IntType })
        ) { backStackEntry ->
            val placeOrder = backStackEntry.arguments?.getInt("order") ?: 0
            val placeDetailViewModel = remember { currentItineraryViewModel?.let { PlaceViewModel(it, placeOrder) } }
            if (placeDetailViewModel != null) {
                PlaceScreen(navController, placeDetailViewModel)
            }
        }
    }
}
