package com.stickerpal.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFF6B6B),
    secondary = Color(0xFF4ECDC4),
    background = Color(0xFFFFF8F0),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onSecondary = Color(0xFF1A1A1A),
    onBackground = Color(0xFF1A1A1A),
    onSurface = Color(0xFF1A1A1A),
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF8A8A),
    secondary = Color(0xFF6DD5B8),
    background = Color(0xFF1A1A1A),
    surface = Color(0xFF2D2D2D),
    onPrimary = Color(0xFF1A1A1A),
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

@Composable
fun StickerPalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        content = content
    )
}
