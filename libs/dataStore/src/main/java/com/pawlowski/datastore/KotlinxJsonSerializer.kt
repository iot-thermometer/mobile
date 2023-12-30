package com.pawlowski.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

abstract class KotlinxJsonSerializer<T>(
    private val kSerializer: KSerializer<T>,
) : Serializer<T> {

    override suspend fun readFrom(input: InputStream): T = withContext(Dispatchers.IO) {
        try {
            Json.decodeFromString(kSerializer, input.readBytes().decodeToString())
        } catch (e: SerializationException) {
            throw CorruptionException(
                message = "SerializationException",
                cause = e,
            )
        }
    }

    override suspend fun writeTo(t: T, output: OutputStream) = withContext(Dispatchers.IO) {
        output.write(Json.encodeToString(kSerializer, t).encodeToByteArray())
    }
}
