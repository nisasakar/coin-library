package com.example.coinlibrary.domain.usecase.coindb

import com.example.coinlibrary.db.CoinDbRepository
import com.example.coinlibrary.db.CoinEntity
import javax.inject.Inject

class CoinDbUseCaseImpl @Inject constructor(private val coinDbRepository: CoinDbRepository) :
    CoinDbUseCase {
    override suspend fun insertCoins(list: List<CoinEntity>) = coinDbRepository.insertCoins(list)

    override fun searchInCoins(query: String) = coinDbRepository.searchInCoins(query)

    override fun getCoin(id: String) = coinDbRepository.getCoin(id)

    override fun addFavorite(id: String, isFavorited: Boolean) =
        coinDbRepository.addFavorite(id, isFavorited)

    override fun getFavoritedCoins() = coinDbRepository.getFavoritedCoins()
}