package com.pawlowski.datastore

interface IAuthTokenRepository {
    suspend fun saveToken(newToken: AuthToken)

    suspend fun getToken(): AuthToken?

    suspend fun removeToken()
}
