package com.pawlowski.temperaturemanager.data.repository

import com.juul.kable.AndroidAdvertisement
import com.pawlowski.temperaturemanager.data.BLEManager
import com.pawlowski.temperaturemanager.data.dataProviders.ThermometerDataProvider
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class AddDeviceRepository @Inject constructor(
    private val thermometerDataProvider: ThermometerDataProvider,
    private val bleManager: BLEManager,
) {

    suspend fun pairWithDevice(
        deviceName: String,
        ssid: String,
        password: String,
        advertisement: AndroidAdvertisement,
    ) {
        val device = thermometerDataProvider.createDevice(
            name = deviceName,
            pushInterval = 10,
            readingInterval = 1,
        )

        bleManager.sendMessageToDevice(
            advertisement = advertisement,
            ssid = ssid,
            password = password,
            id = device.id,
            token = device.token,
        )
    }

    fun scanNearbyDevices(): Flow<List<AndroidAdvertisement>> = bleManager.getScannedDevices()
}
