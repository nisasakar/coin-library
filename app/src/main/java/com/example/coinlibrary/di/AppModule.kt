package com.example.coinlibrary.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.coinlibrary.data.CryptoService
import com.example.coinlibrary.db.CoinDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideApiService(
        okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder
    ): CryptoService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(CryptoService::class.java)

    @Provides
    @Singleton
    fun provideCoinDatabase(@ApplicationContext context: Context): CoinDatabase {
        return Room.databaseBuilder(context, CoinDatabase::class.java, "crypto_database").build()
    }

}