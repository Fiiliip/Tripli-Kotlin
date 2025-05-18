package com.example.tripli.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.tripli.data.model.FilterData
import com.example.tripli.ui.navigation.Screen

@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    onFilterClick: () -> Unit = {}
) {
    val uiState by homeViewModel.uiState.collectAsState()

    HomeContent(
        uiState = uiState,
        onSearch = { query ->
            val currentFilters = uiState.filters ?: FilterData()
            homeViewModel.updateFilters(currentFilters.copy(search = query))
        },
        onFilterClick = onFilterClick,
        onItineraryClick = {
            navController.navigate(Screen.Itinerary.createRoute(it.id))
        }
    )
}

