package com.pawlowski.temperaturemanager.data.channel

import android.app.Application
import io.grpc.Channel
import io.grpc.android.AndroidChannelBuilder
import javax.inject.Inject

class GetGrpcChannelUseCase @Inject constructor(
    private val context: Application,
) {

    operator fun invoke(): Channel =
        AndroidChannelBuilder
            .forAddress("srv3.enteam.pl", 3010)
            .usePlaintext()
            .context(context.applicationContext)
            .build()
}
