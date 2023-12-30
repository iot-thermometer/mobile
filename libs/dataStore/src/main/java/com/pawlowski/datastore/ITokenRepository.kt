package com.pawlowski.datastore

import com.pawlowski.datastore.authToken.Token

interface ITokenRepository {
    suspend fun saveToken(newToken: Token)

    suspend fun getToken(): Token?
}
