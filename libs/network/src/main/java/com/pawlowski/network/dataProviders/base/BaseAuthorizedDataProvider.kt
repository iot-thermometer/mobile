package com.pawlowski.network.dataProviders.base

import com.pawlowski.network.datastore.TokenRepository
import com.pawlowski.network.service.ThermometerServiceProvider
import com.thermometer.proto.ThermometerServiceGrpcKt
import kotlinx.coroutines.flow.Flow

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

    suspend fun <REQ : Any, RESP : Any> authorizedFlow(
        method: suspend ThermometerServiceGrpcKt.ThermometerServiceCoroutineStub.(REQ) -> Flow<RESP>,
        request: REQ,
    ): Flow<RESP> = thermometerServiceProvider
        .invoke()
        .addTokenHeader(token = tokenRepository.getToken()?.token ?: throw Exception("Empty token"))
        .method(request)
}
