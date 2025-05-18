package com.example.tripli.ui.itinerary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.tripli.R
import com.example.tripli.data.model.User
import com.example.tripli.data.model.itinerary.Itinerary
import com.example.tripli.data.model.itinerary.LatLng
import com.example.tripli.data.model.itinerary.Place
import com.example.tripli.ui._components.google.GoogleAutocomplete
import com.example.tripli.ui._components.google.GoogleMap
import com.example.tripli.ui.itinerary._components.AdditionalInformation
import com.example.tripli.ui.itinerary._components.ItineraryHeader
import com.example.tripli.ui.itinerary._components.PlaceItem
import com.example.tripli.data.model.itinerary.AdditionalInformation as AdditionalInformationModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryContent(
    uiState: ItineraryUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onToggleMapImages: () -> Unit = {},
    getMarkers: () -> List<LatLng> = { emptyList() },
    onPlaceSelected: (Place) -> Unit = {},
    onToggleEditMode: () -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onCategoryChange: (Int) -> Unit = {},
    onDescriptionChange: (String) -> Unit = {},
    onAdditionalInformationTypeChange: (Int, String) -> Unit = { _, _ -> },
    onAdditionalInformationValueChange: (Int, String) -> Unit = { _, _ -> },
    onAddAdditionalInformationClick: () -> Unit = {},
    onRemoveAdditionalInformationClick: (Int) -> Unit = {},
    onSaveChangesClick: () -> Unit = {},
    onOptimizeRouteClick: () -> Unit = {},
    onDaySelect: (Int) -> Unit = {},
    onAddDayClick: () -> Unit = {},
    onRemoveDayClick: () -> Unit = {},
    onPlaceClick: (Int) -> Unit = {},
    onRemovePlaceClick: (String) -> Unit = {},
    onDeleteItineraryClick: () -> Unit = {},
) {
    val visiblePlaces = remember(uiState.selectedDay, uiState.itinerary.places) {
        if (uiState.selectedDay == 0) {
            uiState.itinerary.places
        } else {
            uiState.itinerary.places.filter { place ->
                place.day == uiState.selectedDay
            }
        }
    }

    Box(modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Box {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                            .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                    ) {
                        if (uiState.showMap) {
                            GoogleMap(
                                modifier = Modifier.fillMaxSize(),
                                markers = getMarkers(),
                                onPlaceSelected = onPlaceSelected
                            )
                        } else {
                            // ImageSwiper(images = uiState.itinerary.images)
                        }
                    }

                    ItineraryHeader(
                        showMap = uiState.showMap,
                        onBackClick = onBackClick,
                        onToggleImagesMap = onToggleMapImages,
                        content = { if (uiState.isEditMode) {
                            GoogleAutocomplete { place ->
                                onPlaceSelected(place)
                            }
                        } },
                        modifier = Modifier
                            .absoluteOffset(y = 10.dp)
                            .padding(horizontal = 16.dp)
                            .zIndex(1f)
                    )
                }

                Card(
                    modifier = Modifier
                        .offset(y = (-40).dp)
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(Modifier.padding(8.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            TextField(
                                value = uiState.itinerary.title,
                                onValueChange = onTitleChange,
                                modifier = Modifier.weight(1f),
                                readOnly = !uiState.isEditMode,
                                singleLine = true,
                                placeholder = { Text(stringResource(R.string.enter_title)) },
                                textStyle = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent
                                ),
                            )
                            if (uiState.showEditMode) {
                                IconButton(onClick = onToggleEditMode) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = stringResource(R.string.edit),
                                        tint = if (uiState.isEditMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                        }

//                        if (uiState.isEditMode) {
//                        CategoryDropdown(
//                            selectedCategory = uiState.itinerary.category,
//                            onCategorySelected = onCategorySelected
//                        )
//                        } else {
//                            Text(text = "Category", style = MaterialTheme.typography.bodyMedium)
//                        }

                        TextField(
                            value = uiState.itinerary.description,
                            onValueChange = onDescriptionChange,
                            placeholder = { Text(stringResource(R.string.enter_description)) },
                            readOnly = !uiState.isEditMode,
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                        )

                        uiState.itinerary.additionalInformations.forEachIndexed { index, additionalInformation ->
                            AdditionalInformation(
                                additionalInformation = additionalInformation,
                                editMode = uiState.isEditMode,
                                onRemove = { onRemoveAdditionalInformationClick(index) },
                                onTypeChanged = { type -> onAdditionalInformationTypeChange(index, type) },
                                onValueChanged = { value -> onAdditionalInformationValueChange(index, value) }
                            )
                        }

                        if (uiState.isEditMode) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                TextButton(
                                    onClick = onAddAdditionalInformationClick,
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = stringResource(R.string.add_additional_information),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }

                if (!uiState.isEditMode && !uiState.isNewItinerary) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 24.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = stringResource(R.string.user_icon),
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = uiState.itinerary.owner?.username ?: "",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
//                    Rating(itinerary = uiState.itinerary)
                    }
                }

                if (uiState.isEditMode) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconButton(onClick = onSaveChangesClick) {
                            Icon(
                                Icons.Default.Save,
                                contentDescription = stringResource(R.string.save),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                        IconButton(onClick = onOptimizeRouteClick) {
                            Icon(
                                Icons.Default.Route,
                                contentDescription = stringResource(R.string.optimize_route),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }

                if (uiState.dayOptions.size >= 3) {
                    var expanded by remember { mutableStateOf(false) }

                    val selectedText = uiState.dayOptions
                        .firstOrNull { it.first == uiState.selectedDay }
                        ?.second
                        ?: uiState.dayOptions.first().second

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = modifier.fillMaxWidth()
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded },
                        ) {
                            OutlinedTextField(
                                readOnly = true,
                                value = selectedText,
                                onValueChange = {},
                                label = { Text(stringResource(R.string.select_day)) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary
                                ),
                                modifier = Modifier.menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.background(Color.White)
                            ) {
                                uiState.dayOptions.forEach { (dayValue, dayLabel) ->
                                    DropdownMenuItem(
                                        text = { Text(dayLabel) },
                                        onClick = {
                                            expanded = false
                                            onDaySelect(dayValue)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                if (uiState.isEditMode) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(onClick = onAddDayClick) {
                            Icon(Icons.Default.Add, null)
                            Text(stringResource(R.string.add_day))
                        }
                        TextButton(
                            onClick = onRemoveDayClick,
                            enabled = uiState.dayOptions.size > 2
                        ) {
                            Icon(Icons.Default.Remove, null)
                            Text(stringResource(R.string.remove_day))
                        }
                    }
                }
            }

            itemsIndexed(visiblePlaces) { index, place ->
                PlaceItem(
                    place = place,
                    editMode = uiState.isEditMode,
                    onClick = { onPlaceClick(place.order) },
                    onRemoveClick = { onRemovePlaceClick(it) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (uiState.isEditMode && !uiState.isNewItinerary) {
                item {
                    Button(
                        onClick = onDeleteItineraryClick,
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !uiState.isLoading
                    ) {
                        Text(stringResource(R.string.delete_itinerary), color = Color.White)
                    }
                }
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .pointerInput(Unit) {}
                    .zIndex(10f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItineraryContentPreview() {
    ItineraryContent(
        uiState = ItineraryUiState(
            isLoading = false,
            showMap = true,
            showEditMode = true,
            isEditMode = true,
            isNewItinerary = false,
            itinerary = Itinerary(
                id = 1,
                title = "Trip to Paris",
                description = "A wonderful trip to Paris",
                category = 1,
                owner = User(id = "1", username = "JohnDoe", email = ""),
                places = listOf(
                    Place(id = "1", title = "Eiffel Tower", location = LatLng(48.8584, 2.2941)),
                    Place(id = "2", title = "Louvre Museum", location = LatLng(48.8606, 2.3376))
                ),
                additionalInformations = listOf(
                    AdditionalInformationModel(type = "Type", value = "Value")
                ),
                rating = 4.5f
            ),
            dayOptions = listOf(
                Pair(1, "Day 1"),
                Pair(2, "Day 2"),
                Pair(3, "Day 3")
            ),
        ),
        onBackClick = {},
        onToggleMapImages = {},
        onPlaceSelected = {},
        onToggleEditMode = {},
        onTitleChange = {},
        onCategoryChange = {},
        onDescriptionChange = {},
        onAdditionalInformationTypeChange = { _, _ -> },
        onAdditionalInformationValueChange = { _, _ -> },
        onAddAdditionalInformationClick = {},
        onRemoveAdditionalInformationClick = {},
        onSaveChangesClick = {},
        onOptimizeRouteClick = {},
        onDaySelect = {},
        onAddDayClick = {},
        onRemoveDayClick = {},
        onPlaceClick = {},
        onRemovePlaceClick = {},
        onDeleteItineraryClick = {}
    )
}