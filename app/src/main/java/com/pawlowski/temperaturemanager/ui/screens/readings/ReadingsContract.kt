package com.pawlowski.temperaturemanager.ui.screens.readings

import com.pawlowski.temperaturemanager.domain.models.ReadingDomain

sealed interface ReadingsState {

    object Loading : ReadingsState

    object Empty : ReadingsState

    object Error : ReadingsState

    data class Content(
        val lastTemperature: Int,
        val lastSoilMoisture: Int?,
        val readings: Map<String, List<ReadingDomain>>,
    ) : ReadingsState
}

sealed interface ReadingsEvent {

    object BackClick : ReadingsEvent
}
