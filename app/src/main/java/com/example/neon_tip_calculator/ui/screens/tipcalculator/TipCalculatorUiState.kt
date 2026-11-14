package com.example.neon_tip_calculator.ui.screens.tipcalculator

import com.example.neon_tip_calculator.model.TipOption

data class TipCalculatorUiState(
    val billInput: String = "120",
    val selectedTip: TipOption = TipOption.FIFTEEN,
    val peopleCount: Int = 2,
    val isLoading: Boolean = false
) {
    val billAmount: Double
        get() = billInput.toDoubleOrNull() ?: 0.0

    val totalAmount: Double
        get() = billAmount * (1.0 + selectedTip.percentage / 100.0)

    val amountPerPerson: Double
        get() = if (peopleCount > 0) totalAmount / peopleCount else 0.0
}
