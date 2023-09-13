package com.example.coinlibrary.domain.response

import java.io.Serializable

data class RoiResponse(
    val times: Double,
    val currency: String,
    val percentage: Double
) : Serializable
