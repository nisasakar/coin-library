package com.example.coinlibrary.data.model

data class CoinDetailItem(
    val name: String,
    val symbol: String,
    val description: DescriptionItem?,
    val hashing_algorithm: String?,
    val image: ImageItem?,
    val currentPrice: Double?,
    val changePercentage: Double?
)
