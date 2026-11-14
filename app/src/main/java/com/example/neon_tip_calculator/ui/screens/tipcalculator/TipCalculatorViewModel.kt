package com.example.neon_tip_calculator.ui.screens.tipcalculator

import androidx.lifecycle.ViewModel
import com.example.neon_tip_calculator.model.SplitAction
import com.example.neon_tip_calculator.model.TipOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TipCalculatorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TipCalculatorUiState())
    val uiState: StateFlow<TipCalculatorUiState> = _uiState.asStateFlow()

    fun onBillInputChanged(input: String) {
        val sanitized = input.filter { it.isDigit() || (it == '.') }
        _uiState.update { it.copy(billInput = sanitized) }
    }

    fun onTipSelected(tipOption: TipOption) {
        _uiState.update { it.copy(selectedTip = tipOption) }
    }

    fun onSplitAction(action: SplitAction) {
        _uiState.update { currentState ->
            when (action) {
                SplitAction.INCREMENT -> currentState.copy(peopleCount = currentState.peopleCount + 1)
                SplitAction.DECREMENT -> {
                    if (currentState.peopleCount > 1) {
                        currentState.copy(peopleCount = currentState.peopleCount - 1)
                    } else {
                        currentState
                    }
                }
            }
        }
    }
}
