package com.example.coinlibrary.data.base

sealed class NetworkResult<out R : Any> {
    inline fun <T : Any> map(transform: (R) -> T): NetworkResult<T> {
        return when (this) {
            is Success -> Success(transform(data))
            is Error -> Error(errorCode, errorMessage)
            is Loading -> Loading(isLoading)
        }
    }

    class Success<T : Any>(val data: T) : NetworkResult<T>()
    class Error<T : Any>(val errorCode: Int?, val errorMessage: String? = null) : NetworkResult<T>()
    data class Loading<T : Any>(val isLoading: Boolean) : NetworkResult<T>()
}
