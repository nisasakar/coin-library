package com.example.coinlibrary.domain.repository

import com.example.coinlibrary.data.CryptoService
import com.example.coinlibrary.data.base.NetworkResult
import com.example.coinlibrary.data.base.safeApiCall
import com.example.coinlibrary.data.model.CoinDetailItem
import com.example.coinlibrary.data.model.MarketItem
import com.example.coinlibrary.db.CoinEntity
import com.example.coinlibrary.domain.mapper.toCoinDetailItem
import com.example.coinlibrary.domain.mapper.toCoinEntity
import com.example.coinlibrary.domain.mapper.toMarketItem
import javax.inject.Inject

class CryptoRepositoryImpl @Inject constructor(private val cryptoService: CryptoService) :
    CryptoRepository {
    override suspend fun getCoins(): NetworkResult<List<CoinEntity>> {
        return safeApiCall {
            cryptoService.getCoins()
        }.map { response ->
            response.map { coinResponse ->
                coinResponse.toCoinEntity()
            }
        }
    }

    override suspend fun getCoinMarket(
        id: String,
        days: String,
        interval: String?
    ): NetworkResult<MarketItem> {
        return safeApiCall {
            cryptoService.getCoinMarket(id, days, interval)
        }.map { response ->
            response.toMarketItem()
        }
    }

    override suspend fun getCoinDetail(id: String): NetworkResult<CoinDetailItem> {
        return safeApiCall {
            cryptoService.getCoinDetail(id)
        }.map { response ->
            response.toCoinDetailItem()
        }
    }
}