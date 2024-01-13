package com.pawlowski.temperaturemanager.ui.screens.wifiInfo

import androidx.lifecycle.viewModelScope
import com.pawlowski.temperaturemanager.BaseMviViewModel
import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.domain.models.BluetoothDeviceAdvertisement
import com.pawlowski.temperaturemanager.domain.resourceFlow
import com.pawlowski.temperaturemanager.domain.useCase.devices.PairWithDeviceUseCase
import com.pawlowski.temperaturemanager.domain.useCase.pairing.AdvertisementSelectionUseCase
import com.pawlowski.temperaturemanager.ui.navigation.Back
import com.pawlowski.temperaturemanager.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WifiInfoViewModel
    @Inject
    constructor(
        private val selectionUseCase: AdvertisementSelectionUseCase,
        private val pairWithDeviceUseCase: PairWithDeviceUseCase,
    ) :
    BaseMviViewModel<WifiInfoState, WifiInfoEvent, Screen.WifiInfo.WifiInfoDirection>(
            initialState = WifiInfoState.Initialising,
        ) {
        override fun initialised() {
            val currentAdvertisement =
                selectionUseCase.getSelectedAdvertisement().value!!

            updateState {
                WifiInfoState.Content(
                    chosenAdvertisement = currentAdvertisement,
                )
            }
        }

        //    fun onSsidInputChange(newSsidInput: String) {
//        updateState {
//            WifiInfoState.Content(
//                ssidInput = newSsidInput,
//            )
//        }
//    }
        override fun onNewEvent(event: WifiInfoEvent) {
            when (event) {
                is WifiInfoEvent.ContinueClick -> onContinue()
                WifiInfoEvent.BackClick -> pushNavigationEvent(Back)
                is WifiInfoEvent.ChangePassword -> {
                    updateStateIfContent {
                        copy(
                            passwordInput = event.newPassword,
                            passwordError = null,
                        )
                    }
                }

                is WifiInfoEvent.ChangeSsid -> {
                    updateStateIfContent {
                        copy(
                            ssidInput = event.newSsid,
                            ssidError = null,
                        )
                    }
                }
            }
        }

        private fun onContinue() {
            getContentOrNull()?.let { currentState ->
                val ssid = currentState.ssidInput
                val password = currentState.passwordInput

                updateStateIfContent {
                    copy(
                        pairingError = null,
                        ssidError = null,
                        passwordError = null,
                    )
                }

                val isSsidCorrect = ssid.isNotBlank()
                val isPasswordCorrect = password.isNotBlank()

                when {
                    !isPasswordCorrect && !isSsidCorrect -> {
                        updateStateIfContent {
                            copy(
                                ssidError = "Ssid is required!",
                                passwordError = "Password is required!",
                            )
                        }
                    }

                    !isPasswordCorrect -> {
                        updateStateIfContent {
                            copy(
                                passwordError = "Password is required!",
                            )
                        }
                    }

                    !isSsidCorrect -> {
                        updateStateIfContent {
                            copy(
                                ssidError = "Ssid is required!",
                            )
                        }
                    }

                    else -> {
                        pairWithDevice(
                            chosenAdvertisement = currentState.chosenAdvertisement,
                            ssid = ssid,
                            password = password,
                        )
                    }
                }
            }
        }

        private fun pairWithDevice(
            chosenAdvertisement: BluetoothDeviceAdvertisement,
            ssid: String,
            password: String,
        ) {
            viewModelScope.launch {
                resourceFlow {
                    pairWithDeviceUseCase(
                        deviceName = chosenAdvertisement.name,
                        ssid = ssid,
                        password = password,
                        bluetoothDeviceAdvertisement = chosenAdvertisement,
                    )
                }.collect {
                    when (it) {
                        is Resource.Success -> {
                            updateStateIfContent {
                                copy(isLoading = false)
                            }
                            pushNavigationEvent(direction = Screen.WifiInfo.WifiInfoDirection.HOME)
                        }

                        is Resource.Error -> {
                            updateStateIfContent {
                                copy(
                                    isLoading = false,
                                    pairingError = it.throwable.message ?: "Error",
                                )
                            }
                        }

                        is Resource.Loading -> {
                            updateStateIfContent {
                                copy(isLoading = true)
                            }
                        }
                    }
                }
            }
        }

        private fun getContentOrNull(): WifiInfoState.Content? = (actualState as? WifiInfoState.Content)

        private inline fun updateStateIfContent(crossinline update: WifiInfoState.Content.() -> WifiInfoState) {
            updateState {
                getContentOrNull()?.update() ?: this
            }
        }
    }
