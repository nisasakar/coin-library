import app.cash.turbine.test
import com.example.coinlibrary.data.base.NetworkResult
import com.example.coinlibrary.data.base.onError
import com.example.coinlibrary.data.model.CoinItem
import com.example.coinlibrary.db.CoinEntity
import com.example.coinlibrary.domain.usecase.coindb.CoinDbUseCase
import com.example.coinlibrary.domain.usecase.crypto.CryptoUseCase
import com.example.coinlibrary.ui.coinlist.viewmodel.CoinListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class CoinListViewModelTest {

    private lateinit var viewModel: CoinListViewModel

    @Mock
    private lateinit var cryptoUseCase: CryptoUseCase

    @Mock
    private lateinit var coinDbUseCase: CoinDbUseCase


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = CoinListViewModel(cryptoUseCase, coinDbUseCase)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test getCoinsToDb success`() = runBlocking {
        val mockCoinsList = listOf(
            CoinEntity(
                id = "1",
                name = "Bitcoin",
                symbol = "btc",
                image = "",
                currentPrice = 0.0,
                changePercent = 0.0,
                isFavorited = false
            )
        )

        `when`(cryptoUseCase.getCoins()).thenReturn(NetworkResult.Success(mockCoinsList))

        viewModel.coinListResponse.test {
            viewModel.getCoinsToDb()

            assertEquals(NetworkResult.Loading<List<CoinEntity>>(true), awaitItem())
            assertEquals(NetworkResult.Loading<List<CoinEntity>>(false), awaitItem())
            awaitItem().map {
                assertEquals(NetworkResult.Success(mockCoinsList).data, it)
            }
        }
    }

    @Test
    fun `test getCoinsToDb error`() = runBlocking {
        val testErrorCode = 500
        val testErrorMessage = "Error message"
        `when`(cryptoUseCase.getCoins()).thenReturn(NetworkResult.Error(testErrorCode, testErrorMessage))

        viewModel.coinListResponse.test {
            viewModel.getCoinsToDb()

            assertEquals(NetworkResult.Loading<List<CoinEntity>>(true), awaitItem())
            assertEquals(NetworkResult.Loading<List<CoinEntity>>(false), awaitItem())
            awaitItem().onError { errorCode, errorMessage ->
                assertEquals(testErrorCode, errorCode)
                assertEquals(testErrorMessage, errorMessage)
            }

        }
    }

    @Test
    fun `test getCoinsBySearch`() = runBlocking {
        val query = "Bitcoin"
        val mockCoinEntityList = listOf(
            CoinEntity(
                id = "1",
                name = "Bitcoin",
                symbol = "btc",
                image = "",
                currentPrice = 0.0,
                changePercent = 0.0,
                isFavorited = false
            )
        )
        val mockCoinItemList = mockCoinEntityList.map { entity ->
            CoinItem(
                id = entity.id,
                name = entity.name,
                symbol = entity.symbol,
                image = entity.image,
                currentPrice = entity.currentPrice,
                changePercent = entity.changePercent
            )
        }
        `when`(coinDbUseCase.searchInCoins(query)).thenReturn(flowOf(mockCoinEntityList))


        viewModel.coinsFromDb.test {
            viewModel.getCoinsBySearch(query)

            assertEquals(mockCoinItemList, awaitItem())
        }
    }
}
