package com.pawlowski.temperaturemanager.data.ble

import com.juul.kable.AndroidAdvertisement
import com.juul.kable.Scanner
import com.juul.kable.peripheral
import com.juul.kable.write
import com.pawlowski.temperaturemanager.domain.BluetoothException
import com.pawlowski.temperaturemanager.domain.models.BluetoothDeviceAdvertisement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class BLEManager
    @Inject
    constructor() : IBLEManager {
        private val scanner by lazy {
            Scanner { }
        }

        private val scope = CoroutineScope(Dispatchers.IO)

        private val advertisements = hashMapOf<String, AndroidAdvertisement>()

        override fun getScannedDevices(): Flow<List<BluetoothDeviceAdvertisement>> =
            scanner
                .advertisements
                .onStart {
                    println("Started searching")
                }
                .map {
                    advertisements[it.address] = it
                    advertisements.values.toList()
                        .map {
                            BluetoothDeviceAdvertisement(
                                name = it.name ?: "Unnamed",
                                macAddress = it.address,
                            )
                        }
                }

        override suspend fun sendMessageToDevice(
            bluetoothDeviceAdvertisement: BluetoothDeviceAdvertisement,
            token: String,
            id: Long,
            ssid: String,
            password: String,
        ) {
            val advertisement =
                advertisements[bluetoothDeviceAdvertisement.macAddress]
                    ?: throw BluetoothException.MissingAdvertisement

            val peripheral =
                scope
                    .peripheral(advertisement = advertisement) {
                        onServicesDiscovered {
                            requestMtu(100)
                        }
                    }

            peripheral.connect()

            val writeCharacteristics =
                peripheral.services?.firstNotNullOfOrNull {
                    it.characteristics.firstOrNull {
                        it.properties.write
                    }
                } ?: throw BluetoothException.CharacteristicsNotFound

            val textToSend =
                "{\"ssid\":\"$ssid\",\"password\":\"$password\", \"token\":\"${token}\",\"id\":$id}"

            peripheral.write(
                characteristic = writeCharacteristics,
                textToSend.toByteArray(),
            )
            println("Write success, waiting for disconnect")

            peripheral.disconnect()
        }

        override fun clearCache() {
            advertisements.clear()
        }
    }
