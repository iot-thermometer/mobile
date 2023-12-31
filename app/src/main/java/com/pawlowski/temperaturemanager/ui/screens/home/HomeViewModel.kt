package com.pawlowski.temperaturemanager.ui.screens.home

import androidx.lifecycle.viewModelScope
import com.pawlowski.temperaturemanager.BaseMviViewModel
import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.domain.resourceFlow
import com.pawlowski.temperaturemanager.domain.useCase.DeviceSelectionUseCase
import com.pawlowski.temperaturemanager.domain.useCase.GetDevicesOverviewUseCase
import com.pawlowski.temperaturemanager.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val getDevicesOverviewUseCase: GetDevicesOverviewUseCase,
    private val deviceSelectionUseCase: DeviceSelectionUseCase,
) : BaseMviViewModel<HomeState, HomeEvent, Screen.Home.HomeDirection>(
    initialState = HomeState(
        devicesOverviewResource = Resource.Loading,
    ),
) {

    override fun initialised() {
        viewModelScope.launch {
            resourceFlow {
                getDevicesOverviewUseCase()
            }.collect {
                updateState {
                    copy(devicesOverviewResource = it)
                }
            }
        }
    }

    override fun onNewEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.AddNewDeviceClick -> {
                pushNavigationEvent(Screen.Home.HomeDirection.SEARCH_DEVICES)
            }

            is HomeEvent.DeviceClick -> {
                deviceSelectionUseCase.selectDevice(event.deviceId)
                pushNavigationEvent(Screen.Home.HomeDirection.READINGS)
            }
        }
    }
}
