package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.ui.graphics.Color

/**
 * Parses a hex color string into a Compose [Color].
 * Supported formats:
 * - `#RRGGBB` (Alpha defaults to 0xFF)
 * - `#AARRGGBB`
 *
 * Returns [default] if the format is invalid.
 */
fun String?.toColor(default: Color = Color.Unspecified): Color = runCatching {
    if (this == null || !startsWith("#")) return@runCatching default
    val hex = removePrefix("#")
    when (hex.length) {
        6 -> {
            val colorLong = hex.toLong(16)
            Color(
                red = (colorLong shr 16 and 0xFF).toInt(),
                green = (colorLong shr 8 and 0xFF).toInt(),
                blue = (colorLong and 0xFF).toInt(),
                alpha = 0xFF
            )
        }

        8 -> {
            val colorLong = hex.toLong(16)
            val alpha = (colorLong shr 24 and 0xFF).toInt()
            val red = (colorLong shr 16 and 0xFF).toInt()
            val green = (colorLong shr 8 and 0xFF).toInt()
            val blue = (colorLong and 0xFF).toInt()
            Color(red, green, blue, alpha)
        }

        else -> default
    }
}
    .getOrNull()
    ?: default
