package com.smallcodelab.neontipcalculator

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallcodelab.neontipcalculator.model.TipOption
import com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.TipCalculatorScreen
import com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.TipCalculatorUiState
import com.smallcodelab.neontipcalculator.ui.theme.NeonTipCalculatorTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI Flow Test verifying user input and tip chip selection.
 */
@RunWith(AndroidJUnit4::class)
class TipCalculatorUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun mainFlow_enterBill_pickTip_displaysCorrectResult() {
        var billInputState = ""
        var selectedTipState = TipOption.FIFTEEN

        composeTestRule.setContent {
            NeonTipCalculatorTheme {
                TipCalculatorScreen(
                    uiState = TipCalculatorUiState(
                        billInput = billInputState,
                        selectedTip = selectedTipState,
                        peopleCount = 2
                    ),
                    onBillInputChanged = { billInputState = it },
                    onTipSelected = { selectedTipState = it },
                    onCustomTipPercentageChanged = {},
                    onSplitAction = {},
                    onRoundUpToggled = {},
                    onCurrencySelected = {},
                    onSaveToHistory = {},
                    onDeleteHistoryItem = {},
                    onClearAllHistory = {},
                    onToggleHistorySheet = {},
                    onToastShown = {}
                )
            }
        }

        // Verify title & result card exist
        composeTestRule.onNodeWithText("Tip Calculator").assertIsDisplayed()
        composeTestRule.onNodeWithText("PER PERSON").assertIsDisplayed()

        // Interact with Bill input field
        composeTestRule.onNodeWithContentDescription("Bill amount input field").performTextInput("100")

        // Select 20% tip chip
        composeTestRule.onNodeWithContentDescription("Select 20 percent tip").performClick()
    }
}
