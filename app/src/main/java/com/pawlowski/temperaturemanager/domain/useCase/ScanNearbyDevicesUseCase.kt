package com.pawlowski.temperaturemanager.domain.useCase

import com.juul.kable.AndroidAdvertisement
import com.pawlowski.temperaturemanager.data.repository.AddDeviceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ScanNearbyDevicesUseCase @Inject constructor(
    private val addDeviceRepository: AddDeviceRepository,
) {

    operator fun invoke(): Flow<List<AndroidAdvertisement>> = addDeviceRepository
        .scanNearbyDevices()
}
