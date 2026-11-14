package com.example.neon_tip_calculator.ui.screens.tipcalculator

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neon_tip_calculator.R
import com.example.neon_tip_calculator.model.SplitAction
import com.example.neon_tip_calculator.model.TipOption
import com.example.neon_tip_calculator.ui.screens.tipcalculator.components.Appear
import com.example.neon_tip_calculator.ui.screens.tipcalculator.components.GlowBackground
import com.example.neon_tip_calculator.ui.screens.tipcalculator.components.ResultCard
import com.example.neon_tip_calculator.ui.screens.tipcalculator.components.RoundBtn
import com.example.neon_tip_calculator.ui.screens.tipcalculator.components.TipChip
import com.example.neon_tip_calculator.ui.theme.BorderMuted
import com.example.neon_tip_calculator.ui.theme.MutedText
import com.example.neon_tip_calculator.ui.theme.NeonGreen
import com.example.neon_tip_calculator.ui.theme.NeonTipCalculatorTheme
import com.example.neon_tip_calculator.ui.theme.PanelBg
import com.example.neon_tip_calculator.ui.theme.WhiteText

@Composable
fun TipCalculatorScreen(
    uiState: TipCalculatorUiState,
    onBillInputChanged: (String) -> Unit,
    onTipSelected: (TipOption) -> Unit,
    onSplitAction: (SplitAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        GlowBackground()

        Column(
            modifier = Modifier
                .systemBarsPadding()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp),
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

            Appear(delayMs = 150) {
                ResultCard(
                    amountPerPerson = uiState.amountPerPerson,
                    totalAmount = uiState.totalAmount,
                    tipOption = uiState.selectedTip
                )
            }

            Appear(delayMs = 300) {
                val billInputDescription = stringResource(id = R.string.cd_bill_input)
                OutlinedTextField(
                    value = uiState.billInput,
                    onValueChange = onBillInputChanged,
                    label = { Text(text = stringResource(id = R.string.label_bill_amount)) },
                    placeholder = { Text(text = stringResource(id = R.string.bill_amount_placeholder)) },
                    prefix = { Text(text = stringResource(id = R.string.currency_prefix)) },
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

            Appear(delayMs = 450) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = stringResource(id = R.string.label_select_tip),
                        color = MutedText,
                        fontSize = 14.sp
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        TipOption.entries.forEach { option ->
                            TipChip(
                                tipOption = option,
                                modifier = Modifier.weight(1f),
                                selected = uiState.selectedTip == option,
                                onClick = { onTipSelected(option) }
                            )
                        }
                    }
                }
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
                        onClick = { onSplitAction(SplitAction.DECREMENT) }
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
                        onClick = { onSplitAction(SplitAction.INCREMENT) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TipCalculatorScreenPreview() {
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
