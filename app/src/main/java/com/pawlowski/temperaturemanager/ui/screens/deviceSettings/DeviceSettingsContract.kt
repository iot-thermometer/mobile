package com.pawlowski.temperaturemanager.ui.screens.deviceSettings

import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.domain.models.DeviceDomain

data class DeviceSettingsState(
    val deviceResource: Resource<DeviceDomain>,
)

sealed interface DeviceSettingsEvent {

    object BackClick : DeviceSettingsEvent

    object DeleteDeviceClick : DeviceSettingsEvent
}
