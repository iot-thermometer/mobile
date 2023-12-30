package com.pawlowski.notificationservice.synchronization

interface INotificationTokenSynchronizer {
    suspend fun synchronizeWithServer(newToken: String): Result<Unit>
    suspend fun deleteCurrentToken()
}
