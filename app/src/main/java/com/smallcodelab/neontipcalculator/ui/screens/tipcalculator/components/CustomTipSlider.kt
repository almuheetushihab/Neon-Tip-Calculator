package com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallcodelab.neontipcalculator.R
import com.smallcodelab.neontipcalculator.ui.theme.CyanBlue
import com.smallcodelab.neontipcalculator.ui.theme.MutedText
import com.smallcodelab.neontipcalculator.ui.theme.NeonGreen
import com.smallcodelab.neontipcalculator.ui.theme.NeonTipCalculatorTheme
import com.smallcodelab.neontipcalculator.ui.theme.PanelBg
import com.smallcodelab.neontipcalculator.ui.theme.WhiteText

/**
 * Custom tip slider allowing fine-grained selection from 0% to 50%.
 *
 * @param customPercentage Current slider tip percentage.
 * @param onPercentageChanged Callback invoked when slider is adjusted.
 * @param modifier Composable modifier.
 */
@Composable
fun CustomTipSlider(
    customPercentage: Int,
    onPercentageChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = PanelBg, shape = RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.label_custom_tip, customPercentage),
                color = WhiteText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "0% - 50%",
                color = MutedText,
                fontSize = 12.sp
            )
        }
        Slider(
            value = customPercentage.toFloat(),
            onValueChange = { onPercentageChanged(it.toInt()) },
            valueRange = 0f..50f,
            steps = 49,
            colors = SliderDefaults.colors(
                thumbColor = NeonGreen,
                activeTrackColor = CyanBlue,
                inactiveTrackColor = MutedText.copy(alpha = 0.3f)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomTipSliderPreview() {
    NeonTipCalculatorTheme {
        CustomTipSlider(
            customPercentage = 18,
            onPercentageChanged = {}
        )
    }
}
