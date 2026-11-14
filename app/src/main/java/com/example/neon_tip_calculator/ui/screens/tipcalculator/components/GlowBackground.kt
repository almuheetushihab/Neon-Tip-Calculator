package com.example.neon_tip_calculator.ui.screens.tipcalculator.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.neon_tip_calculator.R
import com.example.neon_tip_calculator.ui.theme.CyanBlue
import com.example.neon_tip_calculator.ui.theme.DarkBg
import com.example.neon_tip_calculator.ui.theme.NeonGreen
import com.example.neon_tip_calculator.ui.theme.NeonTipCalculatorTheme
import com.example.neon_tip_calculator.ui.theme.TransparentColor

@Composable
fun GlowBackground(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "glowTransition")
    val pulse by transition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowPulse"
    )

    val backgroundDescription = stringResource(id = R.string.cd_background_glow)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg)
            .semantics {
                contentDescription = backgroundDescription
            }
    ) {
        Box(
            modifier = Modifier
                .size(340.dp)
                .offset(x = (-90).dp, y = (-70).dp)
                .scale(pulse)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(NeonGreen.copy(alpha = 0.28f), TransparentColor)
                    ),
                    shape = CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(320.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 90.dp, y = 70.dp)
                .scale(pulse)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(CyanBlue.copy(alpha = 0.22f), TransparentColor)
                    ),
                    shape = CircleShape
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GlowBackgroundPreview() {
    NeonTipCalculatorTheme {
        GlowBackground()
    }
}
