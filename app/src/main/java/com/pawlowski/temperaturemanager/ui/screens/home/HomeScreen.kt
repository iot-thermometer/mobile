package com.pawlowski.temperaturemanager.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pawlowski.temperaturemanager.domain.Resource

@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        when (state.devicesOverviewResource) {
            is Resource.Success -> {
                val devices = state.devicesOverviewResource.data

                LazyColumn(modifier = Modifier.weight(weight = 1f)) {
                    items(devices) {
                        Text(text = "DeviceName ${it.device.name}")
                    }
                }
            }

            is Resource.Loading -> {
                CircularProgressIndicator()
            }

            is Resource.Error -> {
                Text(text = "Error")
            }
        }

        Button(
            onClick = {
                onEvent(HomeEvent.AddNewDeviceClick)
            },
        ) {
            Text(text = "Paruj nowe")
        }
    }
}
