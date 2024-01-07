package com.pawlowski.temperaturemanager.ui.screens.alerts

import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.domain.models.AlertDomain

data class AlertsState(
    val alertsResource: Resource<List<AlertDomain>>,
)

sealed interface AlertsEvent {

    object OnBackClick : AlertsEvent
}
