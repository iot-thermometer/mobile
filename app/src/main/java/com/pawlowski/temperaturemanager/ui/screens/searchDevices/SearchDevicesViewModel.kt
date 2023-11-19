package com.pawlowski.temperaturemanager.ui.screens.searchDevices

import androidx.lifecycle.viewModelScope
import com.juul.kable.AndroidAdvertisement
import com.pawlowski.temperaturemanager.BaseMviViewModel
import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.domain.resourceFlow
import com.pawlowski.temperaturemanager.domain.useCase.PairWithDeviceUseCase
import com.pawlowski.temperaturemanager.domain.useCase.ScanNearbyDevicesUseCase
import com.pawlowski.temperaturemanager.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SearchDevicesViewModel @Inject constructor(
    private val scanNearbyDevicesUseCase: ScanNearbyDevicesUseCase,
    private val pairWithDeviceUseCase: PairWithDeviceUseCase,
) :
    BaseMviViewModel<SearchDevicesState, SearchDevicesEvent, Screen.SearchDevices.SearchDevicesDirection>(
        initialState = SearchDevicesState(
            devices = emptyList(),
            isPairingInProgress = false,
        ),
    ) {

    override fun initialised() {
        viewModelScope.launch {
            scanNearbyDevicesUseCase()
                .collectLatest {
                    updateState {
                        copy(devices = it)
                    }
                }
        }
    }

    private fun pairWithDevice(advertisement: AndroidAdvertisement) {
        viewModelScope.launch {
            resourceFlow {
                pairWithDeviceUseCase(
                    deviceName = "Nazwa",
                    ssid = "Wifi u Macka",
                    password = "",
                    advertisement = advertisement,
                )
            }.collect {
                when (it) {
                    is Resource.Success -> {
                        updateState {
                            copy(isPairingInProgress = false)
                        }
                        pushNavigationEvent(direction = Screen.SearchDevices.SearchDevicesDirection.HOME)
                    }

                    is Resource.Error -> {
                        updateState {
                            copy(isPairingInProgress = false)
                        }
                    }

                    is Resource.Loading -> {
                        updateState {
                            copy(isPairingInProgress = true)
                        }
                    }
                }
            }
        }
    }

    override fun onNewEvent(event: SearchDevicesEvent) {
        when (event) {
            is SearchDevicesEvent.DeviceClick -> {
                pairWithDevice(advertisement = event.advertisement)
            }
        }
    }
}
