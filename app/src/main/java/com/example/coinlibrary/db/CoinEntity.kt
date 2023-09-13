package com.example.coinlibrary.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coins")
data class CoinEntity(
    @PrimaryKey @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "symbol")
    val symbol: String,
    @ColumnInfo(name = "image")
    val image: String,
    @ColumnInfo(name = "current_price")
    val currentPrice: Double,
    @ColumnInfo(name = "change_percent")
    val changePercent: Double,
    @ColumnInfo(name = "isFavorited")
    var isFavorited: Boolean = false
)
