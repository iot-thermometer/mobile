package com.pawlowski.temperaturemanager.data.repository

import com.pawlowski.temperaturemanager.data.ble.IBLEManager
import com.pawlowski.temperaturemanager.data.dataProviders.ThermometerDataProvider
import com.pawlowski.temperaturemanager.domain.models.BluetoothDeviceAdvertisement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

internal class AddDeviceRepository @Inject constructor(
    private val thermometerDataProvider: ThermometerDataProvider,
    private val bleManager: IBLEManager,
) {

    private val selectedAdvertisementFlow: MutableStateFlow<BluetoothDeviceAdvertisement?> =
        MutableStateFlow(null)

    suspend fun pairWithDevice(
        deviceName: String,
        ssid: String,
        password: String,
        bluetoothDeviceAdvertisement: BluetoothDeviceAdvertisement,
    ) {
        val device = thermometerDataProvider.createDevice(
            name = deviceName,
            pushInterval = 10,
            readingInterval = 1,
        )

        bleManager.sendMessageToDevice(
            bluetoothDeviceAdvertisement = bluetoothDeviceAdvertisement,
            ssid = ssid,
            password = password,
            id = device.id,
            token = device.token,
        )
    }

    fun scanNearbyDevices(): Flow<List<BluetoothDeviceAdvertisement>> =
        bleManager.getScannedDevices()

    fun selectAdvertisement(advertisement: BluetoothDeviceAdvertisement) {
        selectedAdvertisementFlow.value = advertisement
    }

    fun getSelectedAdvertisement(): StateFlow<BluetoothDeviceAdvertisement?> =
        selectedAdvertisementFlow.asStateFlow()
}
