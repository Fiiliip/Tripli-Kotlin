package com.example.tripli.ui.place

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.tripli.R
import com.example.tripli.data.model.itinerary.LatLng
import com.example.tripli.data.model.itinerary.Place
import com.example.tripli.ui._components.google.GoogleAutocomplete
import com.example.tripli.ui._components.google.GoogleMap
import com.example.tripli.ui.itinerary._components.AdditionalInformation
import com.example.tripli.ui.itinerary._components.ItineraryHeader
import com.example.tripli.data.model.itinerary.AdditionalInformation as AdditionalInformationModel

@Composable
fun PlaceContent(
    uiState: PlaceUiState,
    getMarkers: () -> List<LatLng> = { emptyList() },
    onPlaceSelected: (Place) -> Unit = {},
    onBackClick: () -> Unit,
    onToggleMapImages: () -> Unit,
    onToggleEditMode: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDayChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onAdditionalInformationTypeChange: (Int, String) -> Unit,
    onAdditionalInformationValueChange: (Int, String) -> Unit,
    onAddAdditionalInformationClick: () -> Unit,
    onRemoveAdditionalInformationClick: (Int) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
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
//                            ImageSwiper(
//                                images = uiState.images,
//                                editMode = uiState.isEditMode,
//                                onAddPhoto = onUploadPhotoClick,
//                                onRemovePhoto = onRemovePhotoClick
//                            )
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
                            .zIndex(1f),
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
                    Column(Modifier.padding(12.dp)) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            TextField(
                                value = uiState.place.title,
                                onValueChange = onTitleChange,
                                modifier = Modifier.weight(1f),
                                readOnly = !uiState.isEditMode,
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

                        Spacer(Modifier.height(8.dp))

                        if (uiState.place.day != 0) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 18.dp, end = 18.dp)

                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    TextField(
                                        value = uiState.place.day.toString(),
                                        onValueChange = onDayChange,
                                        readOnly = !uiState.isEditMode,
                                        modifier = Modifier.width(60.dp),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                                        colors = TextFieldDefaults.colors(
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            focusedContainerColor = Color.Transparent,
                                            unfocusedContainerColor = Color.Transparent
                                        )
                                    )
                                    Text(
                                        text = stringResource(R.string.day),
                                        modifier = Modifier.padding(start = 2.dp),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                            }
                            Spacer(Modifier.height(8.dp))
                        }

                        uiState.place.additionalInformations.forEachIndexed { index, info ->
                            AdditionalInformation(
                                additionalInformation = info,
                                editMode = uiState.isEditMode,
                                onTypeChanged = { newType -> onAdditionalInformationTypeChange(index, newType) },
                                onValueChanged = { newValue -> onAdditionalInformationValueChange(index, newValue) },
                                onRemove = { onRemoveAdditionalInformationClick(index) }
                            )
                        }

                        if (uiState.isEditMode) {
                            Spacer(Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                TextButton(onClick = onAddAdditionalInformationClick) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(stringResource(R.string.add_additional_information), color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }

                TextField(
                    value = uiState.place.description ?: "",
                    onValueChange = onDescriptionChange,
                    placeholder = if (uiState.isEditMode) { { Text(stringResource(R.string.enter_description)) } } else null,
                    readOnly = !uiState.isEditMode,
                    modifier = Modifier
                        .offset(y = (-20).dp)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Preview (showBackground = true)
@Composable
fun PlaceContentPreview() {
    PlaceContent(
        uiState = PlaceUiState(
            place = Place(
                title = "Place Title",
                description = "Place Description",
                day = 1,
                additionalInformations = listOf(
                    AdditionalInformationModel(type = "Type 1", value = "Value 1"),
                    AdditionalInformationModel(type = "Type 2", value = "Value 2")
                )
            ),
            showMap = true,
            isEditMode = true,
            showEditMode = true
        ),
        onBackClick = {},
        onToggleMapImages = {},
        onPlaceSelected = {},
        onToggleEditMode = {},
        onTitleChange = {},
        onDayChange = {},
        onDescriptionChange = {},
        onAdditionalInformationTypeChange = { _, _ -> },
        onAdditionalInformationValueChange = { _, _ -> },
        onAddAdditionalInformationClick = {},
        onRemoveAdditionalInformationClick = {}
    )
}