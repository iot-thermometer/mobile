package com.pawlowski.temperaturemanager.ui.screens.alerts

import androidx.lifecycle.viewModelScope
import com.pawlowski.temperaturemanager.BaseMviViewModel
import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.domain.getDataOrNull
import com.pawlowski.temperaturemanager.domain.resourceFlow
import com.pawlowski.temperaturemanager.domain.useCase.alerts.AddAlertUseCase
import com.pawlowski.temperaturemanager.domain.useCase.alerts.DeleteAlertUseCase
import com.pawlowski.temperaturemanager.domain.useCase.alerts.GetAlertsUseCase
import com.pawlowski.temperaturemanager.domain.useCase.devices.DeviceSelectionUseCase
import com.pawlowski.temperaturemanager.ui.navigation.Back
import com.pawlowski.temperaturemanager.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ensureActive
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
                    if (actualState.isActionInProgress) {
                        return
                    }
                    updateState {
                        copy(isActionInProgress = true)
                    }
                    viewModelScope.launch {
                        kotlin.runCatching {
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
                        }.onFailure {
                            ensureActive()
                            it.printStackTrace()
                        }

                        updateState {
                            copy(isActionInProgress = false)
                        }
                    }
                }

                is AlertsEvent.DeleteAlert -> {
                    if (actualState.isActionInProgress) {
                        return
                    }
                    updateState {
                        copy(isActionInProgress = true)
                    }
                    viewModelScope.launch {
                        runCatching {
                            deleteAlertUseCase(alertId = event.alertId)
                        }.onFailure {
                            ensureActive()
                            it.printStackTrace()
                        }.onSuccess {
                            updateState {
                                copy(
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
                            }
                        }

                        updateState {
                            copy(isActionInProgress = false)
                        }
                    }
                }
            }
        }
    }
