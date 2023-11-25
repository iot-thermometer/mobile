package com.pawlowski.temperaturemanager.data.ble

import com.pawlowski.temperaturemanager.domain.models.BluetoothDeviceAdvertisement
import kotlinx.coroutines.flow.Flow

interface IBLEManager {
    fun getScannedDevices(): Flow<List<BluetoothDeviceAdvertisement>>

    suspend fun sendMessageToDevice(
        bluetoothDeviceAdvertisement: BluetoothDeviceAdvertisement,
        token: String,
        id: Long,
        ssid: String,
        password: String,
    )
}
