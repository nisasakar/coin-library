package com.example.coinlibrary.domain.mapper

import com.example.coinlibrary.data.model.CoinDetailItem
import com.example.coinlibrary.data.model.DescriptionItem
import com.example.coinlibrary.data.model.ImageItem
import com.example.coinlibrary.data.model.MarketItem
import com.example.coinlibrary.db.CoinEntity
import com.example.coinlibrary.domain.response.CoinDetailResponse
import com.example.coinlibrary.domain.response.CoinResponse
import com.example.coinlibrary.domain.response.DescriptionResponse
import com.example.coinlibrary.domain.response.ImageResponse
import com.example.coinlibrary.domain.response.MarketResponse

fun CoinResponse.toCoinEntity() = CoinEntity(
    id = id,
    symbol = symbol,
    name = name,
    image = image,
    currentPrice = current_price,
    changePercent = price_change_percentage_24h
)

fun MarketResponse.toMarketItem() = MarketItem(prices = prices)

fun CoinDetailResponse.toCoinDetailItem() = CoinDetailItem(
    name = name,
    symbol = symbol,
    description = description?.toDescriptionItem(),
    hashing_algorithm = hashing_algorithm,
    image = image?.toImageItem(),
    currentPrice = marketData?.current_price?.usd,
    changePercentage = marketData?.priceChancePercentage_24h
)

fun DescriptionResponse.toDescriptionItem() = DescriptionItem(en = en)

fun ImageResponse.toImageItem() = ImageItem(thumb = thumb, small = small, large = large)