package com.example.neon_tip_calculator.ui.screens.tipcalculator.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.neon_tip_calculator.R
import com.example.neon_tip_calculator.model.TipOption
import com.example.neon_tip_calculator.ui.theme.DarkBg
import com.example.neon_tip_calculator.ui.theme.NeonGreen
import com.example.neon_tip_calculator.ui.theme.NeonTipCalculatorTheme
import com.example.neon_tip_calculator.ui.theme.PanelBg
import com.example.neon_tip_calculator.ui.theme.WhiteText

@Composable
fun TipChip(
    tipOption: TipOption,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) NeonGreen else PanelBg,
        label = "tipChipBackgroundColor"
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) DarkBg else WhiteText,
        label = "tipChipTextColor"
    )
    val scaleFactor by animateFloatAsState(
        targetValue = if (selected) 1.08f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "tipChipScaleFactor"
    )

    val chipContentDescription = stringResource(id = R.string.cd_tip_chip, tipOption.percentage)

    Box(
        modifier = modifier
            .scale(scaleFactor)
            .background(color = backgroundColor, shape = RoundedCornerShape(16.dp))
            .semantics {
                role = Role.RadioButton
                this.selected = selected
                contentDescription = chipContentDescription
            }
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tipOption.displayLabel,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TipChipSelectedPreview() {
    NeonTipCalculatorTheme {
        TipChip(
            tipOption = TipOption.FIFTEEN,
            selected = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TipChipUnselectedPreview() {
    NeonTipCalculatorTheme {
        TipChip(
            tipOption = TipOption.FIFTEEN,
            selected = false
        )
    }
}
