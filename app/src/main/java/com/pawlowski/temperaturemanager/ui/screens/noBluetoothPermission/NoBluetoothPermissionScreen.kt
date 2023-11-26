package com.pawlowski.temperaturemanager.ui.screens.noBluetoothPermission

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.pawlowski.temperaturemanager.ui.utils.rememberBluetoothMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NoBluetoothPermissionScreen() {
    val bluetoothScanPermissionState = rememberBluetoothMultiplePermissionsState()

    Column {
        val textToShow = if (bluetoothScanPermissionState.shouldShowRationale) {
            "The bluetooth is important for this app. Please grant the permission."
        } else {
            "Bluetooth permission required for this feature to be available. " +
                "Please grant the permission"
        }
        Text(textToShow)
        Button(onClick = { bluetoothScanPermissionState.launchMultiplePermissionRequest() }) {
            Text("Request permission")
        }
    }
}
