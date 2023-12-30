package com.pawlowski.network.service

import com.pawlowski.network.channel.IGetGrpcChannelUseCase
import com.thermometer.proto.ThermometerServiceGrpcKt
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThermometerServiceProvider @Inject constructor(
    private val getGrpcChannelUseCase: IGetGrpcChannelUseCase,
) {
    private val service by lazy {
        getGrpcChannelUseCase().let { channel ->
            ThermometerServiceGrpcKt.ThermometerServiceCoroutineStub(channel)
        }
    }

    operator fun invoke(): ThermometerServiceGrpcKt.ThermometerServiceCoroutineStub = service
}
