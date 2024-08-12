package com.theternal.domain.usecases

import com.theternal.domain.entities.RecordEntity
import com.theternal.domain.entities.WatcherEntity
import com.theternal.domain.repositories.RecordRepository
import com.theternal.domain.repositories.WatcherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetWatcherDetailsUseCase @Inject constructor(
    private val watcherRepository: WatcherRepository,
    private val recordRepository: RecordRepository
) {

    operator fun invoke(id: Long): Flow<Pair<WatcherEntity, List<RecordEntity>>> {
        return combine(
            watcherRepository.getWatcher(id),
            recordRepository.getRecords(id)
        ) { watcher, records -> Pair(watcher, records) }
    }
}