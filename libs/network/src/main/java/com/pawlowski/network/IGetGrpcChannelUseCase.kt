package com.pawlowski.network

import io.grpc.Channel

interface IGetGrpcChannelUseCase {
    operator fun invoke(): Channel
}
