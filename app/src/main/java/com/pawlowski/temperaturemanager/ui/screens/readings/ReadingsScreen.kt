package com.pawlowski.temperaturemanager.ui.screens.readings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.pawlowski.temperaturemanager.domain.models.ReadingDomain

@Composable
fun ReadingsScreen(
    state: ReadingsState,
    onEvent: (ReadingsEvent) -> Unit,
) {
    when (state) {
        is ReadingsState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is ReadingsState.Content -> {
            LazyColumn {
                items(state.readings.toList()) { (day, readings) ->
                    ReadingsSection(
                        day = day,
                        readings = readings,
                    )
                }
            }
        }

        ReadingsState.Empty -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "You have got no readings!")
            }
        }

        ReadingsState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "Something went wrong!")
            }
        }
    }
}

@Composable
private fun ReadingsSection(
    day: String,
    readings: List<ReadingDomain>,
) {
    Column {
        Text(text = day)
        readings.forEach {
            ReadingsItem(reading = it)
        }
    }
}

@Composable
private fun ReadingsItem(
    reading: ReadingDomain,
) {
    Column(modifier = Modifier.background(color = Color.Blue)) {
        Text(text = reading.temperature.toString())
        Text(text = reading.soilMoisture.toString())
    }
}
