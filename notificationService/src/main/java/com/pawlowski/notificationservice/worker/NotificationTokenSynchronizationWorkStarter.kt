package com.pawlowski.notificationservice.worker

import androidx.work.*
import androidx.work.WorkManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NotificationTokenSynchronizationWorkStarter @Inject constructor(
    private val workManager: WorkManager,
) : INotificationTokenSynchronizationWorkStarter {
    override fun startWorker() {
        workManager
            .beginUniqueWork(
                "NotificationTokenSynchronizationWork",
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequestBuilder<NotificationTokenSynchronizationWorker>()
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build(),
                    )
                    .build(),
            ).enqueue()
    }
}
