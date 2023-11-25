package com.pawlowski.temperaturemanager.data.ble

import com.pawlowski.temperaturemanager.domain.models.BluetoothDeviceAdvertisement
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeBLEManager @Inject constructor() : IBLEManager {

    override fun getScannedDevices(): Flow<List<BluetoothDeviceAdvertisement>> = flow {
        delay(1000)

        val list1 = listOf(
            BluetoothDeviceAdvertisement(
                name = "Samsung S10e",
                macAddress = "2E-B7-F9-17-6F-A8",
            ),
        )

        emit(list1)
        delay(1000)

        val list2 = list1 + BluetoothDeviceAdvertisement(
            name = "IoT Thermometer",
            macAddress = "E9-97-99-35-C3-61",
        )
        emit(list2)

        delay(2000)

        val list3 = list2 + BluetoothDeviceAdvertisement(
            name = "Galaxy S22",
            macAddress = "DF-38-34-B2-F9-69",
        )
        emit(list3)
    }

    override suspend fun sendMessageToDevice(
        bluetoothDeviceAdvertisement: BluetoothDeviceAdvertisement,
        token: String,
        id: Long,
        ssid: String,
        password: String,
    ) {
        delay(3000)
    }
}
