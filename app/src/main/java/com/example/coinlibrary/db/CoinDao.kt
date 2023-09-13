package com.example.coinlibrary.db

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {
    suspend fun insertCoins(list: List<CoinEntity>) {
        list.forEach {
            try {
                insertCoin(it)
            } catch (e: SQLiteConstraintException) {
                updateCoin(
                    id = it.id,
                    currentPrice = it.currentPrice,
                    changePercent = it.changePercent
                )
            }
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCoin(entity: CoinEntity)

    @Query("UPDATE coins SET current_price = :currentPrice, change_percent =:changePercent WHERE id = :id")
    fun updateCoin(
        id: String,
        currentPrice: Double,
        changePercent: Double
    ): Int

    @Query("SELECT * FROM coins WHERE name LIKE '%' || :query || '%' OR symbol LIKE '%' || :query || '%'")
    fun searchInCoins(query: String): Flow<List<CoinEntity>>

    @Query("SELECT * FROM coins WHERE id =:id")
    fun getCoin(id: String): Flow<CoinEntity>

    @Query("UPDATE coins SET isFavorited = :isFavorited WHERE id = :id")
    fun addFavorite(id: String, isFavorited: Boolean): Int

    @Query("SELECT * FROM coins WHERE isFavorited == 1")
    fun getFavoritedCoins(): Flow<List<CoinEntity>>
}