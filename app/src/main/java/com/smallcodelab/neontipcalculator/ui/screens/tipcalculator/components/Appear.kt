package com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.smallcodelab.neontipcalculator.ui.theme.NeonTipCalculatorTheme
import kotlinx.coroutines.delay

/**
 * Animated visibility container providing slide-in and fade-in entrance effect.
 *
 * @param modifier Composable modifier.
 * @param delayMs Delay in milliseconds before entrance animation begins.
 * @param content Inner composable content to animate.
 */
@Composable
fun Appear(
    modifier: Modifier = Modifier,
    delayMs: Int = 0,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(delayMs) {
        delay(delayMs.toLong())
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMillis = 600)) +
                slideInVertically(animationSpec = tween(durationMillis = 600)) { fullHeight -> fullHeight / 3 },
        modifier = modifier
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun AppearPreview() {
    NeonTipCalculatorTheme {
        Appear(delayMs = 0) {
            Text(text = "Animated Item Preview")
        }
    }
}
