package com.pawlowski.temperaturemanager.domain.useCase

import com.pawlowski.temperaturemanager.data.ThermometerDataProvider
import javax.inject.Inject

class DeleteDeviceUseCase @Inject constructor(
    private val thermometerDataProvider: ThermometerDataProvider,
) {

    suspend operator fun invoke(deviceId: Long) {
        thermometerDataProvider.deleteDevice(deviceId = deviceId)
    }
}
