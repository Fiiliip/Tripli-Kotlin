package com.example.tripli.ui._components.google

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.example.tripli.R
import com.example.tripli.data.model.itinerary.Place
import com.example.tripli.di.PlacesClientProvider
import com.example.tripli.utils.GoogleUtils.formatPlaceDetails
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place as GooglePlace
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun GoogleAutocomplete(
    onPlaceSelected: (Place) -> Unit,
) {
    val context = LocalContext.current
    val placesClient = remember { PlacesClientProvider.getInstance(context) }

    var query by remember { mutableStateOf("") }
    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        snapshotFlow { query }
            .debounce(300)
            .distinctUntilChanged()
            .filter { it.length >= 2 }
            .collectLatest { term ->
                val request = FindAutocompletePredictionsRequest
                    .builder()
                    .setQuery(term)
                    .build()

                placesClient.findAutocompletePredictions(request)
                    .addOnSuccessListener { response ->
                        predictions = response.autocompletePredictions
                        expanded = predictions.isNotEmpty()
                    }
                    .addOnFailureListener {
                        predictions = emptyList()
                        expanded = false
                    }
            }
    }

    Column {
        BasicTextField(
            value = query,
            onValueChange = { query = it },
            singleLine = true,
            textStyle = TextStyle(fontSize = 14.sp),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(44.dp)
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp)
                )
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(start = 8.dp, end = 8.dp),  // vnútorný padding

            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search),
                        modifier = Modifier
                            .size(18.dp)
                            .padding(end = 6.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Box(Modifier.weight(1f)) {
                        if (query.isEmpty()) {
                            Text(
                                text = stringResource(R.string.find_place),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        innerTextField()
                    }
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            properties = PopupProperties(focusable = false),
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
        ) {
            predictions.forEach { prediction ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        query = ""
                        predictions = emptyList()

                        val placeId = prediction.placeId
                        val fields = listOf(
                            GooglePlace.Field.ID,
                            GooglePlace.Field.NAME,
                            GooglePlace.Field.ADDRESS,
                            GooglePlace.Field.ADDRESS_COMPONENTS,
                            GooglePlace.Field.LAT_LNG,
                            GooglePlace.Field.TYPES,
                            GooglePlace.Field.PHOTO_METADATAS
                        )

                        val request = FetchPlaceRequest.builder(placeId, fields).build()
                        placesClient.fetchPlace(request)
                            .addOnSuccessListener { response ->
                                val place = response.place

                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        val formattedPlace = formatPlaceDetails(placesClient, place)

                                        withContext(Dispatchers.Main) {
                                            onPlaceSelected(formattedPlace)
                                        }
                                    } catch (e: Exception) {
                                        Log.e("PlaceDetails", "Error formatting place details: ${e.message}")
                                    }
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.e("PlaceDetails", "Error fetching place: ${exception.message}")
                            }
                    },
                    text = {
                        Text(
                            text = prediction.getFullText(null).toString(),
                            style = TextStyle(fontSize = 14.sp),
                        )
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GoogleAutocompletePreview() {
    GoogleAutocomplete(
        onPlaceSelected = {}
    )
}
