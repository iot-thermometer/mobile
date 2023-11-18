package com.pawlowski.temperaturemanager.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

sealed class Resource<out T> {
    object Loading : Resource<Nothing>()

    data class Success<T>(val data: T) : Resource<T>()

    data class Error(val throwable: Throwable) : Resource<Nothing>()
}

fun <T> Resource<T>.getDataOrNull() = (this as? Resource.Success)?.data

suspend inline fun <T> resourceFlow(
    crossinline action: suspend () -> T,
): Flow<Resource<T>> = flow {
    emit(Resource.Loading)

    emit(Resource.Success(action()))
}.catch {
    emit(Resource.Error(it))
}.flowOn(Dispatchers.IO)
