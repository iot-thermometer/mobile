package com.pawlowski.temperaturemanager.data.dataProviders

import com.pawlowski.temperaturemanager.data.TokenRepository
import com.pawlowski.temperaturemanager.data.dataProviders.base.BaseAuthorizedDataProvider
import com.pawlowski.temperaturemanager.data.service.ThermometerServiceProvider
import com.thermometer.proto.ListDevicesRequest
import com.thermometer.proto.ThermometerServiceGrpcKt
import javax.inject.Inject

class ThermometerDataProvider @Inject constructor(
    thermometerServiceProvider: ThermometerServiceProvider,
    tokenRepository: TokenRepository,
) : BaseAuthorizedDataProvider(
    thermometerServiceProvider = thermometerServiceProvider,
    tokenRepository = tokenRepository,
) {

    suspend fun listDevices(): String {
        return authorizedUnary(
            method = ThermometerServiceGrpcKt.ThermometerServiceCoroutineStub::listDevices,
            request = ListDevicesRequest.newBuilder()
                .build(),
        ).devicesList.toString()
    }
}
