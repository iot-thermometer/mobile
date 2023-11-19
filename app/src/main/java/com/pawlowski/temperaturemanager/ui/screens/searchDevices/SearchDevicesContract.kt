package com.pawlowski.temperaturemanager.ui.screens.searchDevices

import com.juul.kable.AndroidAdvertisement

data class SearchDevicesState(
    val devices: List<AndroidAdvertisement>,
    val isPairingInProgress: Boolean,
)

sealed interface SearchDevicesEvent {

    data class DeviceClick(val advertisement: AndroidAdvertisement) : SearchDevicesEvent
}
