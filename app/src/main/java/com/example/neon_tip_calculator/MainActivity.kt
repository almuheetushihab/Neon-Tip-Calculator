package com.example.neon_tip_calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.neon_tip_calculator.model.TipOption
import com.example.neon_tip_calculator.ui.screens.tipcalculator.TipCalculatorScreen
import com.example.neon_tip_calculator.ui.screens.tipcalculator.TipCalculatorUiState
import com.example.neon_tip_calculator.ui.screens.tipcalculator.TipCalculatorViewModel
import com.example.neon_tip_calculator.ui.theme.NeonTipCalculatorTheme

class MainActivity : ComponentActivity() {
    private val viewModel: TipCalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NeonTipCalculatorTheme {
                TipApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun TipApp(
    viewModel: TipCalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    TipCalculatorScreen(
        uiState = uiState,
        onBillInputChanged = viewModel::onBillInputChanged,
        onTipSelected = viewModel::onTipSelected,
        onSplitAction = viewModel::onSplitAction,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun TipAppPreview() {
    NeonTipCalculatorTheme {
        TipCalculatorScreen(
            uiState = TipCalculatorUiState(
                billInput = "120",
                selectedTip = TipOption.FIFTEEN,
                peopleCount = 2
            ),
            onBillInputChanged = {},
            onTipSelected = {},
            onSplitAction = {}
        )
    }
}
