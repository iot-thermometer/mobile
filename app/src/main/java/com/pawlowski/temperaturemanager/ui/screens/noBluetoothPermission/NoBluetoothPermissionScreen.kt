package com.pawlowski.temperaturemanager.ui.screens.noBluetoothPermission

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NoBluetoothPermissionScreen() {
    // Camera permission state
    val bluetoothScanPermissionState = rememberPermissionState(
        Manifest.permission.BLUETOOTH_SCAN,
    )

    if (bluetoothScanPermissionState.status.isGranted) {
        Column {
            if (true) {
                Text("Bluetooth permission Granted")
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
