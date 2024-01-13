package com.pawlowski.temperaturemanager.domain.useCase

import com.pawlowski.temperaturemanager.data.ThermometerDataProvider
import javax.inject.Inject

class DeleteDeviceMemberUseCase
    @Inject
    constructor(
        private val thermometerDataProvider: ThermometerDataProvider,
    ) {
        suspend operator fun invoke(
            deviceId: Long,
            userId: Long,
        ) {
            thermometerDataProvider.deleteDeviceMember(
                deviceId = deviceId,
                userId = userId,
            )
        }
    }
