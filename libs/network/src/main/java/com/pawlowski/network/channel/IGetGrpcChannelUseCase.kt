package com.pawlowski.network.channel

import io.grpc.Channel

interface IGetGrpcChannelUseCase {
    operator fun invoke(): Channel
}
