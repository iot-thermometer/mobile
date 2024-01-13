package com.pawlowski.notificationservice.di

import com.pawlowski.notificationservice.INotificationsDataProvider
import com.pawlowski.notificationservice.IRunPushTokenSynchronizationUseCase
import com.pawlowski.notificationservice.dataProvider.NotificationsDataProvider
import com.pawlowski.notificationservice.synchronization.RunPushTokenSynchronizationUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NotificationServiceBinds {
    @Binds
    abstract fun notificationsDataProvider(notificationsDataProvider: NotificationsDataProvider): INotificationsDataProvider

    @Binds
    abstract fun runPushTokenSynchronizationUseCase(
        runPushTokenSynchronizationUseCase: RunPushTokenSynchronizationUseCase,
    ): IRunPushTokenSynchronizationUseCase
}
