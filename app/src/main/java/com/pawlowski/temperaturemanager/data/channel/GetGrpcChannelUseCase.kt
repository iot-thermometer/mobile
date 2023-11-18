package com.pawlowski.temperaturemanager.data.channel

import android.app.Application
import io.grpc.Channel
import io.grpc.okhttp.OkHttpChannelBuilder
import javax.inject.Inject
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class GetGrpcChannelUseCase @Inject constructor(
    private val context: Application,
) {

    operator fun invoke(): Channel =
        OkHttpChannelBuilder
            .forAddress("srv3.enteam.pl", 3010)
            .sslSocketFactory(noTrustedContext().socketFactory)
            .hostnameVerifier { _, _ -> true }
            .build()

    private fun noTrustedContext(): SSLContext {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                return arrayOf()
            }

            override fun checkClientTrusted(
                certs: Array<java.security.cert.X509Certificate>,
                authType: String,
            ) {
            }

            override fun checkServerTrusted(
                certs: Array<java.security.cert.X509Certificate>,
                authType: String,
            ) {
            }
        })

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())

        return sslContext
    }
}
