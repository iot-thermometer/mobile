package com.pawlowski.temperaturemanager.ui.screens.readings

import androidx.lifecycle.viewModelScope
import com.pawlowski.temperaturemanager.BaseMviViewModel
import com.pawlowski.temperaturemanager.domain.useCase.devices.DeviceSelectionUseCase
import com.pawlowski.temperaturemanager.domain.useCase.readings.GetReadingsUseCase
import com.pawlowski.temperaturemanager.ui.navigation.Back
import com.pawlowski.temperaturemanager.ui.navigation.Screen.Readings.ReadingsDirection
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
        private val deviceSelectionUseCase: DeviceSelectionUseCase,
    ) :
    BaseMviViewModel<ReadingsState, ReadingsEvent, ReadingsDirection>(
            initialState = ReadingsState.Loading,
        ) {
        override fun initialised() {
            viewModelScope.launch {
                // TODO: Change to AssistedInject
                val selectedDeviceId = deviceSelectionUseCase.getSelectedDeviceId()!!

                runCatching {
                    getReadingsUseCase.invoke(deviceId = selectedDeviceId)
                }.onFailure {
                    ensureActive()
                    updateState {
                        ReadingsState.Error
                    }
                }.onSuccess { readings ->
                    if (readings.isNotEmpty()) {
                        val lastReading =
                            readings.maxBy {
                                it.measuredAt
                            }
                        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
                        updateState {
                            ReadingsState.Content(
                                lastTemperature = lastReading.temperature.toInt(),
                                lastSoilMoisture = lastReading.soilMoisture.toInt(),
                                readings =
                                    readings.sortedByDescending {
                                        it.measuredAt
                                    }.groupBy {
                                        dateFormat.format(Date(it.measuredAt))
                                    },
                            )
                        }
                    } else {
                        updateState {
                            ReadingsState.Empty
                        }
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
            }
        }
    }
