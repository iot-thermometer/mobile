package com.pawlowski.temperaturemanager.ui.screens.alerts

import com.pawlowski.temperaturemanager.BaseMviViewModel
import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class AlertsViewModel @Inject constructor() :
    BaseMviViewModel<AlertsState, AlertsEvent, Screen.Alerts.AlertsDirection>(
        initialState = AlertsState(
            alertsResource = Resource.Loading,
        ),
    ) {

    override fun onNewEvent(event: AlertsEvent) {
    }
}
