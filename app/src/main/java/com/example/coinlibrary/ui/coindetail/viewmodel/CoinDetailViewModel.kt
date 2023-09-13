package com.example.coinlibrary.ui.coindetail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinlibrary.data.base.NetworkResult
import com.example.coinlibrary.data.base.onError
import com.example.coinlibrary.data.base.onSuccess
import com.example.coinlibrary.data.model.CoinDetailItem
import com.example.coinlibrary.data.model.MarketItem
import com.example.coinlibrary.db.CoinEntity
import com.example.coinlibrary.domain.usecase.coindb.CoinDbUseCase
import com.example.coinlibrary.domain.usecase.crypto.CryptoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val useCase: CryptoUseCase,
    private val coinDbUseCase: CoinDbUseCase
) : ViewModel() {
    private val _coinId = MutableStateFlow<String?>(null)
    val coinId = _coinId.asStateFlow()

    private val _coin = MutableStateFlow<CoinEntity?>(null)
    val coin = _coin.asStateFlow()

    private val _marketResponse = MutableSharedFlow<NetworkResult<MarketItem>>()
    val marketResponse = _marketResponse.asSharedFlow()

    private val _detailResponse = MutableSharedFlow<NetworkResult<CoinDetailItem>>()
    val detailResponse = _detailResponse.asSharedFlow()


    fun getCoinFromDb(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            coinDbUseCase.getCoin(id).collect {
                _coin.emit(it)
            }
        }
    }

    fun getCoinMarket(id: String, days: String, interval: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            _marketResponse.emit(NetworkResult.Loading(isLoading = true))
            useCase.getCoinMarket(id, days, interval)
                .onSuccess {
                    _marketResponse.emit(NetworkResult.Loading(isLoading = false))
                    _marketResponse.emit(NetworkResult.Success(it))

                }
                .onError { errorCode, errorMessage ->
                    _marketResponse.emit(NetworkResult.Loading(isLoading = false))
                    _marketResponse.emit(NetworkResult.Error(errorCode, errorMessage))
                }
        }
    }

    fun getCoinDetail(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _detailResponse.emit(NetworkResult.Loading(isLoading = true))
            useCase.getCoinDetail(id)
                .onSuccess {
                    _detailResponse.emit(NetworkResult.Loading(isLoading = false))
                    _detailResponse.emit(NetworkResult.Success(it))

                }
                .onError { errorCode, errorMessage ->
                    _detailResponse.emit(NetworkResult.Loading(isLoading = false))
                    _detailResponse.emit(NetworkResult.Error(errorCode, errorMessage))
                }
        }
    }

    fun favoriteCoin(isFavorited: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            coin.value?.let { coinDbUseCase.addFavorite(it.id, isFavorited) }
        }
    }


    fun changeCoinIdValue(newValue: String) {
        _coinId.value = newValue
    }
}