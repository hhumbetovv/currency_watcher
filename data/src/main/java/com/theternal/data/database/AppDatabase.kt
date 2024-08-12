package com.theternal.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.theternal.data.database.converters.BigDecimalConverter
import com.theternal.data.datasources.local.RecordDao
import com.theternal.data.datasources.local.WatcherDao
import com.theternal.domain.entities.RecordEntity
import com.theternal.domain.entities.WatcherEntity

@Database(
    entities = [
        WatcherEntity::class,
        RecordEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(BigDecimalConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun watcherDao() : WatcherDao
    abstract fun recordDao() : RecordDao
}