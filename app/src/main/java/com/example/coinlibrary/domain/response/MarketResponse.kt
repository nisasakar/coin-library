package com.example.coinlibrary.domain.response

import java.io.Serializable

data class MarketResponse(
    val prices: List<DoubleArray>
) : Serializable
