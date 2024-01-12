package com.pawlowski.temperaturemanager.domain.useCase

import com.pawlowski.temperaturemanager.data.ThermometerDataProvider
import javax.inject.Inject

class AddAlertUseCase @Inject constructor(
    private val thermometerDataProvider: ThermometerDataProvider,
) {

    suspend operator fun invoke(
        deviceId: Long,
        name: String,
        minTemp: Float?,
        maxTemp: Float?,
        minSoil: Float?,
        maxSoil: Float?,
    ) {
        thermometerDataProvider.createAlerts(
            deviceId = deviceId,
            minTemp = minTemp,
            maxTemp = maxTemp,
            minSoil = minSoil,
            maxSoil = maxSoil,
            name = name,
        )
    }
}
