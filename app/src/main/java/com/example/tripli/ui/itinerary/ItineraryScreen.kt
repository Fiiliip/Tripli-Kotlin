package com.example.tripli.ui.itinerary

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController

@Composable
fun ItineraryScreen(
    navController: NavHostController,
    itineraryViewModel: ItineraryViewModel,
) {
    val uiState by itineraryViewModel.uiState.collectAsState()

    ItineraryContent(
        uiState = uiState,
        onBackClick = { navController.popBackStack()},
        onToggleMapImages = { itineraryViewModel.toggleMapImages() },
        getMarkers = { itineraryViewModel.getMarkers() },
        onPlaceSelected = { itineraryViewModel.selectPlace(it) },
        onToggleEditMode = { itineraryViewModel.toggleEditMode() },
        onTitleChange = { itineraryViewModel.updateItineraryTitle(it) },
        onCategoryChange = { itineraryViewModel.updateItineraryCategory(it) },
        onDescriptionChange = { itineraryViewModel.updateItineraryDescription(it) },
        onAdditionalInformationTypeChange = { index, type -> itineraryViewModel.updateAdditionalInformationType(index, type) },
        onAdditionalInformationValueChange = { index, value -> itineraryViewModel.updateAdditionalInformationValue(index, value) },
        onAddAdditionalInformationClick = { itineraryViewModel.addAdditionalInformation() },
        onRemoveAdditionalInformationClick = { itineraryViewModel.removeAdditionalInformation(it) },
        onSaveChangesClick = { itineraryViewModel.saveChanges() },
        onOptimizeRouteClick = { itineraryViewModel.optimalizeRoute() },
        onDaySelect = { itineraryViewModel.selectDay(it) },
        onAddDayClick = { itineraryViewModel.addDay() },
        onRemoveDayClick = { itineraryViewModel.removeDay() },
        onPlaceClick = { itineraryViewModel.showPlaceDetail(it, navController) },
        onRemovePlaceClick = { itineraryViewModel.removePlace(it) },
        onDeleteItineraryClick = { itineraryViewModel.deleteItinerary() },
    )
}