package com.smallcodelab.neontipcalculator.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a stored tip calculation history item.
 */
@Entity(tableName = "tip_history")
data class TipHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val billAmount: String,
    val tipPercentage: Int,
    val isCustomTip: Boolean,
    val peopleCount: Int,
    val roundUpPerPerson: Boolean,
    val currencyCode: String,
    val perPersonAmount: String,
    val totalAmount: String,
    val timestamp: Long = System.currentTimeMillis()
)
