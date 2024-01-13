package com.pawlowski.temperaturemanager.ui.screens.deviceSettings

import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.domain.models.DeviceDomain

data class DeviceSettingsState(
    val deviceResource: Resource<DeviceDomain>,
    val isLoading: Boolean = false,
    val isActionError: Boolean = false,
)

sealed interface DeviceSettingsEvent {
    object BackClick : DeviceSettingsEvent

    object RetryClick : DeviceSettingsEvent

    object DeleteDeviceClick : DeviceSettingsEvent

    data class OnIntervalsChange(
        val readingInterval: Long,
        val pushInterval: Long,
    ) : DeviceSettingsEvent

    data class OnNameChange(
        val name: String,
    ) : DeviceSettingsEvent

    object AlertsClick : DeviceSettingsEvent
}
