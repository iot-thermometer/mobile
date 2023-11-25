package com.pawlowski.temperaturemanager.ui.screens.wifiInfo

import com.pawlowski.temperaturemanager.domain.models.BluetoothDeviceAdvertisement

sealed interface WifiInfoState {
    object Initialising : WifiInfoState
    data class Content(
        val chosenAdvertisement: BluetoothDeviceAdvertisement,
        val ssidInput: String = "",
        val ssidError: String? = null,
        val passwordInput: String = "",
        val passwordError: String? = null,
        val isLoading: Boolean = false,
        val pairingError: String? = null,
    ) : WifiInfoState
}

sealed interface WifiInfoEvent {

    object ContinueClick : WifiInfoEvent

    data class ChangeSsid(val newSsid: String) : WifiInfoEvent

    data class ChangePassword(val newPassword: String) : WifiInfoEvent

    object BackClick : WifiInfoEvent
}
