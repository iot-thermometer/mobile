package com.pawlowski.temperaturemanager

import com.google.firebase.messaging.FirebaseMessagingService

internal class NotificationService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        // TODO: Send to server
    }
}
