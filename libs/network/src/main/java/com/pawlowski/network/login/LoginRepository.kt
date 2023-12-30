package com.pawlowski.network.login

import com.pawlowski.datastore.ITokenRepository
import com.pawlowski.network.ILoginRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LoginRepository @Inject constructor(
    private val loginDataProvider: LoginDataProvider,
    private val tokenRepository: ITokenRepository,
) : ILoginRepository {

    override suspend fun login(
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

    override suspend fun register(
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
