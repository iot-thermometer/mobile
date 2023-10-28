package com.pawlowski.temperaturemanager.data.dataProviders

import com.pawlowski.temperaturemanager.data.service.ThermometerServiceProvider
import com.pawlowski.temperaturemanager.domain.models.Token
import com.thermometer.proto.LoginRequest
import com.thermometer.proto.RegisterRequest
import javax.inject.Inject

class LoginDataProvider @Inject constructor(
    private val thermometerServiceProvider: ThermometerServiceProvider,
) {
    suspend fun login(
        email: String,
        password: String,
    ): Token =
        thermometerServiceProvider().login(
            request = LoginRequest.newBuilder()
                .setEmail(email)
                .setPassword(password)
                .build(),
        ).let {
            Token(token = it.token)
        }

    suspend fun register(
        email: String,
        password: String,
    ): Token =
        thermometerServiceProvider().register(
            request = RegisterRequest.newBuilder()
                .setEmail(email)
                .setPassword(password)
                .build(),
        ).let {
            Token(token = it.token)
        }
}
