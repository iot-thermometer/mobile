package com.pawlowski.temperaturemanager.ui.screens.login

import androidx.lifecycle.viewModelScope
import com.pawlowski.network.ILoginRepository
import com.pawlowski.notificationservice.IRunPushTokenSynchronizationUseCase
import com.pawlowski.temperaturemanager.BaseMviViewModel
import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.domain.resourceFlow
import com.pawlowski.temperaturemanager.ui.navigation.Screen.Login.LoginDirection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel
    @Inject
    constructor(
        private val loginRepository: ILoginRepository,
        private val pushTokenSynchronizationUseCase: IRunPushTokenSynchronizationUseCase,
    ) : BaseMviViewModel<LoginState, LoginEvent, LoginDirection>(
            initialState = LoginState(),
        ) {
        override fun onNewEvent(event: LoginEvent) {
            when (event) {
                is LoginEvent.LoginClick -> {
                    loginClicked()
                }

                is LoginEvent.RegisterClick -> {
                    registerClicked()
                }

                is LoginEvent.EmailChange -> {
                    updateState {
                        copy(email = event.newEmail)
                    }
                }

                is LoginEvent.PasswordChange -> {
                    updateState {
                        copy(password = event.newPassword)
                    }
                }
            }
        }

        private fun loginClicked() {
            if (actualState.requestResource == Resource.Loading) {
                return
            }
            actualState.let { state ->
                if (state.email.isNotBlank() && state.password.isNotBlank()) {
                    viewModelScope.launch {
                        resourceFlow {
                            loginRepository.login(
                                email = state.email.trim(),
                                password = state.password,
                            )
                        }.collect {
                            if (it is Resource.Success) {
                                actionsOnGoHome()
                            }
                            updateState {
                                copy(requestResource = it)
                            }
                        }
                    }
                } else {
                    updateState {
                        copy(showErrorsIfAny = true)
                    }
                }
            }
        }

        private fun registerClicked() {
            if (actualState.requestResource == Resource.Loading) {
                return
            }
            viewModelScope.launch {
                actualState.let { state ->
                    if (state.email.isNotBlank() && state.password.isNotBlank()) {
                        resourceFlow {
                            loginRepository.register(
                                email = state.email,
                                password = state.password,
                            )
                        }.collect {
                            if (it is Resource.Success) {
                                actionsOnGoHome()
                            }
                            updateState {
                                copy(requestResource = it)
                            }
                        }
                    } else {
                        updateState {
                            copy(showErrorsIfAny = true)
                        }
                    }
                }
            }
        }

        private suspend fun actionsOnGoHome() {
            pushTokenSynchronizationUseCase()
            pushNavigationEvent(LoginDirection.HOME)
        }
    }
