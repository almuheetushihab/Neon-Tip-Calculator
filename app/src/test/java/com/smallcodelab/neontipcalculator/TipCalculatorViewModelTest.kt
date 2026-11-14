package com.smallcodelab.neontipcalculator

import com.smallcodelab.neontipcalculator.data.currency.CurrencyOption
import com.smallcodelab.neontipcalculator.data.db.TipHistoryDao
import com.smallcodelab.neontipcalculator.data.db.TipHistoryEntity
import com.smallcodelab.neontipcalculator.data.preferences.PreferencesRepository
import com.smallcodelab.neontipcalculator.data.preferences.UserSettings
import com.smallcodelab.neontipcalculator.model.SplitAction
import com.smallcodelab.neontipcalculator.model.TipOption
import com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.TipCalculatorViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

@OptIn(ExperimentalCoroutinesApi::class)
class TipCalculatorViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakePreferencesRepository: FakePreferencesRepository
    private lateinit var fakeHistoryDao: FakeHistoryDao
    private lateinit var viewModel: TipCalculatorViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakePreferencesRepository = FakePreferencesRepository()
        fakeHistoryDao = FakeHistoryDao()
        viewModel = TipCalculatorViewModel(fakePreferencesRepository, fakeHistoryDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun defaultState_isCalculatedCorrectly() {
        val state = viewModel.uiState.value
        assertEquals("120", state.billInput)
        assertEquals(TipOption.FIFTEEN, state.selectedTip)
        assertEquals(2, state.peopleCount)
        assertEquals(CurrencyOption.USD, state.selectedCurrency)
        assertEquals(BigDecimal("138.00"), state.totalAmount)
        assertEquals(BigDecimal("69.00"), state.amountPerPerson)
    }

    @Test
    fun customTipSlider_calculatesCorrectly() {
        viewModel.onCustomTipPercentageChanged(20)
        val state = viewModel.uiState.value
        assertEquals(TipOption.CUSTOM, state.selectedTip)
        assertEquals(20, state.effectiveTipPercentage)
        assertEquals(BigDecimal("144.00"), state.totalAmount)
        assertEquals(BigDecimal("72.00"), state.amountPerPerson)
    }

    @Test
    fun roundUpToggle_roundsPerPersonUpToInteger() {
        viewModel.onBillInputChanged("100")
        viewModel.onTipSelected(TipOption.FIFTEEN) // Total $115.00
        viewModel.onSplitAction(SplitAction.INCREMENT) // 3 people -> $38.33 per person
        assertEquals(BigDecimal("38.33"), viewModel.uiState.value.amountPerPerson)

        viewModel.onRoundUpToggled(true)
        assertEquals(BigDecimal("39.00"), viewModel.uiState.value.amountPerPerson)
    }

    @Test
    fun currencySelection_updatesSelectedCurrency() {
        viewModel.onCurrencySelected(CurrencyOption.BDT)
        assertEquals(CurrencyOption.BDT, viewModel.uiState.value.selectedCurrency)
    }

    @Test
    fun saveToHistory_insertsIntoDao() = runTest {
        viewModel.onSaveToHistory()
        assertEquals(1, fakeHistoryDao.insertedItems.size)
        assertEquals("Saved to history!", viewModel.uiState.value.toastMessage)
    }

    @Test
    fun clearAllHistory_clearsDaoItems() = runTest {
        viewModel.onSaveToHistory()
        viewModel.onClearAllHistory()
        assertTrue(fakeHistoryDao.insertedItems.isEmpty())
    }
}

private class FakePreferencesRepository : PreferencesRepository {
    private val settingsFlow = MutableStateFlow(UserSettings())
    override val userSettings: Flow<UserSettings> = settingsFlow

    override suspend fun updateCurrency(currency: CurrencyOption) {
        settingsFlow.value = settingsFlow.value.copy(currency = currency)
    }

    override suspend fun updateLastTip(percentage: Int) {
        settingsFlow.value = settingsFlow.value.copy(lastTipPercentage = percentage)
    }

    override suspend fun updateRoundUp(enabled: Boolean) {
        settingsFlow.value = settingsFlow.value.copy(roundUpEnabled = enabled)
    }
}

private class FakeHistoryDao : TipHistoryDao {
    val insertedItems = mutableListOf<TipHistoryEntity>()
    private val historyFlow = MutableStateFlow<List<TipHistoryEntity>>(emptyList())

    override fun getAllHistory(): Flow<List<TipHistoryEntity>> = historyFlow

    override suspend fun insertHistory(entity: TipHistoryEntity): Long {
        insertedItems.add(entity)
        historyFlow.value = insertedItems.toList()
        return entity.id
    }

    override suspend fun deleteHistory(entity: TipHistoryEntity): Int {
        insertedItems.remove(entity)
        historyFlow.value = insertedItems.toList()
        return 1
    }

    override suspend fun clearAllHistory(): Int {
        val count = insertedItems.size
        insertedItems.clear()
        historyFlow.value = emptyList()
        return count
    }
}
