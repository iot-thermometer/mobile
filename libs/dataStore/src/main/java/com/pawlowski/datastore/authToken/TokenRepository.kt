package com.pawlowski.datastore.authToken

import androidx.datastore.core.DataStore
import com.pawlowski.datastore.ITokenRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TokenRepository @Inject constructor(
    private val dataStore: DataStore<TokenDataStoreModel>,
) : ITokenRepository {

    override suspend fun saveToken(newToken: Token) {
        dataStore.updateData {
            TokenDataStoreModel(token = newToken.token)
        }
    }

    override suspend fun getToken(): Token? = dataStore.data
        .first()
        .token
        ?.let(::Token)
}
