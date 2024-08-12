package com.theternal.data.repositories

import com.theternal.data.datasources.local.RecordDao
import com.theternal.domain.entities.RecordEntity
import com.theternal.domain.repositories.RecordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val recordDao: RecordDao
) : RecordRepository {

    override fun createRecord(record: RecordEntity) {
        recordDao.createRecord(record)
    }

    override fun getRecords(watcherId: Long): Flow<List<RecordEntity>> {
        return recordDao.getRecords(watcherId)
    }
}