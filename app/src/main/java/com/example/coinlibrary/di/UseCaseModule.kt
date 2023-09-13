package com.example.coinlibrary.di

import com.example.coinlibrary.domain.usecase.coindb.CoinDbUseCase
import com.example.coinlibrary.domain.usecase.coindb.CoinDbUseCaseImpl
import com.example.coinlibrary.domain.usecase.crypto.CryptoUseCase
import com.example.coinlibrary.domain.usecase.crypto.CryptoUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun provideCryptoUseCase(cryptoUseCaseImpl: CryptoUseCaseImpl): CryptoUseCase

    @Binds
    abstract fun provideCoinDbUseCase(coinDbUseCaseImpl: CoinDbUseCaseImpl): CoinDbUseCase
}