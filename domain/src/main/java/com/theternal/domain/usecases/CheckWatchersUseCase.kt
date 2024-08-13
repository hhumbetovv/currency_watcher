package com.theternal.domain.usecases

import com.theternal.domain.entities.RecordEntity
import com.theternal.domain.repositories.CurrencyRepository
import com.theternal.domain.repositories.RecordRepository
import com.theternal.domain.repositories.WatcherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheckWatchersUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val watcherRepository: WatcherRepository,
    private val recordRepository: RecordRepository,
) {

    suspend operator fun invoke(): Int {
        val watchers = watcherRepository.getAllWatchersSync()
        var recordCount = 0

        watchers.filter { it.isActive }.forEach { watcher ->

            val exchangeValue = currencyRepository.exchange(
                watcher.amountCurrency,
                watcher.thresholdCurrency,
            )

            val currentValue = exchangeValue * watcher.amount

            if(currentValue != watcher.previousValue) {
                val maxExceeded = watcher.previousValue < watcher.threshold
                        && currentValue > watcher.threshold

                val minExceeded = watcher.previousValue > watcher.threshold
                        && currentValue < watcher.threshold

                if(maxExceeded || minExceeded) {
                    recordCount++
                    recordRepository.createRecord(
                        RecordEntity(
                            date = System.currentTimeMillis(),
                            maxExceeded = maxExceeded,
                            value = currentValue,
                            watcherId = watcher.id,
                            currency = watcher.thresholdCurrency
                        )
                    )
                    watcherRepository.updateWatcher(
                        watcher.copy(
                            previousValue = currentValue,
                            recordCount = watcher.recordCount + 1
                        )
                    )
                }
            }
        }

        return recordCount
    }
}