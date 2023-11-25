package com.pawlowski.temperaturemanager.ui.screens.searchDevices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pawlowski.temperaturemanager.domain.models.BluetoothDeviceAdvertisement

@Composable
fun SearchDevicesScreen(
    state: SearchDevicesState,
    onEvent: (SearchDevicesEvent) -> Unit,
) {
    Column {
        Text(text = "List of devices:")

        LazyColumn(
            modifier = Modifier
                .padding(top = 15.dp)
                .fillMaxWidth(),
        ) {
            items(state.devices) {
                AdvertisementItem(advertisement = it) {
                    onEvent(SearchDevicesEvent.DeviceClick(it))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdvertisementItem(
    advertisement: BluetoothDeviceAdvertisement,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.padding(all = 10.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(text = advertisement.name)
            Text(text = advertisement.macAddress)
        }
    }
}
