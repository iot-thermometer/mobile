package com.pawlowski.temperaturemanager.domain.useCase

import com.pawlowski.temperaturemanager.domain.models.DeviceWithOverview
import javax.inject.Inject

class GetDevicesOverviewUseCase @Inject constructor() {

    suspend operator fun invoke(): List<DeviceWithOverview> {
        return listOf()
    }
}
