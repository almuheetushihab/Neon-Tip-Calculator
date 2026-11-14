package com.smallcodelab.neontipcalculator.ui.screens.tipcalculator

import com.smallcodelab.neontipcalculator.data.currency.CurrencyOption
import com.smallcodelab.neontipcalculator.data.db.TipHistoryEntity
import com.smallcodelab.neontipcalculator.model.TipOption
import java.math.BigDecimal

/**
 * UI state for the Neon Tip Calculator screen.
 *
 * @property billInput Raw input text typed by the user.
 * @property selectedTip Selected tip option (preset or custom).
 * @property customTipPercentage Custom tip percentage selected via slider.
 * @property peopleCount Number of people splitting the bill.
 * @property roundUpPerPerson Whether per-person amount is rounded up to nearest integer.
 * @property selectedCurrency Selected currency option (USD, EUR, GBP, BDT).
 * @property totalAmount Calculated total amount using [BigDecimal].
 * @property amountPerPerson Calculated amount per person using [BigDecimal].
 * @property historyList List of saved calculation items from Room database.
 * @property isHistorySheetOpen State of modal bottom sheet for history.
 * @property toastMessage Toast message text to display once.
 */
data class TipCalculatorUiState(
    val billInput: String = "120",
    val selectedTip: TipOption = TipOption.FIFTEEN,
    val customTipPercentage: Int = 18,
    val peopleCount: Int = 2,
    val roundUpPerPerson: Boolean = false,
    val selectedCurrency: CurrencyOption = CurrencyOption.USD,
    val totalAmount: BigDecimal = BigDecimal("138.00"),
    val amountPerPerson: BigDecimal = BigDecimal("69.00"),
    val historyList: List<TipHistoryEntity> = emptyList(),
    val isHistorySheetOpen: Boolean = false,
    val toastMessage: String? = null
) {
    /**
     * Effective tip percentage used in calculations.
     */
    val effectiveTipPercentage: Int
        get() = if (selectedTip.isCustom) customTipPercentage else selectedTip.percentage
}
