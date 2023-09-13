package com.example.coinlibrary.di

import com.example.coinlibrary.domain.repository.CryptoRepository
import com.example.coinlibrary.domain.repository.CryptoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideCryptoRepository(cryptoRepositoryImpl: CryptoRepositoryImpl): CryptoRepository

}