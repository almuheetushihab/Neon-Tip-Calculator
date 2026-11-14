package com.smallcodelab.neontipcalculator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NeonDarkColorScheme = darkColorScheme(
    primary = NeonGreen,
    secondary = CyanBlue,
    tertiary = CyanBlue,
    background = DarkBg,
    surface = PanelBg,
    onPrimary = DarkBg,
    onSecondary = DarkBg,
    onBackground = WhiteText,
    onSurface = WhiteText,
    outline = MutedText
)

/**
 * Neon Tip Calculator application theme wrapping Material3 Dark Color Scheme.
 *
 * @param darkTheme Indicates whether dark theme is enabled (defaults to system setting).
 * @param content Composable content tree.
 */
@Composable
fun NeonTipCalculatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NeonDarkColorScheme,
        typography = Typography,
        content = content
    )
}
