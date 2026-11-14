package com.smallcodelab.neontipcalculator.ui.screens.tipcalculator

import com.smallcodelab.neontipcalculator.model.TipOption
import java.math.BigDecimal

/**
 * State representing the tip calculator screen UI state.
 *
 * @property billInput Raw text typed by the user in the bill amount text field.
 * @property selectedTip Currently chosen tip option.
 * @property peopleCount Number of people splitting the bill (must be >= 1).
 * @property isLoading Loading state indicator.
 * @property totalAmount Calculated total bill including tip using [BigDecimal].
 * @property amountPerPerson Calculated amount to be paid per person using [BigDecimal].
 */
data class TipCalculatorUiState(
    val billInput: String = "120",
    val selectedTip: TipOption = TipOption.FIFTEEN,
    val peopleCount: Int = 2,
    val isLoading: Boolean = false,
    val totalAmount: BigDecimal = BigDecimal("138.00"),
    val amountPerPerson: BigDecimal = BigDecimal("69.00")
)
