package com.pawlowski.temperaturemanager.ui.screens.login

import androidx.lifecycle.viewModelScope
import com.pawlowski.network.ILoginRepository
import com.pawlowski.temperaturemanager.BaseMviViewModel
import com.pawlowski.temperaturemanager.ui.navigation.Screen.Login.LoginDirection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val loginRepository: ILoginRepository,
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
        viewModelScope.launch {
            runCatching {
                actualState.let { state ->
                    if (state.email.isNotBlank() && state.password.isNotBlank()) {
                        loginRepository.login(
                            email = state.email,
                            password = state.password,
                        )
                    }
                }
            }.onFailure {
                ensureActive()
                it.printStackTrace()
            }.onSuccess {
                pushNavigationEvent(LoginDirection.HOME)
            }
        }
    }

    private fun registerClicked() {
        viewModelScope.launch {
            actualState.let { state ->
                kotlin.runCatching {
                    if (state.email.isNotBlank() && state.password.isNotBlank()) {
                        loginRepository.register(
                            email = state.email,
                            password = state.password,
                        )
                    }
                }.onFailure {
                    ensureActive()
                    it.printStackTrace()
                }.onSuccess {
                    pushNavigationEvent(LoginDirection.HOME)
                }
            }
        }
    }
}
