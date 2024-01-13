package com.pawlowski.temperaturemanager.ui.screens.home

import androidx.lifecycle.viewModelScope
import com.pawlowski.temperaturemanager.BaseMviViewModel
import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.domain.RetrySharedFlow
import com.pawlowski.temperaturemanager.domain.resourceFlowWithRetrying
import com.pawlowski.temperaturemanager.domain.useCase.authentication.LogOutUseCase
import com.pawlowski.temperaturemanager.domain.useCase.devices.DeviceSelectionUseCase
import com.pawlowski.temperaturemanager.domain.useCase.devices.GetDevicesOverviewUseCase
import com.pawlowski.temperaturemanager.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel
    @Inject
    constructor(
        private val getDevicesOverviewUseCase: GetDevicesOverviewUseCase,
        private val deviceSelectionUseCase: DeviceSelectionUseCase,
        private val logOutUseCase: LogOutUseCase,
    ) : BaseMviViewModel<HomeState, HomeEvent, Screen.Home.HomeDirection>(
            initialState =
                HomeState(
                    devicesOverviewResource = Resource.Loading,
                ),
        ) {
        private val retrySharedFlow = RetrySharedFlow()

        override fun initialised() {
            viewModelScope.launch {
                resourceFlowWithRetrying(retrySharedFlow = retrySharedFlow) {
                    getDevicesOverviewUseCase()
                }.collect {
                    updateState {
                        copy(devicesOverviewResource = it)
                    }
                }
            }
        }

        override fun onNewEvent(event: HomeEvent) {
            if (actualState.isLoggingOut) {
                return
            }

            when (event) {
                is HomeEvent.AddNewDeviceClick -> {
                    pushNavigationEvent(Screen.Home.HomeDirection.SEARCH_DEVICES)
                }

                is HomeEvent.DeviceClick -> {
                    deviceSelectionUseCase.selectDevice(event.deviceId)
                    pushNavigationEvent(Screen.Home.HomeDirection.READINGS)
                }

                HomeEvent.LogOutClick -> {
                    updateState {
                        copy(isLoggingOut = true)
                    }
                    viewModelScope.launch {
                        runCatching {
                            logOutUseCase.invoke()
                        }.onFailure {
                            ensureActive()
                            it.printStackTrace()
                        }
                        pushNavigationEvent(Screen.Home.HomeDirection.LOGIN)
                    }
                }

                HomeEvent.RetryClick -> {
                    retrySharedFlow.sendRetryEvent()
                }
            }
        }
    }
