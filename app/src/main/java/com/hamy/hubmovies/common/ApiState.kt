package com.hamy.hubmovies.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

sealed class ApiState<out T> {
    data class Success<out R>(val data: R) : ApiState<R>()
    data class Failure(val msg: Throwable) : ApiState<Nothing>()
    object Loading : ApiState<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success $data"
            is Failure -> "Failure $msg"
            Loading -> "Loading"
        }
    }
}

fun <T, R> ApiState<T>.map(transform: (T) -> R): ApiState<R> {
    return when (this) {
        is ApiState.Success -> ApiState.Success(transform(data))
        is ApiState.Failure -> ApiState.Failure(msg)
        ApiState.Loading -> ApiState.Loading
    }
}

fun <T> Flow<ApiState<T>>. doOnSuccess(action: suspend (T) -> Unit): Flow<ApiState<T>> =
    transform { value ->
        if (value is ApiState.Success) {
            action(value.data)
        }
        return@transform emit(value)
    }

fun <T> Flow<ApiState<T>>.doOnFailure(action: suspend (Throwable?) -> Unit): Flow<ApiState<T>> =
    transform { value ->
        if (value is ApiState.Failure) {
            action(value.msg)
        }
        return@transform emit(value)
    }

fun <T> Flow<ApiState<T>>.doOnLoading(action: suspend () -> Unit): Flow<ApiState<T>> =
    transform { value ->
        if (value is ApiState.Loading) {
            action()
        }
        return@transform emit(value)
    }