package com.smallcodelab.neontipcalculator.ui.screens.tipcalculator

import androidx.lifecycle.ViewModel
import com.smallcodelab.neontipcalculator.model.SplitAction
import com.smallcodelab.neontipcalculator.model.TipOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * ViewModel managing the business logic and state of the Neon Tip Calculator screen.
 */
class TipCalculatorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TipCalculatorUiState())
    val uiState: StateFlow<TipCalculatorUiState> = _uiState.asStateFlow()

    init {
        recalculate(_uiState.value.billInput, _uiState.value.selectedTip, _uiState.value.peopleCount)
    }

    /**
     * Handles updates to the bill input text field with strict decimal validation.
     * Limits input to max 9 integer digits and 2 decimal places, preventing multiple dots.
     *
     * @param input Raw input text entered by user.
     */
    fun onBillInputChanged(input: String) {
        val sanitized = sanitizeInput(input)
        _uiState.update { currentState ->
            val newState = currentState.copy(billInput = sanitized)
            calculateState(newState)
        }
    }

    /**
     * Handles selection of a tip percentage option.
     *
     * @param tipOption The selected [TipOption].
     */
    fun onTipSelected(tipOption: TipOption) {
        _uiState.update { currentState ->
            val newState = currentState.copy(selectedTip = tipOption)
            calculateState(newState)
        }
    }

    /**
     * Handles incrementing or decrementing the split count.
     *
     * @param action The [SplitAction] performed.
     */
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

    /**
     * Recalculates total and per-person amounts using precise [BigDecimal] arithmetic.
     */
    private fun calculateState(state: TipCalculatorUiState): TipCalculatorUiState {
        val bill = state.billInput.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val tipPercent = BigDecimal(state.selectedTip.percentage)
        val tipAmount = bill.multiply(tipPercent).divide(BigDecimal(100), 2, RoundingMode.HALF_UP)
        val total = bill.add(tipAmount).setScale(2, RoundingMode.HALF_UP)
        val perPerson = if (state.peopleCount > 0) {
            total.divide(BigDecimal(state.peopleCount), 2, RoundingMode.HALF_UP)
        } else {
            BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
        }

        return state.copy(
            totalAmount = total,
            amountPerPerson = perPerson
        )
    }

    private fun recalculate(billInput: String, tipOption: TipOption, peopleCount: Int) {
        val initial = TipCalculatorUiState(
            billInput = billInput,
            selectedTip = tipOption,
            peopleCount = peopleCount
        )
        _uiState.value = calculateState(initial)
    }

    /**
     * Sanitizes bill amount string input:
     * - Filters out non-digits and non-dots.
     * - Allows at most 1 decimal point.
     * - Limits digits before decimal to 9.
     * - Limits decimal places to 2.
     * - Prevents invalid leading zeros (e.g. "05" -> "5").
     */
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
