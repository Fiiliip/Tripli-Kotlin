package com.example.tripli.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripli.R
import com.example.tripli.data.model.User
import com.example.tripli.data.model.itinerary.Itinerary
import com.example.tripli.ui._components.ItineraryItem

@Composable
fun ProfileContent(
    uiState: ProfileUiState,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onUsernameChange: (String) -> Unit,
    onItineraryClick: (Int) -> Unit,
    onSaveChangesClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 20.dp)
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back_description),
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
            Text(
                text = stringResource(R.string.log_out),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onLogoutClick() }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 7.dp, end = 26.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = uiState.user.username,
                    onValueChange = onUsernameChange,
                    modifier = Modifier.weight(1f),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    ),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                    )
                )
                if (uiState.usernameChanged) {
                    Column(
                        modifier = Modifier.clickable { onSaveChangesClick() },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = stringResource(R.string.save_username),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = stringResource(R.string.save),
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            Text(
                text = uiState.user.email,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.your_itineraries),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(start = 20.dp)
        )

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            if (uiState.itineraries.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_itineraries_found),
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                }
            } else {
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
                            onClick = { onItineraryClick(itinerary.id) }
                        )
                    }
                }
            }
        }
    }
}

@Preview (showBackground = true)
@Composable
fun ProfileContentPreview() {
    ProfileContent(
        uiState = ProfileUiState(
            user = User(id = "1", username = "Fiiliip", email = "fiiliip@tripli.sk"),
            itineraries = listOf(
                Itinerary(id = 1, title = "Trip to Paris", description = "A trip to Paris with friends"),
                Itinerary(id = 2, title = "Trip to Rome", description = "A trip to Rome with family")
            ),
            usernameChanged = true,
        ),
        onBackClick = {},
        onLogoutClick = {},
        onUsernameChange = {},
        onItineraryClick = {},
        onSaveChangesClick = {}
    )
}