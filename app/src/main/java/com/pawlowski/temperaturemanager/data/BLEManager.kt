package com.pawlowski.temperaturemanager.data

import com.juul.kable.AndroidAdvertisement
import com.juul.kable.Scanner
import com.juul.kable.peripheral
import com.juul.kable.read
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

private const val SERVICE_ID = "000000ff-0000-1000-8000-00805f9b34fb"
private const val CHARACTERISTICS_ID = "0000ff01-0000-1000-8000-00805f9b34fb"
private const val DESCRIPTOR_ID = "00002902-0000-1000-8000-00805f9b34fb"

@Singleton
internal class BLEManager @Inject constructor() {

    private val scanner by lazy {
        Scanner { }
    }

    private val scope = CoroutineScope(Dispatchers.IO)

    private val advertisements = hashMapOf<String, AndroidAdvertisement>()

    fun getScannedDevices(): Flow<List<BluetoothDeviceAdvertisement>> = scanner
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

    suspend fun sendMessageToDevice(
        bluetoothDeviceAdvertisement: BluetoothDeviceAdvertisement,
        token: String,
        id: Long,
        ssid: String,
        password: String,
    ) {
        val advertisement = advertisements[bluetoothDeviceAdvertisement.macAddress]
            ?: throw BluetoothException.MissingAdvertisement

        val peripheral = scope
            .peripheral(advertisement = advertisement) {
                onServicesDiscovered {
                    requestMtu(100)
                }
            }

        peripheral.connect()

        val writeCharacteristics = peripheral.services?.firstNotNullOfOrNull {
            it.characteristics.firstOrNull {
                it.properties.write
            }
        } ?: throw BluetoothException.CharacteristicsNotFound

        val readCharacteristics = peripheral.services?.firstNotNullOfOrNull {
            it.characteristics.filter {
                println(it.characteristicUuid)
                it.properties.read
            }.also {
                it.forEach {
                    println("Characteristics ${it.characteristicUuid}")
                    it.descriptors.forEach {
                        println("Descriptor ${it.descriptorUuid}")
                    }
                }
            }.getOrNull(0)
        } ?: throw BluetoothException.CharacteristicsNotFound

        val textToSend =
            "{\"ssid\":\"$ssid\",\"password\":\"$password\", \"token\":\"${token}\",\"id\":$id}"

        peripheral.write(
            characteristic = writeCharacteristics,
            textToSend.toByteArray(),
        )
        println("Write success, waiting for disconnect")

        /*        peripheral.state
                    .onEach { println(it.toString()) }
                    .filterIsInstance<State.Disconnected>()
                    .timeout(10.seconds)
                    .catch {
                        if (it is TimeoutCancellationException) {
                            throw BluetoothException.Timeout
                        } else {
                            throw it
                        }
                    }
                    .first()*/

        val response =
            peripheral.read(readCharacteristics).also {
                it.forEach {
                    print(it)
                    print(" ")
                }
            }.decodeToString().also(::println)

        println("Wybrałem ${readCharacteristics.characteristicUuid}")

        println("Response: $response")
        /*if (response != "OK") {
            throw BluetoothException.Other("Not ok")
        }*/
        peripheral.disconnect()
    }
}
