package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DiskiColorScheme = darkColorScheme(
    primary = DiskiGold,
    onPrimary = SurfaceDark,
    primaryContainer = PitchGreen,
    onPrimaryContainer = DiskiGoldLight,
    secondary = PitchGreenLight,
    onSecondary = TextPrimary,
    background = SurfaceDark,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceCardHighlight,
    onSurfaceVariant = TextSecondary
)

@Composable
fun DiskiFootballTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DiskiColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    DiskiFootballTheme(content = content)
}
