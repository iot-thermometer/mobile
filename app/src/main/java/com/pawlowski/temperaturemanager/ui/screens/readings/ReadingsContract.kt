package com.pawlowski.temperaturemanager.ui.screens.readings

import com.pawlowski.temperaturemanager.domain.models.ReadingDomain

data class ReadingsState(
    val amIOwner: Boolean,
    val contentState: ContentState,
) {
    sealed interface ContentState {
        object Loading : ContentState

        object Empty : ContentState

        object Error : ContentState

        data class Content(
            val lastTemperature: Int?,
            val lastSoilMoisture: Int?,
            val readings: Map<String, List<ReadingDomain>>,
        ) : ContentState
    }
}

sealed interface ReadingsEvent {
    object BackClick : ReadingsEvent

    object SettingsClick : ReadingsEvent

    object RetryClick : ReadingsEvent
}
