package com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallcodelab.neontipcalculator.R
import com.smallcodelab.neontipcalculator.ui.theme.DarkBg
import com.smallcodelab.neontipcalculator.ui.theme.MutedText
import com.smallcodelab.neontipcalculator.ui.theme.NeonGreen
import com.smallcodelab.neontipcalculator.ui.theme.NeonTipCalculatorTheme
import com.smallcodelab.neontipcalculator.ui.theme.PanelBg
import com.smallcodelab.neontipcalculator.ui.theme.WhiteText

/**
 * Toggle component for enabling/disabling "Round up per person" calculation.
 *
 * @param roundUpEnabled Current state of round-up toggle switch.
 * @param onRoundUpToggled Callback invoked when toggle is switched.
 * @param modifier Composable modifier.
 */
@Composable
fun RoundUpToggle(
    roundUpEnabled: Boolean,
    onRoundUpToggled: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val switchDescription = stringResource(id = R.string.cd_round_up_switch)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = PanelBg, shape = RoundedCornerShape(18.dp))
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.label_round_up),
            color = WhiteText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = roundUpEnabled,
            onCheckedChange = onRoundUpToggled,
            modifier = Modifier.semantics {
                contentDescription = switchDescription
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = DarkBg,
                checkedTrackColor = NeonGreen,
                uncheckedThumbColor = MutedText,
                uncheckedTrackColor = PanelBg
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RoundUpTogglePreview() {
    NeonTipCalculatorTheme {
        RoundUpToggle(
            roundUpEnabled = true,
            onRoundUpToggled = {}
        )
    }
}
