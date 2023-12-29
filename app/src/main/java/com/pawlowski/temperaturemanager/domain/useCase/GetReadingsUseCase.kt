package com.pawlowski.temperaturemanager.domain.useCase

import com.pawlowski.temperaturemanager.data.dataProviders.ThermometerDataProvider
import com.pawlowski.temperaturemanager.domain.models.ReadingDomain
import javax.inject.Inject

class GetReadingsUseCase @Inject constructor(
    private val thermometerDataProvider: ThermometerDataProvider,
) {

    suspend operator fun invoke(deviceId: Long): List<ReadingDomain> = thermometerDataProvider
        .listReadings(deviceId = deviceId)
}
