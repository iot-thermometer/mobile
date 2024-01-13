package com.pawlowski.temperaturemanager.ui.screens.deviceSettings

import androidx.lifecycle.viewModelScope
import com.pawlowski.temperaturemanager.BaseMviViewModel
import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.domain.RetrySharedFlow
import com.pawlowski.temperaturemanager.domain.getDataOrNull
import com.pawlowski.temperaturemanager.domain.resourceFlowWithRetrying
import com.pawlowski.temperaturemanager.domain.useCase.devices.DeleteDeviceUseCase
import com.pawlowski.temperaturemanager.domain.useCase.devices.DeviceSelectionUseCase
import com.pawlowski.temperaturemanager.domain.useCase.devices.GetDeviceByIdUseCase
import com.pawlowski.temperaturemanager.domain.useCase.devices.UpdateDeviceUseCase
import com.pawlowski.temperaturemanager.ui.navigation.Back
import com.pawlowski.temperaturemanager.ui.navigation.Screen.DeviceSettings.DeviceSettingsDirection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DeviceSettingsViewModel
    @Inject
    constructor(
        private val getDeviceByIdUseCase: GetDeviceByIdUseCase,
        deviceSelectionUseCase: DeviceSelectionUseCase,
        private val deleteDeviceUseCase: DeleteDeviceUseCase,
        private val updateDeviceUseCase: UpdateDeviceUseCase,
    ) : BaseMviViewModel<DeviceSettingsState, DeviceSettingsEvent, DeviceSettingsDirection>(
            initialState =
                DeviceSettingsState(
                    deviceResource = Resource.Loading,
                ),
        ) {
        private val deviceId = deviceSelectionUseCase.getSelectedDeviceId()!!
        private val retrySharedFlow = RetrySharedFlow()

        override fun initialised() {
            viewModelScope.launch {
                resourceFlowWithRetrying(retrySharedFlow = retrySharedFlow) {
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
                            resourceFlowWithRetrying(retrySharedFlow = retrySharedFlow) {
                                deleteDeviceUseCase(deviceId = deviceId)
                            }.collect {
                                when (it) {
                                    is Resource.Success -> {
                                        pushNavigationEvent(DeviceSettingsDirection.HOME)
                                    }

                                    is Resource.Loading -> {
                                        updateState {
                                            copy(
                                                isLoading = true,
                                                isActionError = false,
                                            )
                                        }
                                    }

                                    is Resource.Error -> {
                                        updateState {
                                            copy(
                                                isLoading = false,
                                                isActionError = true,
                                            )
                                        }
                                    }
                                }
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
                                resourceFlowWithRetrying(retrySharedFlow = retrySharedFlow) {
                                    updateDeviceUseCase(
                                        deviceId = deviceId,
                                        readingInterval = event.readingInterval,
                                        pushInterval = event.pushInterval,
                                        name = currentDevice.name,
                                    )
                                }.collect {
                                    when (it) {
                                        is Resource.Success -> {
                                            updateState {
                                                copy(
                                                    deviceResource =
                                                        deviceResource.getDataOrNull()
                                                            ?.let { device ->
                                                                Resource.Success(
                                                                    data =
                                                                        device.copy(
                                                                            readingInterval = event.readingInterval,
                                                                            pushInterval = event.pushInterval,
                                                                        ),
                                                                )
                                                            } ?: deviceResource,
                                                    isLoading = false,
                                                    isActionError = false,
                                                )
                                            }
                                        }

                                        is Resource.Error -> {
                                            updateState {
                                                copy(isLoading = false, isActionError = true)
                                            }
                                        }

                                        is Resource.Loading -> {
                                            updateState {
                                                copy(isLoading = true, isActionError = false)
                                            }
                                        }
                                    }
                                }
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
                                resourceFlowWithRetrying(retrySharedFlow = retrySharedFlow) {
                                    updateDeviceUseCase(
                                        deviceId = deviceId,
                                        name = event.name,
                                        pushInterval = currentDevice.pushInterval,
                                        readingInterval = currentDevice.readingInterval,
                                    )
                                }.collect {
                                    when (it) {
                                        is Resource.Success -> {
                                            updateState {
                                                copy(
                                                    deviceResource =
                                                        deviceResource.getDataOrNull()
                                                            ?.let { device ->
                                                                Resource.Success(
                                                                    data = device.copy(name = event.name),
                                                                )
                                                            } ?: deviceResource,
                                                    isActionError = false,
                                                    isLoading = false,
                                                )
                                            }
                                            pushNavigationEvent(DeviceSettingsDirection.HOME)
                                        }

                                        is Resource.Loading -> {
                                            updateState {
                                                copy(isLoading = true, isActionError = false)
                                            }
                                        }

                                        is Resource.Error -> {
                                            updateState {
                                                copy(isActionError = true, isLoading = false)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                DeviceSettingsEvent.AlertsClick -> {
                    pushNavigationEvent(DeviceSettingsDirection.ALERTS)
                }

                DeviceSettingsEvent.RetryClick -> {
                    retrySharedFlow.sendRetryEvent()
                }
            }
        }
    }
