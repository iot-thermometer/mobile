package com.pawlowski.temperaturemanager.domain.useCase.devices

import com.pawlowski.temperaturemanager.data.ThermometerDataProvider
import javax.inject.Inject

class UpdateDeviceUseCase
    @Inject
    constructor(
        private val thermometerDataProvider: ThermometerDataProvider,
    ) {
        suspend operator fun invoke(
            deviceId: Long,
            name: String,
            pushInterval: Long,
            readingInterval: Long,
        ) {
            thermometerDataProvider.updateDevice(
                deviceId = deviceId,
                readingInterval = readingInterval,
                pushInterval = pushInterval,
                name = name,
            )
        }
    }
