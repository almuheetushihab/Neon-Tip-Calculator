package com.smallcodelab.neontipcalculator.di

import android.content.Context
import androidx.room.Room
import com.smallcodelab.neontipcalculator.data.db.AppDatabase
import com.smallcodelab.neontipcalculator.data.db.TipHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing Room database and DAO dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "neon_tip_calculator.db"
        ).build()
    }

    @Provides
    fun provideTipHistoryDao(database: AppDatabase): TipHistoryDao {
        return database.tipHistoryDao()
    }
}
