package com.pawlowski.temperaturemanager.domain.useCase.members

import com.pawlowski.temperaturemanager.domain.models.OwnershipType
import javax.inject.Inject

class AmIOwnerOfDeviceUseCase @Inject constructor(
    private val getDeviceMembersUseCase: GetDeviceMembersUseCase,
) {

    suspend operator fun invoke(deviceId: Long): Boolean {
        return getDeviceMembersUseCase(deviceId = deviceId).firstOrNull {
            it.isMe
        }?.ownership == OwnershipType.OWNER
    }
}