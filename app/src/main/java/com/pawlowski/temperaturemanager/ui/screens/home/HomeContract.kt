package com.pawlowski.temperaturemanager.ui.screens.home

import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.domain.models.DeviceOverview

data class HomeState(
    val devicesOverviewResource: Resource<List<DeviceOverview>>,
)

sealed interface HomeEvent

sealed interface HomeEffect
