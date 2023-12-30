package com.pawlowski.datastore

interface ITokenRepository {
    suspend fun saveToken(newToken: AuthToken)

    suspend fun getToken(): AuthToken?
}
