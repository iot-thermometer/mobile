package com.pawlowski.temperaturemanager.ui.screens.alerts

import androidx.lifecycle.viewModelScope
import com.pawlowski.temperaturemanager.BaseMviViewModel
import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.domain.RetrySharedFlow
import com.pawlowski.temperaturemanager.domain.getDataOrNull
import com.pawlowski.temperaturemanager.domain.resourceFlowWithRetrying
import com.pawlowski.temperaturemanager.domain.useCase.alerts.AddAlertUseCase
import com.pawlowski.temperaturemanager.domain.useCase.alerts.DeleteAlertUseCase
import com.pawlowski.temperaturemanager.domain.useCase.alerts.GetAlertsUseCase
import com.pawlowski.temperaturemanager.domain.useCase.devices.DeviceSelectionUseCase
import com.pawlowski.temperaturemanager.ui.navigation.Back
import com.pawlowski.temperaturemanager.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AlertsViewModel
    @Inject
    constructor(
        private val getAlertsUseCase: GetAlertsUseCase,
        private val addAlertUseCase: AddAlertUseCase,
        private val deleteAlertUseCase: DeleteAlertUseCase,
        deviceSelectionUseCase: DeviceSelectionUseCase,
    ) :
    BaseMviViewModel<AlertsState, AlertsEvent, Screen.Alerts.AlertsDirection>(
            initialState =
                AlertsState(
                    alertsResource = Resource.Loading,
                ),
        ) {
        private val deviceId = deviceSelectionUseCase.getSelectedDeviceId()!!
        private val retrySharedFlow = RetrySharedFlow()

        override fun initialised() {
            viewModelScope.launch {
                resourceFlowWithRetrying(retrySharedFlow = retrySharedFlow) {
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
                    if (actualState.actionResource != null) {
                        return
                    }

                    viewModelScope.launch {
                        resourceFlowWithRetrying(retrySharedFlow = retrySharedFlow) {
                            addAlertUseCase(
                                deviceId = deviceId,
                                minTemp = event.minTemp,
                                maxTemp = event.maxTemp,
                                minSoil = event.minSoil,
                                maxSoil = event.maxSoil,
                                name = event.name,
                            )

                            getAlertsUseCase(deviceId = deviceId).also {
                                updateState {
                                    copy(alertsResource = Resource.Success(it))
                                }
                            }
                            Unit
                        }.collect {
                            updateState {
                                copy(
                                    actionResource =
                                        if (it is Resource.Success) {
                                            null
                                        } else {
                                            it
                                        },
                                )
                            }
                        }
                    }
                }

                is AlertsEvent.DeleteAlert -> {
                    if (actualState.actionResource != null) {
                        return
                    }
                    viewModelScope.launch {
                        resourceFlowWithRetrying(retrySharedFlow = retrySharedFlow) {
                            deleteAlertUseCase(alertId = event.alertId)
                        }.collect {
                            updateState {
                                if (it is Resource.Success) {
                                    copy(
                                        actionResource = null,
                                        alertsResource =
                                            alertsResource.getDataOrNull()
                                                ?.let { previousAlerts ->
                                                    Resource.Success(
                                                        previousAlerts.filter {
                                                            it.id != event.alertId
                                                        },
                                                    )
                                                } ?: alertsResource,
                                    )
                                } else {
                                    copy(actionResource = it)
                                }
                            }
                        }
                    }
                }

                AlertsEvent.RetryClick -> {
                    retrySharedFlow.sendRetryEvent()
                }
            }
        }
    }
