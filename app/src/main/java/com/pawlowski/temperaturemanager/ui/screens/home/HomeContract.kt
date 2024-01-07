package com.pawlowski.temperaturemanager.ui.screens.home

import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.domain.models.DeviceWithOverview

data class HomeState(
    val devicesOverviewResource: Resource<List<DeviceWithOverview>>,
)

sealed interface HomeEvent {
    object AddNewDeviceClick : HomeEvent

    data class DeviceClick(val deviceId: Long) : HomeEvent

    object LogOutClick : HomeEvent
}
