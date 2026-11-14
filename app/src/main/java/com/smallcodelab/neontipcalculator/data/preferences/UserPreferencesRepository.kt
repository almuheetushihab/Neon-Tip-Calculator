package com.smallcodelab.neontipcalculator.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.smallcodelab.neontipcalculator.data.currency.CurrencyOption
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

/**
 * Value object holding user persisted preferences.
 */
data class UserSettings(
    val currency: CurrencyOption = CurrencyOption.USD,
    val lastTipPercentage: Int = 15,
    val roundUpEnabled: Boolean = false
)

/**
 * Interface defining operations for reading and persisting user preferences.
 */
interface PreferencesRepository {
    val userSettings: Flow<UserSettings>
    suspend fun updateCurrency(currency: CurrencyOption)
    suspend fun updateLastTip(percentage: Int)
    suspend fun updateRoundUp(enabled: Boolean)
}

/**
 * DataStore implementation of [PreferencesRepository].
 */
@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : PreferencesRepository {

    private object PreferencesKeys {
        val CURRENCY = stringPreferencesKey("currency_code")
        val LAST_TIP = intPreferencesKey("last_tip_percentage")
        val ROUND_UP = booleanPreferencesKey("round_up_enabled")
    }

    override val userSettings: Flow<UserSettings> = context.dataStore.data.map { preferences ->
        val currencyCode = preferences[PreferencesKeys.CURRENCY] ?: CurrencyOption.USD.code
        val currency = CurrencyOption.entries.find { it.code == currencyCode } ?: CurrencyOption.USD
        val lastTip = preferences[PreferencesKeys.LAST_TIP] ?: 15
        val roundUp = preferences[PreferencesKeys.ROUND_UP] ?: false

        UserSettings(
            currency = currency,
            lastTipPercentage = lastTip,
            roundUpEnabled = roundUp
        )
    }

    override suspend fun updateCurrency(currency: CurrencyOption) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.CURRENCY] = currency.code
        }
    }

    override suspend fun updateLastTip(percentage: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_TIP] = percentage
        }
    }

    override suspend fun updateRoundUp(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ROUND_UP] = enabled
        }
    }
}
