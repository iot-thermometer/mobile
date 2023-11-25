package com.pawlowski.temperaturemanager.ui.screens.searchDevices

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.pawlowski.temperaturemanager.domain.models.BluetoothDeviceAdvertisement

@Composable
fun SearchDevicesScreen(
    state: SearchDevicesState,
    onEvent: (SearchDevicesEvent) -> Unit,
) {
    FeatureThatRequiresBluetoothPermission(state = state, onEvent = onEvent)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun FeatureThatRequiresBluetoothPermission(
    state: SearchDevicesState,
    onEvent: (SearchDevicesEvent) -> Unit,
) {
    // Camera permission state
    val bluetoothScanPermissionState = rememberPermissionState(
        Manifest.permission.BLUETOOTH_SCAN,
    )

    if (bluetoothScanPermissionState.status.isGranted) {
        Column {
            if (!state.isPairingInProgress) {
                Text("Bluetooth permission Granted")
                ScanningDevices(state = state, onEvent = onEvent)
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    } else {
        Column {
            val textToShow = if (bluetoothScanPermissionState.status.shouldShowRationale) {
                // If the user has denied the permission but the rationale can be shown,
                // then gently explain why the app requires this permission
                "The bluetooth scan is important for this app. Please grant the permission."
            } else {
                // If it's the first time the user lands on this feature, or the user
                // doesn't want to be asked again for this permission, explain that the
                // permission is required
                "Bluetooth scan permission required for this feature to be available. " +
                    "Please grant the permission"
            }
            Text(textToShow)
            Button(onClick = { bluetoothScanPermissionState.launchPermissionRequest() }) {
                Text("Request permission")
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ScanningDevices(
    state: SearchDevicesState,
    onEvent: (SearchDevicesEvent) -> Unit,
) {
    Column {
        val bluetoothConnectPermissionState = rememberPermissionState(
            Manifest.permission.BLUETOOTH_CONNECT,
        )
        Text(text = "List of devices:")

        LazyColumn(
            modifier = Modifier
                .padding(top = 15.dp)
                .fillMaxWidth(),
        ) {
            items(state.devices) {
                AdvertisementItem(advertisement = it) {
                    if (!bluetoothConnectPermissionState.status.isGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        bluetoothConnectPermissionState.launchPermissionRequest()
                    } else {
                        onEvent(SearchDevicesEvent.DeviceClick(it))
                    }
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
