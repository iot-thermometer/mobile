package com.pawlowski.temperaturemanager.ui.screens.deviceSettings

import androidx.lifecycle.viewModelScope
import com.pawlowski.temperaturemanager.BaseMviViewModel
import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.domain.resourceFlow
import com.pawlowski.temperaturemanager.domain.useCase.GetDeviceByIdUseCase
import com.pawlowski.temperaturemanager.ui.navigation.Back
import com.pawlowski.temperaturemanager.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DeviceSettingsViewModel @Inject constructor(
    private val getDeviceByIdUseCase: GetDeviceByIdUseCase,
) : BaseMviViewModel<DeviceSettingsState, DeviceSettingsEvent, Screen.DeviceSettings.DeviceSettingsDirection>(
    initialState = DeviceSettingsState(
        deviceResource = Resource.Loading,
    ),
) {

    override fun initialised() {
        viewModelScope.launch {
            resourceFlow {
                getDeviceByIdUseCase(1) // TODO
            }.collect { deviceResource ->
                updateState {
                    copy(deviceResource = deviceResource)
                }
            }
        }
    }

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
