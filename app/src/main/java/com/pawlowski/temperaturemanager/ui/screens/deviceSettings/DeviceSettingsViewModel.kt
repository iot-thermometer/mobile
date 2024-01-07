package com.pawlowski.temperaturemanager.ui.screens.deviceSettings

import androidx.lifecycle.viewModelScope
import com.pawlowski.temperaturemanager.BaseMviViewModel
import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.domain.getDataOrNull
import com.pawlowski.temperaturemanager.domain.resourceFlow
import com.pawlowski.temperaturemanager.domain.useCase.DeleteDeviceUseCase
import com.pawlowski.temperaturemanager.domain.useCase.DeviceSelectionUseCase
import com.pawlowski.temperaturemanager.domain.useCase.GetDeviceByIdUseCase
import com.pawlowski.temperaturemanager.domain.useCase.UpdateDeviceUseCase
import com.pawlowski.temperaturemanager.ui.navigation.Back
import com.pawlowski.temperaturemanager.ui.navigation.Screen.DeviceSettings.DeviceSettingsDirection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DeviceSettingsViewModel @Inject constructor(
    private val getDeviceByIdUseCase: GetDeviceByIdUseCase,
    deviceSelectionUseCase: DeviceSelectionUseCase,
    private val deleteDeviceUseCase: DeleteDeviceUseCase,
    private val updateDeviceUseCase: UpdateDeviceUseCase,
) : BaseMviViewModel<DeviceSettingsState, DeviceSettingsEvent, DeviceSettingsDirection>(
    initialState = DeviceSettingsState(
        deviceResource = Resource.Loading,
    ),
) {
    private val deviceId = deviceSelectionUseCase.getSelectedDeviceId()!!

    override fun initialised() {
        viewModelScope.launch {
            resourceFlow {
                getDeviceByIdUseCase(deviceId)
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
                if (!actualState.isLoading) {
                    pushNavigationEvent(Back)
                }
            }

            is DeviceSettingsEvent.DeleteDeviceClick -> {
                if (!actualState.isLoading) {
                    updateState {
                        copy(isLoading = true)
                    }
                    viewModelScope.launch {
                        kotlin.runCatching {
                            deleteDeviceUseCase(deviceId = deviceId)
                        }.onFailure {
                            ensureActive()
                            it.printStackTrace()
                        }.onSuccess {
                            pushNavigationEvent(DeviceSettingsDirection.HOME)
                        }
                        updateState {
                            copy(isLoading = false)
                        }
                    }
                }
            }

            is DeviceSettingsEvent.OnIntervalsChange -> {
                if (!actualState.isLoading) {
                    updateState {
                        copy(isLoading = true)
                    }
                    viewModelScope.launch {
                        actualState.deviceResource.getDataOrNull()?.let { currentDevice ->
                            runCatching {
                                updateDeviceUseCase(
                                    deviceId = deviceId,
                                    readingInterval = event.readingInterval,
                                    pushInterval = event.pushInterval,
                                    name = currentDevice.name,
                                )
                            }.onFailure {
                                ensureActive()
                                it.printStackTrace()
                            }.onSuccess {
                                updateState {
                                    copy(
                                        deviceResource = deviceResource.getDataOrNull()
                                            ?.let { device ->
                                                Resource.Success(
                                                    data = device.copy(
                                                        readingInterval = event.readingInterval,
                                                        pushInterval = event.pushInterval,
                                                    ),
                                                )
                                            } ?: deviceResource,
                                    )
                                }
                            }
                        }
                        updateState {
                            copy(isLoading = false)
                        }
                    }
                }
            }

            is DeviceSettingsEvent.OnNameChange -> {
                if (!actualState.isLoading) {
                    updateState {
                        copy(isLoading = true)
                    }
                    viewModelScope.launch {
                        actualState.deviceResource.getDataOrNull()?.let { currentDevice ->
                            runCatching {
                                updateDeviceUseCase(
                                    deviceId = deviceId,
                                    name = event.name,
                                    pushInterval = currentDevice.pushInterval,
                                    readingInterval = currentDevice.readingInterval,
                                )
                            }.onFailure {
                                ensureActive()
                                it.printStackTrace()
                            }.onSuccess {
                                updateState {
                                    copy(
                                        deviceResource = deviceResource.getDataOrNull()
                                            ?.let { device ->
                                                Resource.Success(
                                                    data = device.copy(name = event.name),
                                                )
                                            } ?: deviceResource,
                                    )
                                }
                                pushNavigationEvent(DeviceSettingsDirection.HOME)
                            }
                        }

                        updateState {
                            copy(isLoading = false)
                        }
                    }
                }
            }
        }
    }
}
