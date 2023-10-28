package com.pawlowski.temperaturemanager.data

import com.pawlowski.temperaturemanager.domain.models.Token
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class TokenRepository @Inject constructor() {
    private val token = MutableStateFlow<Token?>(null)

    suspend fun saveToken(newToken: Token) {
        token.value = newToken
    }

    suspend fun getToken(): Token? =
        token.value
}
