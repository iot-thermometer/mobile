package com.pawlowski.temperaturemanager.ui.screens.bluetoothOff

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pawlowski.temperaturemanager.ui.components.Toolbar

@Composable
fun BluetoothOffScreen(onBackClick: () -> Unit) {
    Column {
        Toolbar(
            leading = Toolbar.ToolbarLeading.Back(
                onClick = {
                    onBackClick()
                },
            ),
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "Bluetooth is turned off")
        }
    }
}
