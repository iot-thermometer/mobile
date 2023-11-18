package com.pawlowski.temperaturemanager.domain.useCase

import com.pawlowski.temperaturemanager.domain.models.DeviceOverview
import javax.inject.Inject

class GetDevicesOverviewUseCase @Inject constructor() {

    suspend operator fun invoke(): List<DeviceOverview> {
        return listOf()
    }
}
