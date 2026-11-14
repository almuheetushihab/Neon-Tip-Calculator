package com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallcodelab.neontipcalculator.R
import com.smallcodelab.neontipcalculator.model.TipOption
import com.smallcodelab.neontipcalculator.ui.theme.ArcBackground
import com.smallcodelab.neontipcalculator.ui.theme.CyanBlue
import com.smallcodelab.neontipcalculator.ui.theme.MutedText
import com.smallcodelab.neontipcalculator.ui.theme.NeonGreen
import com.smallcodelab.neontipcalculator.ui.theme.NeonTipCalculatorTheme
import com.smallcodelab.neontipcalculator.ui.theme.PanelBg
import com.smallcodelab.neontipcalculator.ui.theme.WhiteText
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Result summary card displaying calculated per-person amount, total bill, and tip percentage ring.
 *
 * @param amountPerPerson Amount calculated per person.
 * @param totalAmount Total calculated bill amount including tip.
 * @param tipOption Selected tip option.
 * @param modifier Composable layout modifier.
 */
@Composable
fun ResultCard(
    amountPerPerson: BigDecimal,
    totalAmount: BigDecimal,
    tipOption: TipOption,
    modifier: Modifier = Modifier
) {
    val targetRingProgress = tipOption.percentage / (100f + tipOption.percentage)
    val ringProgress by animateFloatAsState(
        targetValue = targetRingProgress,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "resultCardRingProgress"
    )

    val cardContentDescription = stringResource(id = R.string.cd_result_card)
    val ringContentDescription = stringResource(id = R.string.cd_tip_percentage_ring)

    val currencySymbol = stringResource(id = R.string.currency_symbol)
    val formattedPerPerson = "$currencySymbol${amountPerPerson.setScale(2, RoundingMode.HALF_UP)}"
    val formattedTotal = "$currencySymbol${totalAmount.setScale(2, RoundingMode.HALF_UP)}"

    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(colors = listOf(NeonGreen, CyanBlue)),
                shape = RoundedCornerShape(28.dp)
            )
            .background(
                color = PanelBg.copy(alpha = 0.85f),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(24.dp)
            .semantics {
                contentDescription = cardContentDescription
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(id = R.string.label_per_person),
                color = MutedText,
                fontSize = 12.sp,
                letterSpacing = 2.sp
            )
            Text(
                text = formattedPerPerson,
                color = NeonGreen,
                fontSize = 44.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = stringResource(id = R.string.label_total, formattedTotal),
                color = MutedText,
                fontSize = 14.sp
            )
        }
        Box(
            modifier = Modifier
                .size(88.dp)
                .semantics {
                    contentDescription = ringContentDescription
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidthPx = 10.dp.toPx()
                val arcTopLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2)
                val arcSize = Size(size.width - strokeWidthPx, size.height - strokeWidthPx)

                drawArc(
                    color = ArcBackground,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = arcTopLeft,
                    size = arcSize,
                    style = Stroke(strokeWidthPx)
                )
                drawArc(
                    brush = Brush.linearGradient(colors = listOf(NeonGreen, CyanBlue)),
                    startAngle = -90f,
                    sweepAngle = 360f * ringProgress,
                    useCenter = false,
                    topLeft = arcTopLeft,
                    size = arcSize,
                    style = Stroke(strokeWidthPx, cap = StrokeCap.Round)
                )
            }
            Text(
                text = tipOption.displayLabel,
                color = WhiteText,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResultCardPreview() {
    NeonTipCalculatorTheme {
        ResultCard(
            amountPerPerson = BigDecimal("69.00"),
            totalAmount = BigDecimal("138.00"),
            tipOption = TipOption.FIFTEEN
        )
    }
}
