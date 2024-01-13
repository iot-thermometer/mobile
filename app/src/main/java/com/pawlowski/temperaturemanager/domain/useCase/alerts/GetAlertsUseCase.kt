package com.pawlowski.temperaturemanager.domain.useCase.alerts

import com.pawlowski.temperaturemanager.data.ThermometerDataProvider
import com.pawlowski.temperaturemanager.domain.models.AlertDomain
import javax.inject.Inject

class GetAlertsUseCase
    @Inject
    constructor(
        private val thermometerDataProvider: ThermometerDataProvider,
    ) {
        suspend operator fun invoke(deviceId: Long): List<AlertDomain> = thermometerDataProvider.listAlerts(deviceId = deviceId)
    }
