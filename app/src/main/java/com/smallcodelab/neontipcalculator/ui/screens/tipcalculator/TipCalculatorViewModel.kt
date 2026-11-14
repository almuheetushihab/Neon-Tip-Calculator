package com.smallcodelab.neontipcalculator.ui.screens.tipcalculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallcodelab.neontipcalculator.data.currency.CurrencyOption
import com.smallcodelab.neontipcalculator.data.db.TipHistoryDao
import com.smallcodelab.neontipcalculator.data.db.TipHistoryEntity
import com.smallcodelab.neontipcalculator.data.preferences.PreferencesRepository
import com.smallcodelab.neontipcalculator.model.SplitAction
import com.smallcodelab.neontipcalculator.model.TipOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

/**
 * ViewModel managing state, persistent preferences, and calculation history.
 */
@HiltViewModel
class TipCalculatorViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val historyDao: TipHistoryDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(TipCalculatorUiState())
    val uiState: StateFlow<TipCalculatorUiState> = _uiState.asStateFlow()

    init {
        observeSettings()
        observeHistory()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            preferencesRepository.userSettings.collect { settings ->
                _uiState.update { currentState ->
                    val matchingPreset = TipOption.entries.find { !it.isCustom && it.percentage == settings.lastTipPercentage }
                    val (tipOption, customValue) = if (matchingPreset != null) {
                        matchingPreset to currentState.customTipPercentage
                    } else {
                        TipOption.CUSTOM to settings.lastTipPercentage
                    }

                    val updated = currentState.copy(
                        selectedCurrency = settings.currency,
                        selectedTip = tipOption,
                        customTipPercentage = customValue,
                        roundUpPerPerson = settings.roundUpEnabled
                    )
                    calculateState(updated)
                }
            }
        }
    }

    private fun observeHistory() {
        viewModelScope.launch {
            historyDao.getAllHistory().collect { list ->
                _uiState.update { it.copy(historyList = list) }
            }
        }
    }

    fun onBillInputChanged(input: String) {
        val sanitized = sanitizeInput(input)
        _uiState.update { currentState ->
            val newState = currentState.copy(billInput = sanitized)
            calculateState(newState)
        }
    }

    fun onTipSelected(tipOption: TipOption) {
        viewModelScope.launch {
            if (!tipOption.isCustom) {
                preferencesRepository.updateLastTip(tipOption.percentage)
            } else {
                preferencesRepository.updateLastTip(_uiState.value.customTipPercentage)
            }
        }
        _uiState.update { currentState ->
            val newState = currentState.copy(selectedTip = tipOption)
            calculateState(newState)
        }
    }

    fun onCustomTipPercentageChanged(percentage: Int) {
        viewModelScope.launch {
            preferencesRepository.updateLastTip(percentage)
        }
        _uiState.update { currentState ->
            val newState = currentState.copy(
                selectedTip = TipOption.CUSTOM,
                customTipPercentage = percentage
            )
            calculateState(newState)
        }
    }

    fun onSplitAction(action: SplitAction) {
        _uiState.update { currentState ->
            val newCount = when (action) {
                SplitAction.INCREMENT -> currentState.peopleCount + 1
                SplitAction.DECREMENT -> (currentState.peopleCount - 1).coerceAtLeast(1)
            }
            val newState = currentState.copy(peopleCount = newCount)
            calculateState(newState)
        }
    }

    fun onRoundUpToggled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.updateRoundUp(enabled)
        }
        _uiState.update { currentState ->
            val newState = currentState.copy(roundUpPerPerson = enabled)
            calculateState(newState)
        }
    }

    fun onCurrencySelected(currency: CurrencyOption) {
        viewModelScope.launch {
            preferencesRepository.updateCurrency(currency)
        }
        _uiState.update { currentState ->
            val newState = currentState.copy(selectedCurrency = currency)
            calculateState(newState)
        }
    }

    fun onToggleHistorySheet(open: Boolean) {
        _uiState.update { it.copy(isHistorySheetOpen = open) }
    }

    fun onSaveToHistory() {
        val state = _uiState.value
        val entity = TipHistoryEntity(
            billAmount = state.billInput.ifBlank { "0.00" },
            tipPercentage = state.effectiveTipPercentage,
            isCustomTip = state.selectedTip.isCustom,
            peopleCount = state.peopleCount,
            roundUpPerPerson = state.roundUpPerPerson,
            currencyCode = state.selectedCurrency.code,
            perPersonAmount = state.amountPerPerson.toPlainString(),
            totalAmount = state.totalAmount.toPlainString()
        )
        viewModelScope.launch {
            historyDao.insertHistory(entity)
            _uiState.update { it.copy(toastMessage = "Saved to history!") }
        }
    }

    fun onDeleteHistoryItem(item: TipHistoryEntity) {
        viewModelScope.launch {
            historyDao.deleteHistory(item)
        }
    }

    fun onClearAllHistory() {
        viewModelScope.launch {
            historyDao.clearAllHistory()
        }
    }

    fun onToastShown() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    private fun calculateState(state: TipCalculatorUiState): TipCalculatorUiState {
        val bill = state.billInput.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val tipPercent = BigDecimal(state.effectiveTipPercentage)
        val tipAmount = bill.multiply(tipPercent).divide(BigDecimal(100), 2, RoundingMode.HALF_UP)
        val total = bill.add(tipAmount).setScale(2, RoundingMode.HALF_UP)

        var perPerson = if (state.peopleCount > 0) {
            total.divide(BigDecimal(state.peopleCount), 2, RoundingMode.HALF_UP)
        } else {
            BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
        }

        if (state.roundUpPerPerson && perPerson > BigDecimal.ZERO) {
            perPerson = perPerson.setScale(0, RoundingMode.CEILING).setScale(2, RoundingMode.HALF_UP)
        }

        return state.copy(
            totalAmount = total,
            amountPerPerson = perPerson
        )
    }

    private fun sanitizeInput(input: String): String {
        if (input.isBlank()) return ""

        val filtered = input.filter { it.isDigit() || it == '.' }
        val parts = filtered.split('.')
        val integerPart = parts[0].trimStart('0').let { if (it.isEmpty() && parts[0].isNotEmpty()) "0" else it }
        val limitedInteger = if (integerPart.length > 9) integerPart.substring(0, 9) else integerPart

        return if (parts.size > 1) {
            val decimalPart = parts[1].take(2)
            if (limitedInteger.isEmpty()) "0.$decimalPart" else "$limitedInteger.$decimalPart"
        } else {
            limitedInteger
        }
    }
}
