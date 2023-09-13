package com.example.coinlibrary.domain.response

import java.io.Serializable

data class MarketDataResponse(
    val current_price: CurrentPriceResponse?,
    val priceChancePercentage_24h: Double?
) : Serializable
