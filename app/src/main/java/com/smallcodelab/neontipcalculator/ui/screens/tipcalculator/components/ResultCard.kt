package com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.smallcodelab.neontipcalculator.data.currency.CurrencyOption
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
 * Result summary card displaying calculated per-person amount, total bill, tip percentage ring,
 * and quick action icons for save, share, and history.
 *
 * @param amountPerPerson Amount calculated per person.
 * @param totalAmount Total calculated bill amount including tip.
 * @param tipOption Selected tip option.
 * @param effectiveTipPercentage Effective tip percentage integer.
 * @param currency Selected currency option.
 * @param onSaveToHistory Action callback to save current calculation to history.
 * @param onShareResult Action callback to share summary text.
 * @param onOpenHistory Action callback to open history sheet.
 * @param modifier Composable modifier.
 */
@Composable
fun ResultCard(
    amountPerPerson: BigDecimal,
    totalAmount: BigDecimal,
    tipOption: TipOption,
    effectiveTipPercentage: Int,
    currency: CurrencyOption,
    onSaveToHistory: () -> Unit,
    onShareResult: () -> Unit,
    onOpenHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val targetRingProgress = effectiveTipPercentage / (100f + effectiveTipPercentage)
    val ringProgress by animateFloatAsState(
        targetValue = targetRingProgress,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "resultCardRingProgress"
    )

    val cardContentDescription = stringResource(id = R.string.cd_result_card)
    val ringContentDescription = stringResource(id = R.string.cd_tip_percentage_ring)
    val saveDescription = stringResource(id = R.string.cd_save_button)
    val shareDescription = stringResource(id = R.string.cd_share_button)
    val historyDescription = stringResource(id = R.string.cd_history_button)

    val formattedPerPerson = "${currency.symbol}${amountPerPerson.setScale(2, RoundingMode.HALF_UP)}"
    val formattedTotal = "${currency.symbol}${totalAmount.setScale(2, RoundingMode.HALF_UP)}"

    Column(
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
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                    text = if (tipOption.isCustom) "$effectiveTipPercentage%" else tipOption.displayLabel,
                    color = WhiteText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onSaveToHistory,
                modifier = Modifier.semantics { contentDescription = saveDescription }
            ) {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = saveDescription,
                    tint = NeonGreen
                )
            }
            IconButton(
                onClick = onShareResult,
                modifier = Modifier.semantics { contentDescription = shareDescription }
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = shareDescription,
                    tint = CyanBlue
                )
            }
            IconButton(
                onClick = onOpenHistory,
                modifier = Modifier.semantics { contentDescription = historyDescription }
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = historyDescription,
                    tint = WhiteText
                )
            }
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
            tipOption = TipOption.FIFTEEN,
            effectiveTipPercentage = 15,
            currency = CurrencyOption.USD,
            onSaveToHistory = {},
            onShareResult = {},
            onOpenHistory = {}
        )
    }
}
