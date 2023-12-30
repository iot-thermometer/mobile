package com.pawlowski.network.service

import com.pawlowski.network.IThermometerServiceProvider
import com.pawlowski.network.channel.IGetGrpcChannelUseCase
import com.thermometer.proto.ThermometerServiceGrpcKt
import javax.inject.Inject

internal class ThermometerServiceProvider @Inject constructor(
    private val getGrpcChannelUseCase: IGetGrpcChannelUseCase,
) : IThermometerServiceProvider {
    private val service by lazy {
        getGrpcChannelUseCase().let { channel ->
            ThermometerServiceGrpcKt.ThermometerServiceCoroutineStub(channel)
        }
    }

    override operator fun invoke(): ThermometerServiceGrpcKt.ThermometerServiceCoroutineStub =
        service
}
