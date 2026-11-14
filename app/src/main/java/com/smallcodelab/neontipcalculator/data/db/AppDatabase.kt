package com.smallcodelab.neontipcalculator.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Room database instance managing calculation history.
 */
@Database(entities = [TipHistoryEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tipHistoryDao(): TipHistoryDao
}
