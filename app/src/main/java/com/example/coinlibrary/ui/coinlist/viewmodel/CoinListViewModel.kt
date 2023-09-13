package com.example.coinlibrary.ui.coinlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinlibrary.data.base.NetworkResult
import com.example.coinlibrary.data.base.onError
import com.example.coinlibrary.data.base.onSuccess
import com.example.coinlibrary.data.model.CoinItem
import com.example.coinlibrary.db.CoinEntity
import com.example.coinlibrary.domain.usecase.coindb.CoinDbUseCase
import com.example.coinlibrary.domain.usecase.crypto.CryptoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val cryptoUseCase: CryptoUseCase,
    private val coinDbUseCase: CoinDbUseCase
) :
    ViewModel() {
    private val _coinListResponse = MutableSharedFlow<NetworkResult<List<CoinEntity>>>()
    val coinListResponse = _coinListResponse.asSharedFlow()

    private val _coinsFromDb = MutableSharedFlow<List<CoinItem>>()
    val coinsFromDb = _coinsFromDb.asSharedFlow()

    fun getCoinsToDb() {
        viewModelScope.launch(Dispatchers.IO) {
            _coinListResponse.emit(NetworkResult.Loading(isLoading = true))
            cryptoUseCase.getCoins()
                .onSuccess {
                    _coinListResponse.emit(NetworkResult.Loading(isLoading = false))
                    _coinListResponse.emit(NetworkResult.Success(it))
                }
                .onError { errorCode, errorMessage ->
                    _coinListResponse.emit(NetworkResult.Loading(isLoading = false))
                    _coinListResponse.emit(NetworkResult.Error(errorCode, errorMessage))
                }
        }
    }

    fun insertCoinsToDb(list: List<CoinEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            coinDbUseCase.insertCoins(list)
        }
    }

    fun getCoinsBySearch(query: String) {
        viewModelScope.launch {
            coinDbUseCase.searchInCoins(query).collect { list ->
                _coinsFromDb.emit(list.map { entity ->
                    CoinItem(
                        id = entity.id,
                        name = entity.name,
                        symbol = entity.symbol,
                        image = entity.image,
                        currentPrice = entity.currentPrice,
                        changePercent = entity.changePercent
                    )
                })
            }
        }
    }
}