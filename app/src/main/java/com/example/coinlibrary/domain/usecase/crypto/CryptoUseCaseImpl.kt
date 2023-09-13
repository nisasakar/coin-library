package com.example.coinlibrary.domain.usecase.crypto

import com.example.coinlibrary.data.base.NetworkResult
import com.example.coinlibrary.data.model.CoinDetailItem
import com.example.coinlibrary.data.model.MarketItem
import com.example.coinlibrary.db.CoinEntity
import com.example.coinlibrary.domain.repository.CryptoRepository
import javax.inject.Inject

class CryptoUseCaseImpl @Inject constructor(private val cryptoRepository: CryptoRepository) :
    CryptoUseCase {
    override suspend fun getCoins(): NetworkResult<List<CoinEntity>> = cryptoRepository.getCoins()
    override suspend fun getCoinMarket(
        id: String,
        days: String,
        interval: String?
    ): NetworkResult<MarketItem> = cryptoRepository.getCoinMarket(id, days, interval)

    override suspend fun getCoinDetail(id: String): NetworkResult<CoinDetailItem> =
        cryptoRepository.getCoinDetail(id)
}