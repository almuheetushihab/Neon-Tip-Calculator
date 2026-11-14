package com.smallcodelab.neontipcalculator.di

import com.smallcodelab.neontipcalculator.data.preferences.PreferencesRepository
import com.smallcodelab.neontipcalculator.data.preferences.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module binding [PreferencesRepository] interface to [UserPreferencesRepository] implementation.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesModule {

    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(
        impl: UserPreferencesRepository
    ): PreferencesRepository
}
