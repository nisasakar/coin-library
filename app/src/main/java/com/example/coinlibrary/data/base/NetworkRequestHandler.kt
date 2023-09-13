package com.example.coinlibrary.data.base

import com.google.gson.Gson
import retrofit2.HttpException
import retrofit2.Response

suspend fun <T : Any> safeApiCall(
    execute: suspend () -> Response<T>
): NetworkResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            NetworkResult.Success(body)
        } else if (response.errorBody() != null) {
            val res = response.errorBody()?.string()
            val errorResponse = convertErrorBody(res)
            NetworkResult.Error(
                errorCode = errorResponse?.status?.error_code,
                errorMessage = errorResponse?.status?.error_message
            )
        } else {
            NetworkResult.Error(
                errorCode = response.code(),
                errorMessage = response.message()
            )
        }
    } catch (e: HttpException) {
        NetworkResult.Error(
            errorCode = e.code(),
            errorMessage = e.message()
        )
    } catch (e: Throwable) {
        NetworkResult.Error(null, errorMessage = e.message)
    }
}


private fun convertErrorBody(res: String?): BaseErrorResponse? {
    return try {
        res.let {
            Gson().fromJson(it, BaseErrorResponse::class.java)
        }
    } catch (exception: Exception) {
        null
    }
}