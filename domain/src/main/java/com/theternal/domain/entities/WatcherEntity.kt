package com.theternal.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "watchers")
data class WatcherEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "amount_currency")
    val amountCurrency: String,
    @ColumnInfo(name = "threshold_currency")
    val thresholdCurrency: String,
    @ColumnInfo(name = "amount")
    val amount: BigDecimal,
    @ColumnInfo(name = "threshold")
    val threshold: BigDecimal,
    @ColumnInfo(name = "previous_value")
    val previousValue: BigDecimal,
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,
    @ColumnInfo(name = "record_count")
    val recordCount: Int = 0
)