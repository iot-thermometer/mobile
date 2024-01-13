package com.pawlowski.temperaturemanager.domain.useCase.devices

import com.pawlowski.temperaturemanager.data.ThermometerDataProvider
import com.pawlowski.temperaturemanager.domain.models.DeviceWithOverview
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.ensureActive
import javax.inject.Inject

class GetDevicesOverviewUseCase
    @Inject
    constructor(
        private val thermometerDataProvider: ThermometerDataProvider,
    ) {
        suspend operator fun invoke(): List<DeviceWithOverview> {
            return coroutineScope {
                thermometerDataProvider.listDevices().map {
                    async {
                        val lastReading =
                            runCatching {
                                thermometerDataProvider.listReadings(
                                    deviceId = it.id,
                                ).firstOrNull()
                            }.onFailure {
                                ensureActive()
                                it.printStackTrace()
                            }.getOrNull()
                        DeviceWithOverview(
                            device = it,
                            currentSoilMoisture = lastReading?.soilMoisture?.toInt(),
                            currentTemperature = lastReading?.temperature?.toInt(),
                        )
                    }
                }.awaitAll()
            }
        }
    }
