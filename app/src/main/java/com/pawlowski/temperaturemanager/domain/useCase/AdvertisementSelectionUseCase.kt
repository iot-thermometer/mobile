package com.pawlowski.temperaturemanager.domain.useCase

import com.pawlowski.temperaturemanager.data.repository.AddDeviceRepository
import com.pawlowski.temperaturemanager.domain.models.BluetoothDeviceAdvertisement
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

internal class AdvertisementSelectionUseCase @Inject constructor(
    private val addDeviceRepository: AddDeviceRepository,
) {

    fun selectAdvertisement(advertisement: BluetoothDeviceAdvertisement) {
        addDeviceRepository.selectAdvertisement(advertisement = advertisement)
    }

    fun getSelectedAdvertisement(): StateFlow<BluetoothDeviceAdvertisement?> =
        addDeviceRepository.getSelectedAdvertisement()
}
