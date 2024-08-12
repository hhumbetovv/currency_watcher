package com.theternal.data.modules

import android.content.Context
import androidx.room.Room
import com.theternal.data.database.AppDatabase
import com.theternal.data.datasources.local.RecordDao
import com.theternal.data.datasources.local.WatcherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room
            .databaseBuilder(
                context, AppDatabase::class.java, "app-database"
            )
            .build()
    }


    @Provides
    @Singleton
    fun provideWatcherDao(
        appDatabase: AppDatabase
    ): WatcherDao {
        return appDatabase.watcherDao()
    }


    @Provides
    @Singleton
    fun provideRecordDao(
        appDatabase: AppDatabase
    ): RecordDao {
        return appDatabase.recordDao()
    }

}