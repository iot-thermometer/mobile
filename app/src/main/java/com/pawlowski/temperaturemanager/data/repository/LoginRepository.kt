package com.pawlowski.temperaturemanager.data.repository

import com.pawlowski.temperaturemanager.data.dataProviders.LoginDataProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    private val loginDataProvider: LoginDataProvider,
    private val tokenRepository: TokenRepository,
) {

    suspend fun login(
        email: String,
        password: String,
    ) {
        loginDataProvider.login(
            email = email,
            password = password,
        ).also {
            tokenRepository.saveToken(it)
        }
    }

    suspend fun register(
        email: String,
        password: String,
    ) {
        loginDataProvider.register(
            email = email,
            password = password,
        ).also {
            tokenRepository.saveToken(it)
        }
    }
}
