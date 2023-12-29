package com.pawlowski.notificationservice.channelHandler

import android.app.NotificationChannel
import android.app.NotificationManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NotificationChannelHandler @Inject constructor(
    private val notificationManager: NotificationManager,
) : INotificationChannelHandler {
    private val channels by lazy {
        listOf(
            MyNotificationChannel(
                channelId = "ReadingMessage",
                tittle = "Odczyty z czujników",
                description = "Przychodzi powiadomienie gdy dostajemy odczyty z czujników",
            ),
        )
    }

    override fun innitNotificationChannels() {
        channels.forEach {
            val name = it.tittle
            val descriptionText = it.description
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(it.channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }
    }
}
