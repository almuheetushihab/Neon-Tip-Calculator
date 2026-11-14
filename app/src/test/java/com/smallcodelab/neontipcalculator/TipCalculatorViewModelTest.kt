package com.smallcodelab.neontipcalculator

import com.smallcodelab.neontipcalculator.model.SplitAction
import com.smallcodelab.neontipcalculator.model.TipOption
import com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.TipCalculatorViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class TipCalculatorViewModelTest {

    private lateinit var viewModel: TipCalculatorViewModel

    @Before
    fun setUp() {
        viewModel = TipCalculatorViewModel()
    }

    @Test
    fun defaultState_isCalculatedCorrectly() {
        val state = viewModel.uiState.value
        assertEquals("120", state.billInput)
        assertEquals(TipOption.FIFTEEN, state.selectedTip)
        assertEquals(2, state.peopleCount)
        assertEquals(BigDecimal("138.00"), state.totalAmount)
        assertEquals(BigDecimal("69.00"), state.amountPerPerson)
    }

    @Test
    fun validBillInput_updatesCalculationWithBigDecimal() {
        viewModel.onBillInputChanged("100")
        val state = viewModel.uiState.value
        assertEquals("100", state.billInput)
        assertEquals(BigDecimal("115.00"), state.totalAmount)
        assertEquals(BigDecimal("57.50"), state.amountPerPerson)
    }

    @Test
    fun emptyBillInput_handlesGracefullyAsZero() {
        viewModel.onBillInputChanged("")
        val state = viewModel.uiState.value
        assertEquals("", state.billInput)
        assertEquals(BigDecimal("0.00"), state.totalAmount)
        assertEquals(BigDecimal("0.00"), state.amountPerPerson)
    }

    @Test
    fun billInputWithMultipleDots_sanitizesToOneDot() {
        viewModel.onBillInputChanged("12.3.4")
        val state = viewModel.uiState.value
        assertEquals("12.3", state.billInput)
    }

    @Test
    fun billInputLimitedTo2DecimalPlacesAnd9Digits() {
        viewModel.onBillInputChanged("1234567890.999")
        val state = viewModel.uiState.value
        assertEquals("123456789.99", state.billInput)
    }

    @Test
    fun splitAction_incrementAndDecrement_boundsCheck() {
        viewModel.onSplitAction(SplitAction.INCREMENT)
        assertEquals(3, viewModel.uiState.value.peopleCount)

        viewModel.onSplitAction(SplitAction.DECREMENT)
        viewModel.onSplitAction(SplitAction.DECREMENT)
        viewModel.onSplitAction(SplitAction.DECREMENT) // Try below 1
        assertEquals(1, viewModel.uiState.value.peopleCount)
    }
}
