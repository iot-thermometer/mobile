package com.pawlowski.notificationservice.synchronization

import com.google.firebase.messaging.FirebaseMessaging
import com.pawlowski.notificationservice.INotificationsDataProvider
import com.pawlowski.notificationservice.dataStore.IDeviceIdAndTokenDataStore
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NotificationTokenSynchronizer @Inject constructor(
    private val deviceIdAndTokenPreferences: IDeviceIdAndTokenDataStore,
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

    override fun deleteCurrentToken() {
        synchronized(this) {
            firebaseMessaging.deleteToken()
            deviceIdAndTokenPreferences.updateDeviceIdAndToken {
                it.copy(token = null)
            }
        }
    }

    private fun getOrCreateDeviceId(): String {
        val currentValue = deviceIdAndTokenPreferences.getDeviceIdAndToken()
        return if (currentValue.deviceId.isNullOrEmpty()) {
            val newId = UUID.randomUUID().toString()
            deviceIdAndTokenPreferences.updateDeviceIdAndToken {
                it.copy(deviceId = newId)
            }
            newId
        } else {
            currentValue.deviceId
        }
    }

    private fun isThisTokenSynchronizedWithServer(token: String): Boolean {
        val currentValue = deviceIdAndTokenPreferences.getDeviceIdAndToken()
        return currentValue.token == token
    }
}
