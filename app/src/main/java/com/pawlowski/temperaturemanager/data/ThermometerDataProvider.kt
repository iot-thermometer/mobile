package com.pawlowski.temperaturemanager.data

import com.pawlowski.network.dataProviders.base.BaseAuthorizedDataProvider
import com.pawlowski.network.datastore.TokenRepository
import com.pawlowski.network.service.ThermometerServiceProvider
import com.pawlowski.temperaturemanager.domain.models.DeviceDomain
import com.pawlowski.temperaturemanager.domain.models.ReadingDomain
import com.thermometer.proto.CreateDeviceRequest
import com.thermometer.proto.ListDevicesRequest
import com.thermometer.proto.ListReadingsRequest
import com.thermometer.proto.ThermometerServiceGrpcKt
import javax.inject.Inject

class ThermometerDataProvider @Inject constructor(
    thermometerServiceProvider: ThermometerServiceProvider,
    tokenRepository: TokenRepository,
) : BaseAuthorizedDataProvider(
    thermometerServiceProvider = thermometerServiceProvider,
    tokenRepository = tokenRepository,
) {

    suspend fun listDevices(): List<DeviceDomain> = authorizedUnary(
        method = ThermometerServiceGrpcKt.ThermometerServiceCoroutineStub::listDevices,
        request = ListDevicesRequest.newBuilder()
            .build(),
    ).devicesList.map {
        it.toDomain()
    }

    suspend fun listReadings(deviceId: Long): List<ReadingDomain> {
        return authorizedUnary(
            method = ThermometerServiceGrpcKt.ThermometerServiceCoroutineStub::listReadings,
            request = ListReadingsRequest.newBuilder()
                .setId(deviceId)
                .build(),
        ).readingsList.toDomain()
    }

    suspend fun createDevice(
        name: String,
        readingInterval: Int,
        pushInterval: Int,
    ): DeviceDomain = authorizedUnary(
        method = ThermometerServiceGrpcKt.ThermometerServiceCoroutineStub::createDevice,
        request = CreateDeviceRequest.newBuilder()
            .setName(name)
            .setPushInterval(pushInterval)
            .setReadingInterval(readingInterval)
            .build(),
    ).device.toDomain()
}
