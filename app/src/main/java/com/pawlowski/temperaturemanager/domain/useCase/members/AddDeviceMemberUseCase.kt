package com.pawlowski.temperaturemanager.domain.useCase.members

import com.pawlowski.temperaturemanager.data.ThermometerDataProvider
import javax.inject.Inject

class AddDeviceMemberUseCase
    @Inject
    constructor(
        private val thermometerDataProvider: ThermometerDataProvider,
    ) {
        suspend operator fun invoke(
            deviceId: Long,
            email: String,
        ) {
            thermometerDataProvider
                .addDeviceMember(
                    deviceId = deviceId,
                    email = email,
                )
        }
    }
