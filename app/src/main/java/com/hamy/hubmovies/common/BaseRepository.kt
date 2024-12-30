package com.hamy.hubmovies.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import java.io.IOException

abstract class BaseRepository {
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<T>
    ): Flow<ApiState<T>> = flow {
        emit(ApiState.Loading)
        apiCall().apply {
            if (isSuccessful) {
                body()?.let {
                    emit(ApiState.Success(it))
                } ?: run {
                    emit(ApiState.Failure(IOException(errorBody()?.toString() ?: "Something went wrong...")))
                }
            } else {
                emit(ApiState.Failure(Throwable(errorBody().toString())))
            }
        }
    }.catch { e ->
        e.printStackTrace()
        emit(ApiState.Failure(Exception(e)))
    }.flowOn(Dispatchers.IO)
}