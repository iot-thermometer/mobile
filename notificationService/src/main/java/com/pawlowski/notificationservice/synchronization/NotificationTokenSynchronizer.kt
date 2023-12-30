package com.pawlowski.notificationservice.synchronization

import com.google.firebase.messaging.FirebaseMessaging
import com.pawlowski.datastore.IPushTokenRepository
import com.pawlowski.notificationservice.INotificationsDataProvider
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NotificationTokenSynchronizer @Inject constructor(
    private val pushTokenRepository: IPushTokenRepository,
    private val notificationsDataProvider: INotificationsDataProvider,
    private val firebaseMessaging: FirebaseMessaging,
) : INotificationTokenSynchronizer {

    override suspend fun synchronizeWithServer(newToken: String): Result<Unit> {
        return if (!isThisTokenSynchronizedWithServer(newToken)) {
            val deviceId = getOrCreateDeviceId()
            TODO()
            /*val result = notificationsDataProvider.sendNotificationToken(
                token = newToken,
                deviceId = deviceId,
            )

            result.onSuccess {
                deviceIdAndTokenPreferences.updateDeviceIdAndToken {
                    it.copy(token = newToken)
                }
                Log.d("NotificationTokenSynchronizer", "Success")
            }.onError { _, _ ->
                Log.d("NotificationTokenSynchronizer", "Error")
            }*/
        } else {
            Result.success(Unit)
        }
    }

    override suspend fun deleteCurrentToken() {
        firebaseMessaging.deleteToken()
        pushTokenRepository.updatePushToken {
            it.copy(token = null)
        }
    }

    private suspend fun getOrCreateDeviceId(): String {
        return pushTokenRepository.getPushToken().deviceId?.ifEmpty {
            null
        } ?: createNewDeviceId()
    }

    private suspend fun createNewDeviceId(): String = UUID.randomUUID().toString()
        .also { newId ->
            pushTokenRepository.updatePushToken {
                it.copy(deviceId = newId)
            }
        }

    private suspend fun isThisTokenSynchronizedWithServer(token: String): Boolean {
        val currentValue = pushTokenRepository.getPushToken()
        return currentValue.token == token
    }
}
