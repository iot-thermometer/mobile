package com.pawlowski.temperaturemanager.ui.screens.searchDevices

import androidx.lifecycle.viewModelScope
import com.pawlowski.temperaturemanager.BaseMviViewModel
import com.pawlowski.temperaturemanager.domain.models.BluetoothDeviceAdvertisement
import com.pawlowski.temperaturemanager.domain.useCase.AdvertisementSelectionUseCase
import com.pawlowski.temperaturemanager.domain.useCase.ScanNearbyDevicesUseCase
import com.pawlowski.temperaturemanager.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SearchDevicesViewModel @Inject constructor(
    private val scanNearbyDevicesUseCase: ScanNearbyDevicesUseCase,
    private val selectionUseCase: AdvertisementSelectionUseCase,
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

    private fun chooseDevice(advertisement: BluetoothDeviceAdvertisement) {
        selectionUseCase.selectAdvertisement(advertisement = advertisement)
        pushNavigationEvent(Screen.SearchDevices.SearchDevicesDirection.WIFI_INFO)
    }

    override fun onNewEvent(event: SearchDevicesEvent) {
        when (event) {
            is SearchDevicesEvent.DeviceClick -> {
                chooseDevice(advertisement = event.advertisement)
            }
        }
    }
}
