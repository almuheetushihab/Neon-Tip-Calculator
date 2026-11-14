package com.smallcodelab.neontipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.smallcodelab.neontipcalculator.model.TipOption
import com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.TipCalculatorScreen
import com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.TipCalculatorUiState
import com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.TipCalculatorViewModel
import com.smallcodelab.neontipcalculator.ui.theme.NeonTipCalculatorTheme

/**
 * Main activity host for the Neon Tip Calculator app.
 */
class MainActivity : ComponentActivity() {
    private val viewModel: TipCalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NeonTipCalculatorTheme {
                TipApp(viewModel = viewModel)
            }
        }
    }
}

/**
 * Top-level composable binding ViewModel state to [TipCalculatorScreen].
 *
 * @param viewModel ViewModel holding calculation state and logic.
 * @param modifier Composable modifier.
 */
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
