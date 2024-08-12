package com.theternal.data.datasources.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.theternal.domain.entities.WatcherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatcherDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun createWatcher(watcher: WatcherEntity)

    @Delete
    fun deleteWatcher(watcher: WatcherEntity)

    @Query("SELECT * FROM watchers")
    fun getAllWatchers(): Flow<List<WatcherEntity>>

    @Query("SELECT * FROM watchers")
    fun getAllWatchersSync(): List<WatcherEntity>

    @Query("SELECT * FROM watchers WHERE id = :id")
    fun getWatcher(id: Long): Flow<WatcherEntity>

    @Update
    fun updateWatcher(watcher: WatcherEntity)

}