package com.pawlowski.temperaturemanager.domain.useCase.readings

import com.pawlowski.temperaturemanager.data.ThermometerDataProvider
import com.pawlowski.temperaturemanager.domain.models.ReadingDomain
import javax.inject.Inject

class GetReadingsUseCase
    @Inject
    constructor(
        private val thermometerDataProvider: ThermometerDataProvider,
    ) {
        suspend operator fun invoke(deviceId: Long): List<ReadingDomain> =
        /*listOf(
        ReadingDomain(
            temperature = 30.5f,
            soilMoisture = 19.2f,
            measuredAt = 1704085242000,
        ),
        ReadingDomain(
            temperature = 15.5f,
            soilMoisture = 19.2f,
            measuredAt = 1704081642000,
        ),
        ReadingDomain(
            temperature = 20.5f,
            soilMoisture = 19.2f,
            measuredAt = 1703979822000,
        ),
    )*/
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
