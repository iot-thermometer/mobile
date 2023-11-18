package com.pawlowski.temperaturemanager.domain.useCase

import com.juul.kable.AndroidAdvertisement
import com.pawlowski.temperaturemanager.data.repository.AddDeviceRepository
import javax.inject.Inject

internal class PairWithDeviceUseCase @Inject constructor(
    private val addDeviceRepository: AddDeviceRepository,
) {

    suspend operator fun invoke(
        deviceName: String,
        ssid: String,
        password: String,
        advertisement: AndroidAdvertisement,
    ) {
        addDeviceRepository.pairWithDevice(
            deviceName = deviceName,
            ssid = ssid,
            password = password,
            advertisement = advertisement,
        )
    }
}
