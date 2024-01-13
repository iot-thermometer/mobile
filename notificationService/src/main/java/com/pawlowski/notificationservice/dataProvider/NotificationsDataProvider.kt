package com.pawlowski.notificationservice.dataProvider

import com.pawlowski.datastore.IAuthTokenRepository
import com.pawlowski.datastore.PushToken
import com.pawlowski.network.BaseAuthorizedDataProvider
import com.pawlowski.network.IThermometerServiceProvider
import com.pawlowski.notificationservice.INotificationsDataProvider
import com.thermometer.proto.AddPhoneRequest
import com.thermometer.proto.RemovePhoneRequest
import com.thermometer.proto.ThermometerServiceGrpcKt
import javax.inject.Inject

internal class NotificationsDataProvider
    @Inject
    constructor(
        thermometerServiceProvider: IThermometerServiceProvider,
        tokenRepository: IAuthTokenRepository,
    ) : BaseAuthorizedDataProvider(
            thermometerServiceProvider = thermometerServiceProvider,
            authTokenRepository = tokenRepository,
        ),
        INotificationsDataProvider {
        override suspend fun addPushToken(pushToken: PushToken) {
            authorizedUnary(
                method = ThermometerServiceGrpcKt.ThermometerServiceCoroutineStub::addPhone,
                request =
                    AddPhoneRequest.newBuilder()
                        .setPushID(pushToken.deviceId)
                        .setPushToken(pushToken.deviceId)
                        .build(),
            ).also {
                println("Sent token $pushToken")
            }
        }

        override suspend fun removePushToken(pushToken: PushToken) {
            authorizedUnary(
                method = ThermometerServiceGrpcKt.ThermometerServiceCoroutineStub::removePhone,
                request =
                    RemovePhoneRequest.newBuilder()
                        .setPushID(pushToken.deviceId)
                        .build(),
            )
        }
    }
