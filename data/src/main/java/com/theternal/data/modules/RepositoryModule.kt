package com.theternal.data.modules

import com.theternal.data.repositories.CurrencyRepositoryImpl
import com.theternal.data.repositories.RecordRepositoryImpl
import com.theternal.data.repositories.WatcherRepositoryImpl
import com.theternal.domain.repositories.CurrencyRepository
import com.theternal.domain.repositories.RecordRepository
import com.theternal.domain.repositories.WatcherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun provideCurrencyRepository(
        currencyRepositoryImpl: CurrencyRepositoryImpl
    ): CurrencyRepository

    @Binds
    fun provideWatcherRepository(
        watcherRepository: WatcherRepositoryImpl
    ): WatcherRepository

    @Binds
    fun provideRecordRepository(
        recordRepository: RecordRepositoryImpl
    ): RecordRepository

}
