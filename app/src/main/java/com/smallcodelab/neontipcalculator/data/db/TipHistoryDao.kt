package com.smallcodelab.neontipcalculator.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for accessing tip history items in Room database.
 */
@Dao
interface TipHistoryDao {

    @Query("SELECT * FROM tip_history ORDER BY timestamp DESC")
    @JvmSuppressWildcards
    fun getAllHistory(): Flow<List<TipHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(entity: TipHistoryEntity): Long

    @Delete
    suspend fun deleteHistory(entity: TipHistoryEntity): Int

    @Query("DELETE FROM tip_history")
    suspend fun clearAllHistory(): Int
}
