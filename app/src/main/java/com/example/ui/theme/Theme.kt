package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = BambooGreenPrimary,
    onPrimary = PureWhite,
    primaryContainer = BambooGreenSoft,
    onPrimaryContainer = BambooGreenDark,
    secondary = PastelPink,
    onSecondary = SoftTextDark,
    secondaryContainer = PastelPinkLight,
    onSecondaryContainer = PastelPinkDark,
    tertiary = PastelLilac,
    onTertiary = SoftTextDark,
    tertiaryContainer = SoftLilac,
    onTertiaryContainer = DeepLilac,
    background = PandaCream,
    onBackground = SoftTextDark,
    surface = PureWhite,
    onSurface = SoftTextDark,
    surfaceVariant = WarmBeige,
    onSurfaceVariant = SoftTextMuted
)

val AppShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false, // Keep consistent cheerful pastel theme for kids
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}
