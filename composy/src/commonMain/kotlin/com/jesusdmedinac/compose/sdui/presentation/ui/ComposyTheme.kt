package com.jesusdmedinac.compose.sdui.presentation.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightThemeColors = lightColors(
    primary = Color(0xFF6200EE), // Purple 500
    primaryVariant = Color(0xFF3700B3), // Purple 700
    secondary = Color(0xFF03DAC5), // Teal 200
    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

private val DarkThemeColors = darkColors(
    primary = Color(0xFFBB86FC), // Purple 200
    primaryVariant = Color(0xFF3700B3), // Purple 700
    secondary = Color(0xFF03DAC5) // Teal 200
    /* Other default colors to override
    background = Color.Black,
    surface = Color.Black,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    */
)

@Composable
fun ComposyTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) {
        DarkThemeColors
    } else {
        LightThemeColors
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
}