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
                        val lastPair =
                            runCatching {
                                thermometerDataProvider.listReadings(
                                    deviceId = it.id,
                                ).let {
                                    val lastTemp = it.firstNotNullOfOrNull { it.temperature }
                                    val lastSoil = it.firstNotNullOfOrNull { it.soilMoisture }
                                    lastTemp to lastSoil
                                }
                            }.onFailure {
                                ensureActive()
                                it.printStackTrace()
                            }.getOrNull()
                        DeviceWithOverview(
                            device = it,
                            currentSoilMoisture = lastPair?.second?.toInt(),
                            currentTemperature = lastPair?.first?.toInt(),
                        )
                    }
                }.awaitAll()
            }
        }
    }
