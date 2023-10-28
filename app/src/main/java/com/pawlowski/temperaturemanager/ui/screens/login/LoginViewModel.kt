package com.pawlowski.temperaturemanager.ui.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawlowski.temperaturemanager.data.dataProviders.ThermometerDataProvider
import com.pawlowski.temperaturemanager.domain.useCase.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val thermometerRepository: ThermometerDataProvider,
) : ViewModel() {

    val state: StateFlow<LoginState>
        get() = _state.asStateFlow()

    private val _state = MutableStateFlow(LoginState())

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.LoginClick -> {
                loginClicked()
            }

            is LoginEvent.RegisterClick -> {
                registerClicked()
            }

            is LoginEvent.EmailChange -> {
                _state.update {
                    it.copy(email = event.newEmail)
                }
            }

            is LoginEvent.PasswordChange -> {
                _state.update {
                    it.copy(password = event.newPassword)
                }
            }
        }
    }

    private fun loginClicked() {
        viewModelScope.launch {
            runCatching {
                state.value.let {
                    if (it.email.isNotBlank() && it.password.isNotBlank()) {
                        loginRepository.login(
                            email = it.email,
                            password = it.password,
                        )

                        Log.d("DEVICES_LIST", thermometerRepository.listDevices())
                    }
                }
            }.onFailure {
                ensureActive()
                it.printStackTrace()
            }
        }
    }

    private fun registerClicked() {
        viewModelScope.launch {
            state.value.let {
                kotlin.runCatching {
                    if (it.email.isNotBlank() && it.password.isNotBlank()) {
                        loginRepository.register(
                            email = it.email,
                            password = it.password,
                        )

                        Log.d("DEVICES_LIST", thermometerRepository.listDevices())
                    }
                }.onFailure {
                    ensureActive()
                    it.printStackTrace()
                }
            }
        }
    }
}
