package com.theternal.domain.repositories

import com.theternal.domain.entities.RecordEntity
import kotlinx.coroutines.flow.Flow

interface RecordRepository {

    fun createRecord(record: RecordEntity)

    fun getRecords(watcherId: Long): Flow<List<RecordEntity>>

}