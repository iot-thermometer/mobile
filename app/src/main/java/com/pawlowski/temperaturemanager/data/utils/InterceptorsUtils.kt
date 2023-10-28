package com.pawlowski.temperaturemanager.data.utils

import io.grpc.Metadata
import io.grpc.stub.AbstractStub
import io.grpc.stub.MetadataUtils

fun <T : AbstractStub<T>> T.addTokenHeader(token: String): T =
    this.withInterceptors(
        MetadataUtils.newAttachHeadersInterceptor(
            Metadata().apply {
                put(
                    Metadata.Key.of(
                        "authorization",
                        Metadata.ASCII_STRING_MARSHALLER,
                    ),
                    token,
                )
            },
        ),
    )
