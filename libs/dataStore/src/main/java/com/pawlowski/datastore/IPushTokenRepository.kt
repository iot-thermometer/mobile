package com.pawlowski.datastore

interface IPushTokenRepository {
    suspend fun updatePushToken(calculateNewValue: (PushToken) -> PushToken)
    suspend fun getPushToken(): PushToken
}
