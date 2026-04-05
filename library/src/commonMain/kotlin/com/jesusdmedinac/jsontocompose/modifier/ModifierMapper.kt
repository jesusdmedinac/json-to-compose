package com.jesusdmedinac.jsontocompose.modifier

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import com.jesusdmedinac.jsontocompose.model.ComposeShape
import com.jesusdmedinac.jsontocompose.renderer.toColor

/**
 * Enumerates all supported modifier operations.
 *
 * Each value corresponds to a [ComposeModifier.Operation] subclass and maps
 * to a specific Compose `Modifier` function during rendering.
 */
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
    Rotate,

    // --- Phase 3: Missing Modifiers ---
    Clickable,
    Weight,
    VerticalScroll,
    HorizontalScroll,
    Offset,
    Size,
    WrapContentWidth,
    WrapContentHeight,
    AspectRatio,
    ZIndex,
    MinWidth,
    MinHeight,
    MaxWidth,
    MaxHeight,
    AnimateContentSize,
    TestTag;
}

/**
 * Applies all [ComposeModifier] operations to this [Modifier] in order.
 *
 * Each [ComposeModifier.Operation] is mapped to its corresponding Compose modifier function.
 *
 * @param composeModifier The modifier descriptor containing the operations to apply.
 * @return A new [Modifier] with all operations applied.
 */
@Composable
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
            is ComposeModifier.Operation.BackgroundColor -> result.background(operation.hexColor.toColor())
            is ComposeModifier.Operation.Border -> result.border(
                width = operation.width.dp,
                color = operation.color.toColor(),
                shape = operation.shape.toShape()
            )

            is ComposeModifier.Operation.Shadow -> result.shadow(
                elevation = operation.elevation.dp,
                shape = operation.shape.toShape(),
                clip = operation.clip
            )

            is ComposeModifier.Operation.Clip -> result.clip(operation.shape.toShape())
            is ComposeModifier.Operation.Background -> result.background(
                color = operation.color.toColor(),
                shape = operation.shape.toShape()
            )

            is ComposeModifier.Operation.Alpha -> result.alpha(operation.value)
            is ComposeModifier.Operation.Rotate -> result.rotate(operation.degrees)

            // --- Phase 3: Missing Modifiers (stubs) ---
            is ComposeModifier.Operation.Clickable -> result.clickable { /* resolved via LocalBehavior at render time */ }
            is ComposeModifier.Operation.Weight -> result // Weight requires RowScope/ColumnScope — applied contextually by renderers
            is ComposeModifier.Operation.VerticalScroll -> result.verticalScroll(rememberScrollState())
            is ComposeModifier.Operation.HorizontalScroll -> result.horizontalScroll(rememberScrollState())
            is ComposeModifier.Operation.Offset -> result.offset(x = operation.x.dp, y = operation.y.dp)
            is ComposeModifier.Operation.Size -> result.size(operation.width.dp, operation.height.dp)
            is ComposeModifier.Operation.WrapContentWidth -> result.wrapContentWidth()
            is ComposeModifier.Operation.WrapContentHeight -> result.wrapContentHeight()
            is ComposeModifier.Operation.AspectRatio -> result.aspectRatio(operation.ratio)
            is ComposeModifier.Operation.ZIndex -> result.zIndex(operation.zIndex)
            is ComposeModifier.Operation.MinWidth -> result.defaultMinSize(minWidth = operation.value.dp)
            is ComposeModifier.Operation.MinHeight -> result.defaultMinSize(minHeight = operation.value.dp)
            is ComposeModifier.Operation.MaxWidth -> result.widthIn(max = operation.value.dp)
            is ComposeModifier.Operation.MaxHeight -> result.heightIn(max = operation.value.dp)
            is ComposeModifier.Operation.AnimateContentSize -> result.animateContentSize()
            is ComposeModifier.Operation.TestTag -> result.testTag(operation.tag)
        }
    }
    return result
}

/**
 * Converts a [ComposeShape] descriptor to a Compose [Shape].
 */
fun ComposeShape.toShape(): Shape = when (this) {
    ComposeShape.Rectangle -> RectangleShape
    ComposeShape.Circle -> CircleShape
    is ComposeShape.RoundedCorner ->
        all
            ?.let { RoundedCornerShape(it.dp) }
            ?: RoundedCornerShape(
                topStart = (topStart ?: 0).dp,
                topEnd = (topEnd ?: 0).dp,
                bottomEnd = (bottomEnd ?: 0).dp,
                bottomStart = (bottomStart ?: 0).dp
            )
}
