package com.pawlowski.temperaturemanager.domain.useCase.alerts

import com.pawlowski.temperaturemanager.data.ThermometerDataProvider
import javax.inject.Inject

class DeleteAlertUseCase
    @Inject
    constructor(
        private val thermometerDataProvider: ThermometerDataProvider,
    ) {
        suspend operator fun invoke(alertId: Long) {
            thermometerDataProvider.deleteAlert(alertId = alertId)
        }
    }
