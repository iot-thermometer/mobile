package com.pawlowski.temperaturemanager.data

import com.juul.kable.Advertisement
import com.juul.kable.AndroidAdvertisement
import com.juul.kable.Scanner
import com.juul.kable.descriptorOf
import com.juul.kable.peripheral
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

private const val SERVICE_ID = ""
private const val CHARACTERISTICS_ID = ""
private const val DESCRIPTOR_ID = ""


@Singleton
internal class BLEManager @Inject constructor(

) {

    private val scanner by lazy {
        Scanner {  }
    }

    private val scope = CoroutineScope(Dispatchers.IO)

    private val advertisements = hashMapOf<String, AndroidAdvertisement>()

    fun getScannedDevices(): Flow<List<AndroidAdvertisement>> = scanner
            .advertisements
        .onStart {
            println("Started searching")
        }
        .onEach {
            println("Found $it")
        }
        .map {
            advertisements[it.address] = it
            advertisements.values.toList()
        }


    suspend fun connectToDevice(
        advertisement: AndroidAdvertisement,
        coroutineScope: CoroutineScope,
    ):String {
        val peripheral = coroutineScope
            .peripheral(advertisement = advertisement)

        peripheral.connect()

        /*val descriptor = descriptorOf(
            service = SERVICE_ID,
            characteristic = CHARACTERISTICS_ID,
            descriptor = DESCRIPTOR_ID,
        )

        peripheral.write(descriptor, "".toByteArray())*/
        val services = peripheral.services.toString()

        peripheral.disconnect()

        return services
    }
}