package com.theternal.data.repositories

import com.theternal.data.datasources.local.WatcherDao
import com.theternal.domain.entities.WatcherEntity
import com.theternal.domain.repositories.WatcherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WatcherRepositoryImpl @Inject constructor(
    private val watcherDao: WatcherDao
) : WatcherRepository {

    override fun createWatcher(watcher: WatcherEntity) {
        watcherDao.createWatcher(watcher)
    }

    override fun deleteWatcher(watcher: WatcherEntity) {
        watcherDao.deleteWatcher(watcher)
    }

    override fun getAllWatchers(): Flow<List<WatcherEntity>> {
        return watcherDao.getAllWatchers()
    }

    override fun getAllWatchersSync():List<WatcherEntity> {
        return watcherDao.getAllWatchersSync()
    }

    override fun updateWatcher(watcher: WatcherEntity) {
        return watcherDao.updateWatcher(watcher)
    }

    override fun getWatcher(id: Long): Flow<WatcherEntity> {
        return watcherDao.getWatcher(id)
    }
}