package com.example.coinlibrary.domain.usecase.coindb

import com.example.coinlibrary.db.CoinEntity
import kotlinx.coroutines.flow.Flow

interface CoinDbUseCase {
    suspend fun insertCoins(list: List<CoinEntity>)

    fun searchInCoins(query: String): Flow<List<CoinEntity>>

    fun getCoin(id: String): Flow<CoinEntity>

    fun addFavorite(id: String, isFavorited: Boolean): Int

    fun getFavoritedCoins(): Flow<List<CoinEntity>>
}