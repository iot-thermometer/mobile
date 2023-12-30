package com.pawlowski.notificationservice.di

import android.app.NotificationManager
import android.content.Context
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessaging
import com.pawlowski.notificationservice.channelHandler.INotificationChannelHandler
import com.pawlowski.notificationservice.channelHandler.NotificationChannelHandler
import com.pawlowski.notificationservice.synchronization.INotificationTokenSynchronizer
import com.pawlowski.notificationservice.synchronization.NotificationTokenSynchronizer
import com.pawlowski.notificationservice.worker.INotificationTokenSynchronizationWorkStarter
import com.pawlowski.notificationservice.worker.NotificationTokenSynchronizationWorkStarter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NotificationServiceModule {
    @Singleton
    @Provides
    internal fun synchronizer(notificationTokenSynchronizer: NotificationTokenSynchronizer): INotificationTokenSynchronizer =
        notificationTokenSynchronizer

    /*    @Singleton
        @Provides
        internal fun preferences(deviceIdAndTokenPreferences: DeviceIdAndTokenDataStore): IDeviceIdAndTokenDataStore = deviceIdAndTokenPreferences*/

    @Singleton
    @Provides
    fun firebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()

    @Singleton
    @Provides
    internal fun notificationTokenSynchronizationWorkStarter(ntsWorkStarter: NotificationTokenSynchronizationWorkStarter): INotificationTokenSynchronizationWorkStarter =
        ntsWorkStarter

    @Singleton
    @Provides
    fun notificationManager(appContext: Context): NotificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Singleton
    @Provides
    internal fun notificationChannelHandler(notificationChannelHandler: NotificationChannelHandler): INotificationChannelHandler =
        notificationChannelHandler

    @Singleton
    @Provides
    internal fun workManager(appContext: Context): WorkManager {
        return WorkManager.getInstance(appContext)
    }
}
