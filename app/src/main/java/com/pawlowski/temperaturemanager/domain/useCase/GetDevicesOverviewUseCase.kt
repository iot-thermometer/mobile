package com.pawlowski.temperaturemanager.domain.useCase

import com.pawlowski.temperaturemanager.data.dataProviders.ThermometerDataProvider
import com.pawlowski.temperaturemanager.domain.models.DeviceWithOverview
import javax.inject.Inject

class GetDevicesOverviewUseCase @Inject constructor(
    private val thermometerDataProvider: ThermometerDataProvider,
) {

    suspend operator fun invoke(): List<DeviceWithOverview> {
        return thermometerDataProvider.listDevices().map {
            DeviceWithOverview(
                device = it,
                currentSoilMoisture = 20, // TODO
                currentTemperature = 20, // TODO
            )
        }
    }
}
