package com.pawlowski.datastore.authToken

import androidx.datastore.core.DataStore
import com.pawlowski.datastore.AuthToken
import com.pawlowski.datastore.ITokenRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TokenRepository @Inject constructor(
    private val dataStore: DataStore<TokenDataStoreModel>,
) : ITokenRepository {

    override suspend fun saveToken(newToken: AuthToken) {
        dataStore.updateData {
            TokenDataStoreModel(token = newToken.token)
        }
    }

    override suspend fun getToken(): AuthToken? = dataStore.data
        .first()
        .token
        ?.let(::AuthToken)
}
