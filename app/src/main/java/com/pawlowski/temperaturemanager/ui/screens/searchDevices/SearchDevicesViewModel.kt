package com.pawlowski.temperaturemanager.ui.screens.searchDevices

import androidx.lifecycle.viewModelScope
import com.juul.kable.AndroidAdvertisement
import com.pawlowski.temperaturemanager.BaseMviViewModel
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

    fun pairWithDevice(advertisement: AndroidAdvertisement) {
        viewModelScope.launch {
            runCatching {
                pairWithDeviceUseCase(
                    deviceName = "Nazwa",
                    ssid = "Wifi u Macka",
                    password = "",
                    advertisement = advertisement,
                )
            }.onFailure {
                it.printStackTrace()
            }.onSuccess {
                println("Success")
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
