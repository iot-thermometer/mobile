package com.pawlowski.network

import com.thermometer.proto.ThermometerServiceGrpcKt

interface IThermometerServiceProvider {
    operator fun invoke(): ThermometerServiceGrpcKt.ThermometerServiceCoroutineStub
}
