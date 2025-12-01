package com.jesusdmedinac.jsontocompose.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.jsontocompose.model.ComposeModifier

enum class ModifierOperation {
    Padding,
    FillMaxSize,
    FillMaxWidth,
    FillMaxHeight,
    Width,
    Height,
    BackgroundColor;
}

infix fun Modifier.from(composeModifier: ComposeModifier): Modifier {
    var result = this
    composeModifier.operations.forEach { operation ->
        result = when (operation) {
            is ComposeModifier.Operation.Padding -> result.padding(operation.value.dp)
            is ComposeModifier.Operation.FillMaxSize -> result.fillMaxSize()
            is ComposeModifier.Operation.FillMaxWidth -> result.fillMaxWidth()
            is ComposeModifier.Operation.FillMaxHeight -> result.fillMaxHeight()
            is ComposeModifier.Operation.Width -> result.width(operation.value.dp)
            is ComposeModifier.Operation.Height -> result.height(operation.value.dp)
            is ComposeModifier.Operation.BackgroundColor -> result.background(operation.hexColor.toColorInt())
        }
    }
    return result
}

fun String.toColorInt(): Color {
    if (!startsWith("#") || length != 9) {
        return Color.White
    }

    val colorLong = removePrefix("#").toLong(16)
    val alpha = (colorLong shr 24 and 0xFF).toInt()
    val red = (colorLong shr 16 and 0xFF).toInt()
    val green = (colorLong shr 8 and 0xFF).toInt()
    val blue = (colorLong and 0xFF).toInt()

    return Color(red, green, blue, alpha)
}
