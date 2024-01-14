package com.pawlowski.temperaturemanager.ui.screens.readings

import androidx.lifecycle.viewModelScope
import com.pawlowski.temperaturemanager.BaseMviViewModel
import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.domain.RetrySharedFlow
import com.pawlowski.temperaturemanager.domain.resourceFlowWithRetrying
import com.pawlowski.temperaturemanager.domain.useCase.devices.DeviceSelectionUseCase
import com.pawlowski.temperaturemanager.domain.useCase.members.AmIOwnerOfDeviceUseCase
import com.pawlowski.temperaturemanager.domain.useCase.readings.GetReadingsUseCase
import com.pawlowski.temperaturemanager.ui.navigation.Back
import com.pawlowski.temperaturemanager.ui.navigation.Screen.Readings.ReadingsDirection
import com.pawlowski.temperaturemanager.ui.screens.readings.ReadingsState.ContentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
internal class ReadingsViewModel
    @Inject
    constructor(
        private val getReadingsUseCase: GetReadingsUseCase,
        deviceSelectionUseCase: DeviceSelectionUseCase,
        private val amIOwnerOfDeviceUseCase: AmIOwnerOfDeviceUseCase,
    ) :
    BaseMviViewModel<ReadingsState, ReadingsEvent, ReadingsDirection>(
            initialState =
                ReadingsState(
                    contentState = ContentState.Loading,
                    amIOwner = false,
                ),
        ) {
        private val retrySharedFlow = RetrySharedFlow()
        private val selectedDeviceId = deviceSelectionUseCase.getSelectedDeviceId()!!

        override fun initialised() {
            viewModelScope.launch {
                // TODO: Change to AssistedInject

                resourceFlowWithRetrying(retrySharedFlow = retrySharedFlow) {
                    getReadingsUseCase.invoke(deviceId = selectedDeviceId)
                }.collect {
                    when (it) {
                        is Resource.Loading -> {
                            updateState {
                                copy(contentState = ContentState.Loading)
                            }
                        }

                        is Resource.Error -> {
                            updateState {
                                copy(contentState = ContentState.Error)
                            }
                        }

                        is Resource.Success -> {
                            val readings = it.data
                            if (readings.isNotEmpty()) {
                                val lastReading =
                                    readings.maxBy {
                                        it.measuredAt
                                    }
                                val dateFormat = SimpleDateFormat("dd.MM.yyyy")
                                updateState {
                                    copy(
                                        contentState =
                                            ContentState.Content(
                                                lastTemperature = lastReading.temperature?.toInt(),
                                                lastSoilMoisture = lastReading.soilMoisture?.toInt(),
                                                readings =
                                                    readings.sortedByDescending {
                                                        it.measuredAt
                                                    }.groupBy {
                                                        dateFormat.format(Date(it.measuredAt))
                                                    },
                                            ),
                                    )
                                }
                            } else {
                                updateState {
                                    copy(contentState = ContentState.Empty)
                                }
                            }
                        }
                    }
                }
            }

            viewModelScope.launch {
                runCatching {
                    amIOwnerOfDeviceUseCase(deviceId = selectedDeviceId)
                }.onFailure {
                    ensureActive()
                    it.printStackTrace()
                }.onSuccess {
                    updateState {
                        copy(amIOwner = it)
                    }
                }
            }
        }

        override fun onNewEvent(event: ReadingsEvent) {
            when (event) {
                ReadingsEvent.BackClick -> {
                    pushNavigationEvent(Back)
                }

                ReadingsEvent.SettingsClick -> {
                    pushNavigationEvent(ReadingsDirection.DEVICE_SETTINGS)
                }

                ReadingsEvent.RetryClick -> {
                    retrySharedFlow.sendRetryEvent()
                }

                ReadingsEvent.AlertsClick -> {
                    pushNavigationEvent(ReadingsDirection.ALERTS)
                }
            }
        }
    }
