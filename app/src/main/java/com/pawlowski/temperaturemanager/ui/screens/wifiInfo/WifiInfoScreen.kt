package com.pawlowski.temperaturemanager.ui.screens.wifiInfo

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun WifiInfoScreen(
    state: WifiInfoState,
    onEvent: (WifiInfoEvent) -> Unit,
) {
    Text(text = "Wifi info")
}
