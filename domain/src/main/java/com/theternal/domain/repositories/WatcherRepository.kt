package com.theternal.domain.repositories

import com.theternal.domain.entities.WatcherEntity
import kotlinx.coroutines.flow.Flow

interface WatcherRepository {

    fun createWatcher(watcher: WatcherEntity)

    fun deleteWatcher(watcher: WatcherEntity)

    fun getAllWatchers(): Flow<List<WatcherEntity>>

    fun getAllWatchersSync(): List<WatcherEntity>

    fun updateWatcher(watcher: WatcherEntity)

    fun getWatcher(id: Long): Flow<WatcherEntity>

}