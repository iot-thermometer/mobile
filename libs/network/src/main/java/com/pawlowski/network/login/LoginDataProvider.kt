package com.pawlowski.network.login

import com.pawlowski.datastore.authToken.Token
import com.pawlowski.network.IThermometerServiceProvider
import com.thermometer.proto.LoginRequest
import com.thermometer.proto.RegisterRequest
import javax.inject.Inject

internal class LoginDataProvider @Inject constructor(
    private val thermometerServiceProvider: IThermometerServiceProvider,
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
