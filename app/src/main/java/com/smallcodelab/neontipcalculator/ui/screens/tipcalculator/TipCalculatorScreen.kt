package com.smallcodelab.neontipcalculator.ui.screens.tipcalculator

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallcodelab.neontipcalculator.R
import com.smallcodelab.neontipcalculator.data.currency.CurrencyOption
import com.smallcodelab.neontipcalculator.data.db.TipHistoryEntity
import com.smallcodelab.neontipcalculator.model.SplitAction
import com.smallcodelab.neontipcalculator.model.TipOption
import com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.components.Appear
import com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.components.CurrencySelector
import com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.components.CustomTipSlider
import com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.components.GlowBackground
import com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.components.HistoryBottomSheet
import com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.components.ResultCard
import com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.components.RoundBtn
import com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.components.RoundUpToggle
import com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.components.TipChip
import com.smallcodelab.neontipcalculator.ui.theme.BorderMuted
import com.smallcodelab.neontipcalculator.ui.theme.MutedText
import com.smallcodelab.neontipcalculator.ui.theme.NeonGreen
import com.smallcodelab.neontipcalculator.ui.theme.NeonTipCalculatorTheme
import com.smallcodelab.neontipcalculator.ui.theme.PanelBg
import com.smallcodelab.neontipcalculator.ui.theme.WhiteText

/**
 * Main Tip Calculator screen layout composable integrating all features.
 */
@Composable
fun TipCalculatorScreen(
    uiState: TipCalculatorUiState,
    onBillInputChanged: (String) -> Unit,
    onTipSelected: (TipOption) -> Unit,
    onCustomTipPercentageChanged: (Int) -> Unit,
    onSplitAction: (SplitAction) -> Unit,
    onRoundUpToggled: (Boolean) -> Unit,
    onCurrencySelected: (CurrencyOption) -> Unit,
    onSaveToHistory: () -> Unit,
    onDeleteHistoryItem: (TipHistoryEntity) -> Unit,
    onClearAllHistory: () -> Unit,
    onToggleHistorySheet: (Boolean) -> Unit,
    onToastShown: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            onToastShown()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        GlowBackground()

        Column(
            modifier = Modifier
                .systemBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Appear(delayMs = 0) {
                Column {
                    Text(
                        text = stringResource(id = R.string.header_subtitle),
                        color = NeonGreen,
                        fontSize = 12.sp,
                        letterSpacing = 3.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(id = R.string.header_title),
                        color = WhiteText,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Appear(delayMs = 100) {
                CurrencySelector(
                    selectedCurrency = uiState.selectedCurrency,
                    onCurrencySelected = { currency ->
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onCurrencySelected(currency)
                    }
                )
            }

            Appear(delayMs = 200) {
                ResultCard(
                    amountPerPerson = uiState.amountPerPerson,
                    totalAmount = uiState.totalAmount,
                    tipOption = uiState.selectedTip,
                    effectiveTipPercentage = uiState.effectiveTipPercentage,
                    currency = uiState.selectedCurrency,
                    onSaveToHistory = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSaveToHistory()
                    },
                    onShareResult = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        shareSummaryText(context, uiState)
                    },
                    onOpenHistory = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onToggleHistorySheet(true)
                    }
                )
            }

            Appear(delayMs = 300) {
                val billInputDescription = stringResource(id = R.string.cd_bill_input)
                OutlinedTextField(
                    value = uiState.billInput,
                    onValueChange = onBillInputChanged,
                    label = { Text(text = stringResource(id = R.string.label_bill_amount)) },
                    placeholder = { Text(text = stringResource(id = R.string.bill_amount_placeholder)) },
                    prefix = { Text(text = "${uiState.selectedCurrency.symbol} ") },
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {
                            contentDescription = billInputDescription
                        },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = WhiteText,
                        unfocusedTextColor = WhiteText,
                        focusedBorderColor = NeonGreen,
                        unfocusedBorderColor = BorderMuted,
                        focusedLabelColor = NeonGreen,
                        unfocusedLabelColor = MutedText,
                        focusedPrefixColor = NeonGreen,
                        unfocusedPrefixColor = MutedText,
                        cursorColor = NeonGreen
                    )
                )
            }

            Appear(delayMs = 400) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = stringResource(id = R.string.label_select_tip),
                        color = MutedText,
                        fontSize = 14.sp
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TipOption.entries.forEach { option ->
                            TipChip(
                                tipOption = option,
                                modifier = Modifier.weight(1f),
                                selected = uiState.selectedTip == option,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onTipSelected(option)
                                }
                            )
                        }
                    }

                    AnimatedVisibility(visible = uiState.selectedTip.isCustom) {
                        CustomTipSlider(
                            customPercentage = uiState.customTipPercentage,
                            onPercentageChanged = onCustomTipPercentageChanged
                        )
                    }
                }
            }

            Appear(delayMs = 500) {
                RoundUpToggle(
                    roundUpEnabled = uiState.roundUpPerPerson,
                    onRoundUpToggled = { enabled ->
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onRoundUpToggled(enabled)
                    }
                )
            }

            Appear(delayMs = 600) {
                val decrementDescription = stringResource(id = R.string.cd_decrement_people)
                val incrementDescription = stringResource(id = R.string.cd_increment_people)
                val peopleCountDescription = stringResource(id = R.string.cd_people_count, uiState.peopleCount)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = PanelBg, shape = RoundedCornerShape(20.dp))
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.label_split_between),
                        color = MutedText,
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f)
                    )
                    RoundBtn(
                        symbol = stringResource(id = R.string.btn_decrement),
                        contentDescriptionText = decrementDescription,
                        enabled = uiState.peopleCount > 1,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onSplitAction(SplitAction.DECREMENT)
                        }
                    )
                    AnimatedContent(
                        targetState = uiState.peopleCount,
                        transitionSpec = {
                            if (targetState > initialState) {
                                (slideInVertically { -it } + fadeIn()) togetherWith (slideOutVertically { it } + fadeOut())
                            } else {
                                (slideInVertically { it } + fadeIn()) togetherWith (slideOutVertically { -it } + fadeOut())
                            }
                        },
                        label = "peopleCountTransition",
                        modifier = Modifier.width(56.dp)
                    ) { count ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics {
                                    contentDescription = peopleCountDescription
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = count.toString(),
                                color = WhiteText,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    RoundBtn(
                        symbol = stringResource(id = R.string.btn_increment),
                        contentDescriptionText = incrementDescription,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onSplitAction(SplitAction.INCREMENT)
                        }
                    )
                }
            }
        }

        if (uiState.isHistorySheetOpen) {
            HistoryBottomSheet(
                historyList = uiState.historyList,
                onDismiss = { onToggleHistorySheet(false) },
                onDeleteItem = onDeleteHistoryItem,
                onClearAll = onClearAllHistory
            )
        }
    }
}

private fun shareSummaryText(context: Context, uiState: TipCalculatorUiState) {
    val sym = uiState.selectedCurrency.symbol
    val billStr = "$sym${uiState.billInput.ifBlank { "0.00" }}"
    val totalStr = "$sym${uiState.totalAmount}"
    val perPersonStr = "$sym${uiState.amountPerPerson}"

    val title = context.getString(R.string.share_summary_title)
    val bill = context.getString(R.string.share_bill, billStr)
    val tip = context.getString(R.string.share_tip, uiState.effectiveTipPercentage)
    val people = context.getString(R.string.share_people, uiState.peopleCount)
    val total = context.getString(R.string.share_total, totalStr)
    val perPerson = context.getString(R.string.share_per_person, perPersonStr)
    val footer = context.getString(R.string.share_footer)

    val shareText = "$title\n$bill\n$tip\n$people\n$total\n$perPerson\n\n$footer"

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, shareText)
    }
    context.startActivity(Intent.createChooser(intent, title))
}

@Preview(showBackground = true)
@Composable
fun TipCalculatorScreenPreview() {
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
