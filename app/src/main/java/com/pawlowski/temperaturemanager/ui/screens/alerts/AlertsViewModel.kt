package com.pawlowski.temperaturemanager.ui.screens.alerts

import androidx.lifecycle.viewModelScope
import com.pawlowski.temperaturemanager.BaseMviViewModel
import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.domain.resourceFlow
import com.pawlowski.temperaturemanager.domain.useCase.GetAlertsUseCase
import com.pawlowski.temperaturemanager.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AlertsViewModel @Inject constructor(
    private val getAlertsUseCase: GetAlertsUseCase,
) :
    BaseMviViewModel<AlertsState, AlertsEvent, Screen.Alerts.AlertsDirection>(
        initialState = AlertsState(
            alertsResource = Resource.Loading,
        ),
    ) {

    override fun initialised() {
        viewModelScope.launch {
            resourceFlow {
                getAlertsUseCase(deviceId = 0)
            }.collectLatest {
                updateState {
                    copy(alertsResource = it)
                }
            }
        }
    }

    override fun onNewEvent(event: AlertsEvent) {
    }
}
