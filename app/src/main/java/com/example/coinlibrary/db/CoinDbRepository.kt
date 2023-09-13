package com.example.coinlibrary.db

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoinDbRepository @Inject internal constructor(coinDatabase: CoinDatabase) {
    private val coinDao = coinDatabase.coinDao()

    suspend fun insertCoins(list: List<CoinEntity>) = coinDao.insertCoins(list)

    fun searchInCoins(query: String) = coinDao.searchInCoins(query)

    fun getCoin(id: String) = coinDao.getCoin(id)

    fun addFavorite(id: String, isFavorited: Boolean) = coinDao.addFavorite(id, isFavorited)

    fun getFavoritedCoins() = coinDao.getFavoritedCoins()
}