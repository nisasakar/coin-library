package com.example.coinlibrary.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@SmallTest
@ExperimentalCoroutinesApi
class CoinDaoTest {
    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: CoinDatabase
    private lateinit var coinDao: CoinDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CoinDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        coinDao = database.coinDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertCoins() = runBlocking {
        val coinsList = listOf(
            CoinEntity("1", "Bitcoin", "BTC", "", 45000.0, 5.0),
            CoinEntity("2", "Ethereum", "ETH", "", 3500.0, 3.0)
        )

        coinDao.insertCoins(coinsList)

        val list = coinDao.searchInCoins("").first()
        assertEquals(list, coinsList)
    }

    @Test
    fun testInsertCoin() = runBlocking {
        val coin = CoinEntity(
            id = "bitcoin",
            name = "Bitcoin",
            symbol = "BTC",
            image = "",
            changePercent = 0.0,
            currentPrice = 0.0,
            isFavorited = false
        )

        coinDao.insertCoin(coin)

        val loaded = coinDao.getCoin("bitcoin").firstOrNull()

        assertNotNull(loaded)
        assertEquals(coin.id, loaded?.id)
        assertEquals(coin.name, loaded?.name)
        assertEquals(coin.symbol, loaded?.symbol)
    }

    @Test
    fun updateCoin() = runBlocking {
        val coin = CoinEntity(
            id = "ethereum",
            name = "Ethereum",
            symbol = "ETH",
            image = "",
            changePercent = 0.0,
            currentPrice = 0.0
        )

        coinDao.insertCoin(coin)

        val newPrice = 2000.0
        val newChangePercent = 5.0

        coinDao.updateCoin("ethereum", newPrice, newChangePercent)

        val updatedCoin = coinDao.getCoin("ethereum").firstOrNull()
        assertNotNull(updatedCoin)
        assertEquals(newPrice, updatedCoin?.currentPrice)
        assertEquals(newChangePercent, updatedCoin?.changePercent)
    }

    @Test
    fun testSearchInCoins() = runBlocking {
        // Arrange
        val coinsList = listOf(
            CoinEntity("1", "Bitcoin", "BTC", "", 45000.0, 5.0),
            CoinEntity("2", "Ethereum", "ETH", "", 3500.0, 3.0),
            CoinEntity("3", "Cardano", "ADA", "", 2.0, 2.0)
        )

        coinDao.insertCoins(coinsList)

        // Act
        val query = "Bitcoin"
        val searchedCoins = coinDao.searchInCoins(query).first()

        // Assert
        assert(searchedCoins.size == 1)
        assert(searchedCoins[0].name == "Bitcoin")
    }

    @Test
    fun getCoinById() = runBlocking {
        val coin = CoinEntity(
            id = "bitcoin",
            name = "Bitcoin",
            symbol = "BTC",
            image = "",
            currentPrice = 0.0,
            changePercent = 0.0
        )
        coinDao.insertCoin(coin)

        val loadedCoin = coinDao.getCoin("bitcoin").firstOrNull()

        assertNotNull(loadedCoin)
        assertEquals(coin.id, loadedCoin?.id)
        assertEquals(coin.name, loadedCoin?.name)
        assertEquals(coin.symbol, loadedCoin?.symbol)
    }

    @Test
    fun addFavoriteById() = runBlocking {
        val coin = CoinEntity(
            id = "bitcoin",
            name = "Bitcoin",
            symbol = "BTC",
            image = "",
            currentPrice = 0.0,
            changePercent = 0.0
        )

        coinDao.insertCoin(coin)

        val newFavoritedValue = true
        coinDao.addFavorite(id = "bitcoin", isFavorited = newFavoritedValue)
        var updatedCoin = coinDao.getCoin("bitcoin").firstOrNull()
        assertEquals(newFavoritedValue, updatedCoin?.isFavorited)

        val newUnfavoritedValue = false
        coinDao.addFavorite(id = "bitcoin", isFavorited = newUnfavoritedValue)
        updatedCoin = coinDao.getCoin("bitcoin").firstOrNull()
        assertEquals(newUnfavoritedValue, updatedCoin?.isFavorited)
    }

    @Test
    fun getFavoritedCoins() = runBlocking {
        coinDao.insertCoins(
            listOf(
                CoinEntity(
                    id = "bitcoin",
                    name = "Bitcoin",
                    symbol = "BTC",
                    image = "",
                    changePercent = 0.0,
                    currentPrice = 0.0,
                    isFavorited = true
                ),
                CoinEntity(
                    id = "ethereum",
                    name = "Ethereum",
                    symbol = "ETH",
                    image = "",
                    changePercent = 0.0,
                    currentPrice = 0.0,
                    isFavorited = true
                ),
                CoinEntity(
                    id = "doge",
                    name = "DogeCoin",
                    symbol = "DOGE",
                    image = "",
                    changePercent = 0.0,
                    currentPrice = 0.0,
                    isFavorited = false
                )
            )
        )

        val favoritedCoins = coinDao.getFavoritedCoins().firstOrNull()

        assertNotNull(favoritedCoins)
        assertEquals(2, favoritedCoins?.size)
        assertTrue(favoritedCoins?.all { it.isFavorited } == true)
    }
}
