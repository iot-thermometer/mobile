package com.pawlowski.temperaturemanager.data

import com.juul.kable.AndroidAdvertisement
import com.juul.kable.Scanner
import com.juul.kable.peripheral
import com.juul.kable.write
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

    fun getScannedDevices(): Flow<List<AndroidAdvertisement>> = scanner
        .advertisements
        .onStart {
            println("Started searching")
        }
        .map {
            advertisements[it.address] = it
            advertisements.values.toList()
        }

    suspend fun sendMessageToDevice(
        advertisement: AndroidAdvertisement,
        token: String,
        id: Long,
        ssid: String,
        password: String,
    ) {
        val peripheral = scope
            .peripheral(advertisement = advertisement) {
                onServicesDiscovered {
                    requestMtu(100)
                }
            }

        peripheral.connect()

        val characteristics = peripheral.services?.firstNotNullOfOrNull {
            it.characteristics.firstOrNull {
                it.properties.write
            }
        }!!

        val textToSend =
            "{\"ssid\":\"$ssid\",\"password\":\"$password\", \"token\":\"${token}\",\"id\":$id}"

        peripheral.write(
            characteristic = characteristics,
            textToSend.toByteArray(),
        )

        peripheral.disconnect()
    }
}
