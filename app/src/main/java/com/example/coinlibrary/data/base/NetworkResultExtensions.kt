package com.example.coinlibrary.data.base


suspend fun <T : Any> NetworkResult<T>.onSuccess(
    executable: suspend (T) -> Unit
): NetworkResult<T> = apply {
    if (this is NetworkResult.Success<T>) {
        executable(data)
    }
}

suspend fun <T : Any> NetworkResult<T>.onError(
    executable: suspend (errorCode: Int, errorMessage: String?) -> Unit
): NetworkResult<T> = apply {
    if (this is NetworkResult.Error<T>) {
        executable(errorCode ?: 0, errorMessage)
    }
}