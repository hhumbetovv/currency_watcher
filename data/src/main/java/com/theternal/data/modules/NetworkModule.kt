package com.theternal.data.modules

import com.theternal.data.datasources.remote.CurrencyService
import com.theternal.data.interceptors.ApiKeyInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://currency-exchange.p.rapidapi.com"
    private const val TIMEOUT = 10L

    @Provides
    @IntoSet
    fun provideApiInterceptor(): Interceptor {
        return ApiKeyInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        interceptors: Set<@JvmSuppressWildcards Interceptor>
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)

        interceptors.forEach { builder.addInterceptor(it) }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        onHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(onHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCurrencyService(
        retrofit: Retrofit,
    ): CurrencyService {
        return retrofit.create(
            CurrencyService::class.java
        )
    }
}
