package com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallcodelab.neontipcalculator.ui.theme.DarkBg
import com.smallcodelab.neontipcalculator.ui.theme.NeonGreen
import com.smallcodelab.neontipcalculator.ui.theme.NeonTipCalculatorTheme

/**
 * Circular action button component used for incrementing and decrementing values.
 *
 * @param symbol Text or symbol displayed inside the button (e.g. "+" or "-").
 * @param contentDescriptionText Accessibility label describing button action.
 * @param onClick Action callback when clicked.
 * @param modifier Composable modifier.
 * @param enabled Whether button interactions are enabled.
 */
@Composable
fun RoundBtn(
    symbol: String,
    contentDescriptionText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .size(44.dp)
            .background(color = if (enabled) NeonGreen else NeonGreen.copy(alpha = 0.4f), shape = CircleShape)
            .semantics {
                role = Role.Button
                contentDescription = contentDescriptionText
            }
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = symbol,
            color = DarkBg,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RoundBtnPreview() {
    NeonTipCalculatorTheme {
        RoundBtn(
            symbol = "+",
            contentDescriptionText = "Increase count",
            onClick = {}
        )
    }
}
