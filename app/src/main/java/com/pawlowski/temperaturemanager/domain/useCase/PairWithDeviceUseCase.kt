package com.pawlowski.temperaturemanager.domain.useCase

import com.pawlowski.temperaturemanager.data.repository.AddDeviceRepository
import com.pawlowski.temperaturemanager.domain.models.BluetoothDeviceAdvertisement
import javax.inject.Inject

internal class PairWithDeviceUseCase @Inject constructor(
    private val addDeviceRepository: AddDeviceRepository,
) {

    suspend operator fun invoke(
        deviceName: String,
        ssid: String,
        password: String,
        bluetoothDeviceAdvertisement: BluetoothDeviceAdvertisement,
    ) {
        addDeviceRepository.pairWithDevice(
            deviceName = deviceName,
            ssid = ssid,
            password = password,
            bluetoothDeviceAdvertisement = bluetoothDeviceAdvertisement,
        )
    }
}
