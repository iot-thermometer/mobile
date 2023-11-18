package com.pawlowski.temperaturemanager.data.dataProviders

import com.pawlowski.temperaturemanager.data.dataProviders.base.BaseAuthorizedDataProvider
import com.pawlowski.temperaturemanager.data.repository.TokenRepository
import com.pawlowski.temperaturemanager.data.service.ThermometerServiceProvider
import com.pawlowski.temperaturemanager.data.toDomain
import com.pawlowski.temperaturemanager.domain.models.DeviceDomain
import com.pawlowski.temperaturemanager.domain.models.ReadingDomain
import com.thermometer.proto.CreateDeviceRequest
import com.thermometer.proto.ListDevicesRequest
import com.thermometer.proto.ListReadingsRequest
import com.thermometer.proto.ThermometerServiceGrpcKt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    suspend fun listReadings(deviceId: Long): Flow<List<ReadingDomain>> {
        return authorizedFlow(
            method = ThermometerServiceGrpcKt.ThermometerServiceCoroutineStub::listReadings,
            request = ListReadingsRequest.newBuilder()
                .setId(deviceId)
                .build(),
        ).map {
            it.readingsList.toDomain()
        }
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
