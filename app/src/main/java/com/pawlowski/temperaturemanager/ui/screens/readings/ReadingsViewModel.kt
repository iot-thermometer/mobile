package com.pawlowski.temperaturemanager.ui.screens.readings

import androidx.lifecycle.viewModelScope
import com.pawlowski.temperaturemanager.BaseMviViewModel
import com.pawlowski.temperaturemanager.domain.useCase.DeviceSelectionUseCase
import com.pawlowski.temperaturemanager.domain.useCase.GetReadingsUseCase
import com.pawlowski.temperaturemanager.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
internal class ReadingsViewModel @Inject constructor(
    private val getReadingsUseCase: GetReadingsUseCase,
    private val deviceSelectionUseCase: DeviceSelectionUseCase,
) :
    BaseMviViewModel<ReadingsState, ReadingsEvent, Screen.Readings.ReadingsDirection>(
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
                    val lastReading = readings.maxBy {
                        it.measuredAt
                    }
                    updateState {
                        ReadingsState.Content(
                            lastTemperature = lastReading.temperature.toInt(),
                            lastSoilMoisture = lastReading.soilMoisture.toInt(),
                            readings = readings.groupBy {
                                SimpleDateFormat("dd.MM.yyyy").format(Date(it.measuredAt))
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
    }
}
