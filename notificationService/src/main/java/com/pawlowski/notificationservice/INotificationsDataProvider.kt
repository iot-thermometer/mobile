package com.pawlowski.notificationservice

import com.pawlowski.datastore.PushToken

interface INotificationsDataProvider {
    suspend fun addPushToken(pushToken: PushToken)

    suspend fun removePushToken(pushToken: PushToken)
}
