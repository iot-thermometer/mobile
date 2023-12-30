package com.pawlowski.notificationservice

interface INotificationsDataProvider {

    suspend fun sendMyNewToken()
}
