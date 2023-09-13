package com.example.coinlibrary.di

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.coinlibrary.data.CryptoService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun provideAuthenticationOkhttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        application: Application
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(45L, TimeUnit.SECONDS)
            .writeTimeout(45L, TimeUnit.SECONDS)
            .readTimeout(45L, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(
                ChuckerInterceptor.Builder(application.applicationContext)
                    .collector(ChuckerCollector(application.applicationContext))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(false)
                    .build()
            )
            .build()
    }
    @Provides
    @Singleton
    fun provideBaseUrl() = CryptoService.BASE_URL

    @Provides
    @Singleton
    fun provideRetrofitBuilder(baseUrl: String): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().serializeNulls().create()
                )
            )
    }
}