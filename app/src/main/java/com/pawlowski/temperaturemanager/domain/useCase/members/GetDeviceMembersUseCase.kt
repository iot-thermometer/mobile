package com.pawlowski.temperaturemanager.domain.useCase.members

import com.pawlowski.temperaturemanager.data.ThermometerDataProvider
import com.pawlowski.temperaturemanager.domain.models.Member
import javax.inject.Inject

class GetDeviceMembersUseCase
    @Inject
    constructor(
        private val thermometerDataProvider: ThermometerDataProvider,
    ) {
        suspend operator fun invoke(deviceId: Long): List<Member> =
            thermometerDataProvider
                .getDeviceMembers(deviceId = deviceId)
    }
