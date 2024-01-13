package com.pawlowski.temperaturemanager.ui.screens.splash

import androidx.lifecycle.viewModelScope
import com.pawlowski.notificationservice.IRunPushTokenSynchronizationUseCase
import com.pawlowski.temperaturemanager.BaseMviViewModel
import com.pawlowski.temperaturemanager.domain.useCase.IsLoggedInUseCase
import com.pawlowski.temperaturemanager.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
    @Inject
    constructor(
        private val isLoggedInUseCase: IsLoggedInUseCase,
        private val synchronizationUseCase: IRunPushTokenSynchronizationUseCase,
    ) : BaseMviViewModel<Any, Nothing, Screen.Splash.SplashDirection>(
            initialState = Any(),
        ) {
        override fun initialised() {
            viewModelScope.launch {
                isLoggedInUseCase().also {
                    if (it) {
                        synchronizationUseCase()
                        pushNavigationEvent(Screen.Splash.SplashDirection.HOME)
                    } else {
                        pushNavigationEvent(Screen.Splash.SplashDirection.LOGIN)
                    }
                }
            }
        }

        override fun onNewEvent(event: Nothing) {}
    }
