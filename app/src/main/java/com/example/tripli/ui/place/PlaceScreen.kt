package com.example.tripli.ui.place

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController

@Composable
fun PlaceScreen(
    navController: NavHostController,
    placeViewModel: PlaceViewModel,
) {
    val uiState by placeViewModel.uiState.collectAsState()

    PlaceContent(
        uiState = uiState,
        getMarkers = { placeViewModel.getMarkers() },
        onPlaceSelected = { placeViewModel.selectPlace(it) },
        onBackClick = { placeViewModel.onBackClick(navController) },
        onToggleMapImages = { placeViewModel.toggleMapImages() },
        onToggleEditMode = { placeViewModel.toggleEditMode() },
        onTitleChange = { placeViewModel.updatePlaceTitle(it) },
        onDayChange = { placeViewModel.updateDay(it) },
        onDescriptionChange = { placeViewModel.updatePlaceDescription(it) },
        onAdditionalInformationTypeChange = { index, type -> placeViewModel.updateAdditionalInformationType(index, type) },
        onAdditionalInformationValueChange = { index, value -> placeViewModel.updateAdditionalInformationValue(index, value) },
        onAddAdditionalInformationClick = { placeViewModel.addAdditionalInformation() },
        onRemoveAdditionalInformationClick = { placeViewModel.removeAdditionalInformation(it) },
    )
}