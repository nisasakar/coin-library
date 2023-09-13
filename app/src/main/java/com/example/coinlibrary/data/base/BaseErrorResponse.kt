package com.example.coinlibrary.data.base

import java.io.Serializable

data class BaseErrorResponse(
    val status: ErrorResponse
) : Serializable

data class ErrorResponse(
    val error_code: Int,
    val error_message: String
) : Serializable
