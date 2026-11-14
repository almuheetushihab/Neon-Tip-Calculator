package com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallcodelab.neontipcalculator.R
import com.smallcodelab.neontipcalculator.data.currency.CurrencyOption
import com.smallcodelab.neontipcalculator.ui.theme.DarkBg
import com.smallcodelab.neontipcalculator.ui.theme.NeonGreen
import com.smallcodelab.neontipcalculator.ui.theme.NeonTipCalculatorTheme
import com.smallcodelab.neontipcalculator.ui.theme.PanelBg
import com.smallcodelab.neontipcalculator.ui.theme.WhiteText

/**
 * Currency selector component displaying options for USD, EUR, GBP, BDT.
 *
 * @param selectedCurrency Currently selected currency option.
 * @param onCurrencySelected Callback invoked when a currency option is selected.
 * @param modifier Composable modifier.
 */
@Composable
fun CurrencySelector(
    selectedCurrency: CurrencyOption,
    onCurrencySelected: (CurrencyOption) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectorDescription = stringResource(id = R.string.cd_currency_selector)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = selectorDescription
            },
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CurrencyOption.entries.forEach { option ->
            val selected = option == selectedCurrency
            val backgroundColor by animateColorAsState(
                targetValue = if (selected) NeonGreen else PanelBg,
                label = "currencyChipBg"
            )
            val textColor by animateColorAsState(
                targetValue = if (selected) DarkBg else WhiteText,
                label = "currencyChipTextColor"
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(color = backgroundColor, shape = RoundedCornerShape(12.dp))
                    .semantics {
                        role = Role.RadioButton
                        this.selected = selected
                    }
                    .clickable { onCurrencySelected(option) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${option.symbol} ${option.code}",
                    color = textColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencySelectorPreview() {
    NeonTipCalculatorTheme {
        CurrencySelector(
            selectedCurrency = CurrencyOption.USD,
            onCurrencySelected = {}
        )
    }
}
