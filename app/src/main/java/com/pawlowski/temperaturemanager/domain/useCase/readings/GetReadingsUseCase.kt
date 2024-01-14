package com.pawlowski.temperaturemanager.domain.useCase.readings

import com.pawlowski.temperaturemanager.data.ThermometerDataProvider
import com.pawlowski.temperaturemanager.domain.models.ReadingDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetReadingsUseCase
    @Inject
    constructor(
        private val thermometerDataProvider: ThermometerDataProvider,
    ) {
        suspend operator fun invoke(deviceId: Long): List<ReadingDomain> =
            withContext(Dispatchers.IO) {
                thermometerDataProvider
                    .listReadings(deviceId = deviceId)
                    .groupBy {
                        it.measuredAt
                    }.map {
                        val readingsForTimestamp = it.value
                        val temp = readingsForTimestamp.firstNotNullOfOrNull { it.temperature }
                        val soil = readingsForTimestamp.firstNotNullOfOrNull { it.soilMoisture }
                        ReadingDomain(
                            temperature = temp,
                            soilMoisture = soil,
                            measuredAt = it.key,
                        )
                    }
            }
    }
