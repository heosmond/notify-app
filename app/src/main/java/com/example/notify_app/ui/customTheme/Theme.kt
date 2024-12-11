package com.example.notify_app.ui.customTheme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = SpotifyGreen,
    onPrimary = Black,
    secondary = LightGray,
    onSecondary = White,
    background = DarkGray,
    onBackground = White,
    surface = DarkGray,
    onSurface = White
)

private val LightColorScheme = lightColorScheme(
    primary = SpotifyGreen,
    onPrimary = White,
    secondary = DarkGray,
    onSecondary = Black,
    background = White,
    onBackground = Black,
    surface = LightGray,
    onSurface = Black
)

@Composable
fun SpotifyTheme(
    darkTheme: Boolean = true, // Default to dark theme
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}

