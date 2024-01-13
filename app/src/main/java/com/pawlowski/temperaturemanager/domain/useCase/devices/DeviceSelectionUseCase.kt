package com.pawlowski.temperaturemanager.domain.useCase.devices

import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DeviceSelectionUseCase
    @Inject
    constructor() {
        private val selectedDeviceId = MutableStateFlow<Long?>(null)

        fun selectDevice(deviceId: Long) {
            selectedDeviceId.value = deviceId
        }

        fun getSelectedDeviceId(): Long? = selectedDeviceId.value
    }
