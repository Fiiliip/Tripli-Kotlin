package com.example.tripli.ui.itinerary._components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tripli.data.model.itinerary.AdditionalInformation
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.tripli.R

@Composable
fun AdditionalInformation(
    additionalInformation: AdditionalInformation,
    editMode: Boolean,
    onRemove: (Int) -> Unit,
    onTypeChanged: (String) -> Unit,
    onValueChanged: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(0.5f)) {
            TextField(
                value = additionalInformation.type,
                onValueChange = { onTypeChanged(it) },
                label = { Text(stringResource(R.string.type)) },
                enabled = editMode,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledTextColor  = Color.Black,
                    disabledLabelColor  = Color.Black,
                ),
            )
        }

        Column(modifier = Modifier.weight(0.5f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = additionalInformation.value,
                    onValueChange = { onValueChanged(it) },
                    label = { Text(stringResource(R.string.value)) },
                    enabled = editMode,
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        disabledTextColor  = Color.Black,
                        disabledLabelColor  = Color.Black,
                    )
                )

                if (additionalInformation.type.equals(stringResource(R.string.price), ignoreCase = true)) {
                    Text(
                        text = "€",
                        modifier = Modifier
                            .padding(start = 4.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        if (editMode) {
            IconButton(
                onClick = { onRemove(additionalInformation.order) },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.remove),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdditionalInformationPreview() {
    AdditionalInformation(
        additionalInformation = AdditionalInformation(type = "Price", value = "100"),
        editMode = true,
        onRemove = {},
        onValueChanged = {},
        onTypeChanged = {}
    )
}


