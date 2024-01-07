package com.pawlowski.datastore.authToken

import androidx.datastore.core.DataStore
import com.pawlowski.datastore.AuthToken
import com.pawlowski.datastore.IAuthTokenRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AuthTokenRepository @Inject constructor(
    private val dataStore: DataStore<AuthTokenDataStoreModel>,
) : IAuthTokenRepository {

    override suspend fun saveToken(newToken: AuthToken) {
        dataStore.updateData {
            AuthTokenDataStoreModel(token = newToken.token)
        }
    }

    override suspend fun getToken(): AuthToken? = dataStore.data
        .first()
        .token
        ?.let(::AuthToken)

    override suspend fun removeToken() {
        dataStore.updateData {
            AuthTokenDataStoreModel(token = null)
        }
    }
}
