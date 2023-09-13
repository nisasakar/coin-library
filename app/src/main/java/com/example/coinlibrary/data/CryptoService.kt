package com.example.coinlibrary.data

import com.example.coinlibrary.domain.response.CoinDetailResponse
import com.example.coinlibrary.domain.response.CoinResponse
import com.example.coinlibrary.domain.response.MarketResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CryptoService {
    companion object {
        const val BASE_URL = "https://api.coingecko.com/api/v3/"
    }

    @GET("coins/markets")
    suspend fun getCoins(
        @Query("vs_currency") currency: String = "usd"
    ): Response<List<CoinResponse>>

    @GET("coins/{id}/market_chart")
    suspend fun getCoinMarket(
        @Path("id") id: String,
        @Query("days") days: String,
        @Query("interval") interval: String?,
        @Query("vs_currency") currency: String = "usd"
    ): Response<MarketResponse>

    @GET("coins/{id}")
    suspend fun getCoinDetail(
        @Path("id") id: String
    ): Response<CoinDetailResponse>
}