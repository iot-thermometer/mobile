package com.pawlowski.notificationservice.synchronization

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.pawlowski.datastore.IPushTokenRepository
import com.pawlowski.datastore.PushToken
import com.pawlowski.notificationservice.INotificationsDataProvider
import kotlinx.coroutines.ensureActive
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.coroutineContext

@Singleton
internal class NotificationTokenSynchronizer
    @Inject
    constructor(
        private val pushTokenRepository: IPushTokenRepository,
        private val notificationsDataProvider: INotificationsDataProvider,
        private val firebaseMessaging: FirebaseMessaging,
    ) : INotificationTokenSynchronizer {
        override suspend fun synchronizeWithServer(newToken: String) {
            if (!isThisTokenSynchronizedWithServer(newToken)) {
                val deviceId = getOrCreateDeviceId()

                runCatching {
                    notificationsDataProvider.addPushToken(
                        PushToken(
                            token = newToken,
                            deviceId = deviceId,
                        ),
                    )
                }.onFailure {
                    coroutineContext.ensureActive()
                    Log.d("NotificationTokenSynchronizer", "Error")
                }.onSuccess {
                    pushTokenRepository.updatePushToken {
                        it.copy(token = newToken)
                    }
                    Log.d("NotificationTokenSynchronizer", "Success")
                }
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

        private suspend fun createNewDeviceId(): String =
            UUID.randomUUID().toString()
                .also { newId ->
                    pushTokenRepository.updatePushToken {
                        it.copy(deviceId = newId)
                    }
                }

        private suspend fun isThisTokenSynchronizedWithServer(token: String): Boolean {
            val currentValue = pushTokenRepository.getPushToken()
            println("MOJ TAG IsSynchronized: ${currentValue.token == token} $token $currentValue")
            return currentValue.token == token
        }
    }
