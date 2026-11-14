package com.smallcodelab.neontipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.TipCalculatorScreen
import com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.TipCalculatorUiState
import com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.TipCalculatorViewModel
import com.smallcodelab.neontipcalculator.ui.theme.NeonTipCalculatorTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity host for the Neon Tip Calculator app with splash screen & Hilt DI support.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NeonTipCalculatorTheme {
                TipApp()
            }
        }
    }
}

/**
 * Top-level composable binding ViewModel state to [TipCalculatorScreen].
 *
 * @param viewModel Hilt injected ViewModel instance.
 * @param modifier Composable modifier.
 */
@Composable
fun TipApp(
    viewModel: TipCalculatorViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    TipCalculatorScreen(
        uiState = uiState,
        onBillInputChanged = viewModel::onBillInputChanged,
        onTipSelected = viewModel::onTipSelected,
        onCustomTipPercentageChanged = viewModel::onCustomTipPercentageChanged,
        onSplitAction = viewModel::onSplitAction,
        onRoundUpToggled = viewModel::onRoundUpToggled,
        onCurrencySelected = viewModel::onCurrencySelected,
        onSaveToHistory = viewModel::onSaveToHistory,
        onDeleteHistoryItem = viewModel::onDeleteHistoryItem,
        onClearAllHistory = viewModel::onClearAllHistory,
        onToggleHistorySheet = viewModel::onToggleHistorySheet,
        onToastShown = viewModel::onToastShown,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun TipAppPreview() {
    NeonTipCalculatorTheme {
        TipCalculatorScreen(
            uiState = TipCalculatorUiState(),
            onBillInputChanged = {},
            onTipSelected = {},
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
