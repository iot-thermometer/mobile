package com.pawlowski.temperaturemanager.data.dataProviders.base

import com.pawlowski.temperaturemanager.data.TokenRepository
import com.pawlowski.temperaturemanager.data.service.ThermometerServiceProvider
import com.pawlowski.temperaturemanager.data.utils.addTokenHeader
import com.thermometer.proto.ThermometerServiceGrpcKt

abstract class BaseAuthorizedDataProvider(
    private val tokenRepository: TokenRepository,
    private val thermometerServiceProvider: ThermometerServiceProvider,
) {
    suspend fun <REQ : Any, RESP : Any> authorizedUnary(
        method: suspend ThermometerServiceGrpcKt.ThermometerServiceCoroutineStub.(REQ) -> RESP,
        request: REQ,
    ): RESP = thermometerServiceProvider
        .invoke()
        .addTokenHeader(token = tokenRepository.getToken()?.token ?: throw Exception("Empty token"))
        .method(request)
}
