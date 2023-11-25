package com.pawlowski.temperaturemanager.ui.screens.searchDevices

import com.pawlowski.temperaturemanager.domain.models.BluetoothDeviceAdvertisement

data class SearchDevicesState(
    val devices: List<BluetoothDeviceAdvertisement>,
    val isPairingInProgress: Boolean,
)

sealed interface SearchDevicesEvent {

    data class DeviceClick(val advertisement: BluetoothDeviceAdvertisement) : SearchDevicesEvent
}
