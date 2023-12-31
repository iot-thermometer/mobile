package com.pawlowski.temperaturemanager.ui.screens.deviceSettings

import com.pawlowski.temperaturemanager.BaseMviViewModel
import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.ui.navigation.Back
import com.pawlowski.temperaturemanager.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class DeviceSettingsViewModel @Inject constructor() : BaseMviViewModel<DeviceSettingsState, DeviceSettingsEvent, Screen.DeviceSettings.DeviceSettingsDirection>(
    initialState = DeviceSettingsState(
        deviceResource = Resource.Loading,
    ),
) {

    override fun onNewEvent(event: DeviceSettingsEvent) {
        when (event) {
            is DeviceSettingsEvent.BackClick -> {
                pushNavigationEvent(Back)
            }

            is DeviceSettingsEvent.DeleteDeviceClick -> {
            }
        }
    }
}
