package com.example.coinlibrary.ui.favorites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinlibrary.db.CoinEntity
import com.example.coinlibrary.domain.usecase.coindb.CoinDbUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val coinDbUseCase: CoinDbUseCase) :
    ViewModel() {
    private val _favoritedCoins = MutableSharedFlow<List<CoinEntity>>()
    val favoritedCoins = _favoritedCoins.asSharedFlow()

    fun getFavoritedCoins() {
        viewModelScope.launch(Dispatchers.IO) {
            coinDbUseCase.getFavoritedCoins().collect {
                _favoritedCoins.emit(it)
            }
        }
    }

    fun removeCoinFromFavorites(coinId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            coinDbUseCase.addFavorite(coinId, false)
        }
    }
}