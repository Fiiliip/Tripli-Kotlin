package com.example.tripli.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tripli.R
import com.example.tripli.data.model.itinerary.AdditionalInformation
import com.example.tripli.data.model.itinerary.AddressComponent
import com.example.tripli.data.model.itinerary.Image
import com.example.tripli.data.model.itinerary.Itinerary
import com.example.tripli.data.model.itinerary.LatLng
import com.example.tripli.data.model.itinerary.Place
import com.example.tripli.data.model.User
import com.example.tripli.ui._components.ItineraryItem
import com.example.tripli.ui.home._components.SearchBar

@Composable
fun HomeContent(
    uiState: HomeUiState,
    onSearch: (String) -> Unit = {},
    onFilterClick: () -> Unit = {},
    onItineraryClick: (Itinerary) -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onFilterClick) {
                Icon(Icons.Default.FilterList, contentDescription = stringResource(R.string.filter))
            }
            Text(
                text = "Home",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 28.dp, end = 28.dp, top = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                onSearch = onSearch
            )
        }

        when {
            uiState.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            uiState.itineraries.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.no_itineraries_found),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary)
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(uiState.itineraries) { itinerary ->
                        ItineraryItem(
                            itinerary = itinerary,
                            onClick = { onItineraryClick(itinerary) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeContentPreview() {
    val mockUiState = HomeUiState(
        isLoading = false,
        itineraries = listOf(
            Itinerary(
                id = 1,
                title = "Výlet do Tatier",
                description = "Nádherná túra po Tatrách s výhľadmi a horskými chatami.",
                owner = User(
                    id = "u1",
                    username = "peter123",
                    email = "peter@example.com"
                ),
                category = 1,
                places = listOf(
                    Place(
                        id = "p1",
                        google_place_id = "p1",
                        title = "Štrbské Pleso",
                        description = "Jazero v horách.",
                        location = LatLng(49.118, 20.059),
                        latitude = 49.118,
                        longitude = 20.059,
                        addressComponents = listOf(
                            AddressComponent(1, "Štrbské Pleso", "ŠP", "locality")
                        ),
                        day = 1,
                        order = 1,
                        images = listOf(
                            Image("https://example.com/image1.jpg", 1)
                        ),
                        additionalInformations = listOf(
                            AdditionalInformation("note", "Zastávka na obed", 1)
                        )
                    )
                ),
                additionalInformations = listOf(
                    AdditionalInformation("weather", "slnečno", 1)
                ),
                rating = 4.5f
            ),
            Itinerary(
                id = 2,
                title = "Historická Bratislava",
                description = "Prechádzka mestom s návštevou hradu a starého mesta.",
                owner = User(
                    id = "u2",
                    username = "jana_k",
                    email = "jana@example.com"
                ),
                category = 2,
                places = listOf(
                    Place(
                        id = "p2",
                        google_place_id = "p2",
                        title = "Bratislavský hrad",
                        description = "Dominanta hlavného mesta.",
                        location = LatLng(48.141, 17.100),
                        latitude = 48.141,
                        longitude = 17.100,
                        addressComponents = listOf(
                            AddressComponent(2, "Bratislava", "BA", "locality")
                        ),
                        day = 1,
                        order = 1,
                        images = listOf(
                            Image("https://example.com/image2.jpg", 1)
                        ),
                        additionalInformations = listOf(
                            AdditionalInformation("entry", "vstupné 10€", 1)
                        )
                    )
                ),
                additionalInformations = emptyList(),
                rating = 4.8f
            )
        )
    )

    HomeContent(
        uiState = mockUiState,
        onSearch = {},
        onFilterClick = {},
        onItineraryClick = {}
    )
}
