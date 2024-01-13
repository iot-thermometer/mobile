package com.pawlowski.temperaturemanager.domain.useCase.pairing

import com.pawlowski.temperaturemanager.data.repository.AddDeviceRepository
import com.pawlowski.temperaturemanager.domain.models.BluetoothDeviceAdvertisement
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ScanNearbyDevicesUseCase
    @Inject
    constructor(
        private val addDeviceRepository: AddDeviceRepository,
    ) {
        operator fun invoke(): Flow<List<BluetoothDeviceAdvertisement>> =
            addDeviceRepository
                .scanNearbyDevices()
    }
