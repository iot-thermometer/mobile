package com.pawlowski.temperaturemanager.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retryWhen

sealed class Resource<out T> {
    object Loading : Resource<Nothing>()

    data class Success<T>(val data: T) : Resource<T>()

    data class Error(val throwable: Throwable) : Resource<Nothing>()
}

fun <T> Resource<T>.getDataOrNull() = (this as? Resource.Success)?.data

inline fun <T> resourceFlow(crossinline action: suspend () -> T): Flow<Resource<T>> =
    flow {
        emit(Resource.Loading)

        emit(Resource.Success(action()))
    }.catch {
        it.printStackTrace()
        emit(Resource.Error(it))
    }.flowOn(Dispatchers.IO)

interface RetrySharedFlow {
    fun sendRetryEvent()

    suspend fun waitForRetry()
}

private class RetrySharedFlowImpl : RetrySharedFlow {
    private val sharedFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    override fun sendRetryEvent() {
        sharedFlow.tryEmit(Unit)
    }

    override suspend fun waitForRetry() {
        sharedFlow.first()
    }
}

fun RetrySharedFlow(): RetrySharedFlow = RetrySharedFlowImpl()

inline fun <T> resourceFlowWithRetrying(
    retrySharedFlow: RetrySharedFlow,
    crossinline action: suspend () -> T,
): Flow<Resource<T>> =
    flow {
        emit(Resource.Loading)
        emit(Resource.Success(action()))
    }.retryWhen { cause, _ ->
        cause.printStackTrace()
        emit(Resource.Error(cause))
        retrySharedFlow.waitForRetry()
        true
    }.flowOn(Dispatchers.IO)
