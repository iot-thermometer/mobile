package com.pawlowski.temperaturemanager.domain.useCase

import com.pawlowski.temperaturemanager.data.ThermometerDataProvider
import javax.inject.Inject

class UpdateDeviceIntervalsUseCase @Inject constructor(
    private val thermometerDataProvider: ThermometerDataProvider,
) {

    suspend operator fun invoke(
        deviceId: Long,
        readingInterval: Long,
        pushInterval: Long,
    ) {
        thermometerDataProvider.updateDevice(
            deviceId = deviceId,
            readingInterval = readingInterval,
            pushInterval = pushInterval,
        )
    }
}
