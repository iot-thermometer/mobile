package com.pawlowski.temperaturemanager.ui.screens.alerts

import androidx.lifecycle.viewModelScope
import com.pawlowski.temperaturemanager.BaseMviViewModel
import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.domain.resourceFlow
import com.pawlowski.temperaturemanager.domain.useCase.AddAlertUseCase
import com.pawlowski.temperaturemanager.domain.useCase.DeviceSelectionUseCase
import com.pawlowski.temperaturemanager.domain.useCase.GetAlertsUseCase
import com.pawlowski.temperaturemanager.ui.navigation.Back
import com.pawlowski.temperaturemanager.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AlertsViewModel @Inject constructor(
    private val getAlertsUseCase: GetAlertsUseCase,
    private val addAlertUseCase: AddAlertUseCase,
    deviceSelectionUseCase: DeviceSelectionUseCase,
) :
    BaseMviViewModel<AlertsState, AlertsEvent, Screen.Alerts.AlertsDirection>(
        initialState = AlertsState(
            alertsResource = Resource.Loading,
        ),
    ) {

    private val deviceId = deviceSelectionUseCase.getSelectedDeviceId()!!

    override fun initialised() {
        viewModelScope.launch {
            resourceFlow {
                getAlertsUseCase(deviceId = deviceId)
            }.collectLatest {
                updateState {
                    copy(alertsResource = it)
                }
            }
        }
    }

    override fun onNewEvent(event: AlertsEvent) {
        when (event) {
            is AlertsEvent.OnBackClick -> {
                pushNavigationEvent(Back)
            }

            is AlertsEvent.OnAddAlert -> {
                viewModelScope.launch {
                    kotlin.runCatching {
                        addAlertUseCase(
                            deviceId = deviceId,
                            minTemp = event.minTemp,
                            maxTemp = event.maxTemp,
                            minSoil = event.minSoil,
                            maxSoil = event.maxSoil,
                            name = "Alert2",
                        )
                    }.onFailure {
                        ensureActive()
                        it.printStackTrace()
                    }.onSuccess {
                    }
                }
            }
        }
    }
}
