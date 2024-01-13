package com.pawlowski.notificationservice.synchronization

import com.google.firebase.messaging.FirebaseMessaging
import com.pawlowski.notificationservice.IRunPushTokenSynchronizationUseCase
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class RunPushTokenSynchronizationUseCase
    @Inject
    constructor(
        private val notificationsSynchronizer: INotificationTokenSynchronizer,
        private val firebaseMessaging: FirebaseMessaging,
    ) : IRunPushTokenSynchronizationUseCase {
        override suspend operator fun invoke() {
            runCatching {
                val token = firebaseMessaging.token.await()
                notificationsSynchronizer.synchronizeWithServer(newToken = token)
            }.onFailure {
                coroutineContext.ensureActive()
                it.printStackTrace()
            }
        }
    }
