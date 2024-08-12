package com.theternal.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(
    tableName = "records",
    foreignKeys = [
        ForeignKey(
            entity = WatcherEntity::class,
            parentColumns = ["id"],
            childColumns = ["watcherId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["watcherId"])]
)
data class RecordEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long = 0,
    @ColumnInfo("watcherId")
    val watcherId: Long,
    @ColumnInfo("value")
    val value: BigDecimal,
    @ColumnInfo("currency")
    val currency: String,
    @ColumnInfo("max_exceeded")
    val maxExceeded: Boolean,
    @ColumnInfo("date")
    val date: Long
)