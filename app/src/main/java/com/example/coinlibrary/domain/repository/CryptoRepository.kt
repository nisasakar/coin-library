package com.example.coinlibrary.domain.repository

import com.example.coinlibrary.data.base.NetworkResult
import com.example.coinlibrary.data.model.CoinDetailItem
import com.example.coinlibrary.data.model.MarketItem
import com.example.coinlibrary.db.CoinEntity

interface CryptoRepository {

    suspend fun getCoins(): NetworkResult<List<CoinEntity>>

    suspend fun getCoinMarket(
        id: String,
        days: String,
        interval: String?
    ): NetworkResult<MarketItem>

    suspend fun getCoinDetail(id: String): NetworkResult<CoinDetailItem>
}