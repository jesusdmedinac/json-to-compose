package com.jesusdmedinac.compose.sdui.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.materialkolor.PaletteStyle
import com.materialkolor.rememberDynamicColorScheme

@Composable
fun ComposyTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme =
        rememberDynamicColorScheme(
            seedColor = Color(0xFF607D8B),
            isAmoled = false,
            isDark = useDarkTheme,
            style = PaletteStyle.Fidelity
        )
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}