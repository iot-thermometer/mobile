package com.pawlowski.temperaturemanager.data.repository

import androidx.datastore.core.DataStore
import com.pawlowski.temperaturemanager.data.datastore.token.TokenDataStoreModel
import com.pawlowski.temperaturemanager.domain.models.Token
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepository @Inject constructor(
    private val dataStore: DataStore<TokenDataStoreModel>,
) {

    suspend fun saveToken(newToken: Token) {
        dataStore.updateData {
            TokenDataStoreModel(token = newToken.token)
        }
    }

    suspend fun getToken(): Token? = dataStore.data
        .first()
        .token
        ?.let(::Token)
}
