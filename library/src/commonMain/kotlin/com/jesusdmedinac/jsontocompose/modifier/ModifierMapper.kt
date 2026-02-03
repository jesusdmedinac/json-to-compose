package com.jesusdmedinac.jsontocompose.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import com.jesusdmedinac.jsontocompose.model.ComposeShape

enum class ModifierOperation {
    Padding,
    FillMaxSize,
    FillMaxWidth,
    FillMaxHeight,
    Width,
    Height,
    BackgroundColor,
    Border,
    Shadow,
    Clip,
    Background,
    Alpha,
    Rotate;
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
            is ComposeModifier.Operation.Border -> result.border(
                width = operation.width.dp,
                color = operation.color.toColorInt(),
                shape = operation.shape.toShape()
            )

            is ComposeModifier.Operation.Shadow -> result.shadow(
                elevation = operation.elevation.dp,
                shape = operation.shape.toShape(),
                clip = operation.clip
            )

            is ComposeModifier.Operation.Clip -> result.clip(operation.shape.toShape())
            is ComposeModifier.Operation.Background -> result.background(
                color = operation.color.toColorInt(),
                shape = operation.shape.toShape()
            )

            is ComposeModifier.Operation.Alpha -> result.alpha(operation.value)
            is ComposeModifier.Operation.Rotate -> result.rotate(operation.degrees)
        }
    }
    return result
}

fun ComposeShape.toShape(): Shape = when (this) {
    ComposeShape.Rectangle -> RectangleShape
    ComposeShape.Circle -> CircleShape
    is ComposeShape.RoundedCorner -> {
        if (all != null) {
            RoundedCornerShape(all.dp)
        } else {
            RoundedCornerShape(
                topStart = (topStart ?: 0).dp,
                topEnd = (topEnd ?: 0).dp,
                bottomEnd = (bottomEnd ?: 0).dp,
                bottomStart = (bottomStart ?: 0).dp
            )
        }
    }
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
