package com.pawlowski.network.login

import com.pawlowski.datastore.IAuthTokenRepository
import com.pawlowski.network.ILoginRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LoginRepository @Inject constructor(
    private val loginDataProvider: LoginDataProvider,
    private val authTokenRepository: IAuthTokenRepository,
) : ILoginRepository {

    override suspend fun login(
        email: String,
        password: String,
    ) {
        loginDataProvider.login(
            email = email,
            password = password,
        ).also {
            authTokenRepository.saveToken(it)
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
            authTokenRepository.saveToken(it)
        }
    }
}
