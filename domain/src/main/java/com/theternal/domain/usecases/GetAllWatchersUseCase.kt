package com.theternal.domain.usecases

import android.util.Log
import com.theternal.domain.entities.WatcherEntity
import com.theternal.domain.repositories.WatcherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllWatchersUseCase @Inject constructor(
    private val watcherRepository: WatcherRepository
) {

    operator fun invoke(): Flow<List<WatcherEntity>> {
        return watcherRepository.getAllWatchers()
    }
}