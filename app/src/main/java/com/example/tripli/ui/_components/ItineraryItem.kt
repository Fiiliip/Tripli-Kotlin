package com.example.tripli.ui._components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.tripli.R
import com.example.tripli.data.model.User
import com.example.tripli.data.model.itinerary.AdditionalInformation
import com.example.tripli.data.model.itinerary.AddressComponent
import com.example.tripli.data.model.itinerary.Itinerary
import com.example.tripli.data.model.itinerary.LatLng
import com.example.tripli.data.model.itinerary.Place
import com.example.tripli.di.StringProvider

@Composable
fun ItineraryItem(
    itinerary: Itinerary,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .height(96.dp)
            .padding(end = 4.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
                clip = false
            )
            .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
            .background(Color.White)
            .clickable { onClick() }
    ) {
        Row {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
            ) {
                val imageUrl = itinerary.places
                    .firstOrNull { !it.images.isNullOrEmpty() }
                    ?.images
                    ?.firstOrNull()
                    ?.url

                if (imageUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = itinerary.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray.copy(alpha = 0.4f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.ImageNotSupported,
                            contentDescription = stringResource(R.string.image_not_found),
                            modifier = Modifier
                                .fillMaxSize(0.5f)
                                .align(Alignment.Center),
                            tint = Color.White
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(3f)
                    .padding(12.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = itinerary.title,
                    style = MaterialTheme.typography.titleMedium
                )
                // Spacer(modifier = Modifier.height(4.dp))
                // Text(
                //     text = itinerary.description,
                //     style = MaterialTheme.typography.bodySmall,
                //     maxLines = 2,
                //     overflow = TextOverflow.Ellipsis
                // )
            }
        }
    }
}

@Preview
@Composable
fun ItineraryItemPreview() {
    ItineraryItem(
        itinerary = Itinerary(
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
                    images = emptyList(),
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
        onClick = {}
    )
}
