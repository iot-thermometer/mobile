package com.pawlowski.temperaturemanager.ui.screens.alerts

import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.domain.models.AlertDomain

data class AlertsState(
    val alertsResource: Resource<List<AlertDomain>>,
    val actionResource: Resource<Unit>? = null,
)

sealed interface AlertsEvent {
    object OnBackClick : AlertsEvent

    data class OnAddAlert(
        val name: String,
        val minTemp: Float?,
        val maxTemp: Float?,
        val minSoil: Float?,
        val maxSoil: Float?,
    ) : AlertsEvent

    data class DeleteAlert(val alertId: Long) : AlertsEvent

    object RetryClick : AlertsEvent
}
