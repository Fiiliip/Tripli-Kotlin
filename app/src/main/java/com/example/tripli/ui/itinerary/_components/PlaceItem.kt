package com.example.tripli.ui.itinerary._components

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.tripli.R
import com.example.tripli.data.model.itinerary.LatLng
import com.example.tripli.data.model.itinerary.Place

@Composable
fun PlaceItem(
    place: Place,
    editMode: Boolean,
    onClick: () -> Unit = {},
    onRemoveClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .padding(top = 8.dp, end = 8.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
                clip = false
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Row {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.25f)
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
            ) {
                val imageUrl = place.images?.firstOrNull()?.url
                if (imageUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = place.title,
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
                    .weight(0.75f)
                    .padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = place.title,
                        fontWeight = FontWeight.Bold
                    )
                    if (editMode) {
                        IconButton(onClick = { onRemoveClick(place.id) }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.remove_place),
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                }

                Text(
                    text = place.description ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlaceItemPreview() {
    PlaceItem(
        place = Place(
            id = "1",
            title = "Sample Place",
            description = "This is a sample place description.",
            location = LatLng(48.1482, 17.1067),
            addressComponents = emptyList(),
            day = 1,
            order = 1,
            images = listOf(
                com.example.tripli.data.model.itinerary.Image("https://example.com/image1.jpg", 1)
            ),
            additionalInformations = emptyList()
        ),
        editMode = true,
        onRemoveClick = {}
    )
}