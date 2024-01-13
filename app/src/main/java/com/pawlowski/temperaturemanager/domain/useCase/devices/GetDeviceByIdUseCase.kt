package com.pawlowski.temperaturemanager.domain.useCase.devices

import com.pawlowski.temperaturemanager.data.ThermometerDataProvider
import com.pawlowski.temperaturemanager.domain.models.DeviceDomain
import javax.inject.Inject

class GetDeviceByIdUseCase
    @Inject
    constructor(
        private val thermometerDataProvider: ThermometerDataProvider,
    ) {
        suspend operator fun invoke(deviceId: Long): DeviceDomain {
            return thermometerDataProvider.listDevices().first { it.id == deviceId }
        }
    }
