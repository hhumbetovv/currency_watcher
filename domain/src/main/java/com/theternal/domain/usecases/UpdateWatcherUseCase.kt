package com.theternal.domain.usecases

import com.theternal.domain.entities.WatcherEntity
import com.theternal.domain.repositories.WatcherRepository
import javax.inject.Inject

class UpdateWatcherUseCase @Inject constructor(
    private val watcherRepository: WatcherRepository
) {

    operator fun invoke(watcher: WatcherEntity) {
        watcherRepository.updateWatcher(watcher)

    }
}