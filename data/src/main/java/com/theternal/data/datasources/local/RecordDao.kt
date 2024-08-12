package com.theternal.data.datasources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.theternal.domain.entities.RecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun createRecord(record: RecordEntity)

    @Query("SELECT * FROM records WHERE watcherId = :watcherId")
    fun getRecords(watcherId: Long): Flow<List<RecordEntity>>

}