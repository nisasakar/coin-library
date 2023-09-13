package com.example.coinlibrary.domain.response

import java.io.Serializable

data class CoinDetailResponse(
    val name: String,
    val symbol: String,
    val description: DescriptionResponse?,
    val hashing_algorithm: String?,
    val image: ImageResponse?,
    val marketData: MarketDataResponse?
) : Serializable
