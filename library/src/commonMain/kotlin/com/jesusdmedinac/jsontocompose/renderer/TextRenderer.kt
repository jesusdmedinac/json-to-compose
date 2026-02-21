package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

@Composable
fun ComposeNode.ToText() {
    val props = properties as? NodeProperties.TextProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)

    val (text, _) = resolveStateHostValue(
        stateHostName = props.textStateHostName,
        inlineValue = props.text,
        defaultValue = "",
    )
    val (fontSize, _) = resolveStateHostValue(
        stateHostName = props.fontSizeStateHostName,
        inlineValue = props.fontSize,
        defaultValue = -1.0,
    )
    val (fontWeight, _) = resolveStateHostValue(
        stateHostName = props.fontWeightStateHostName,
        inlineValue = props.fontWeight,
        defaultValue = null,
    )
    val (fontStyle, _) = resolveStateHostValue(
        stateHostName = props.fontStyleStateHostName,
        inlineValue = props.fontStyle,
        defaultValue = null,
    )
    val (color, _) = resolveStateHostValue(
        stateHostName = props.colorStateHostName,
        inlineValue = props.color,
        defaultValue = null,
    )
    val (textAlign, _) = resolveStateHostValue(
        stateHostName = props.textAlignStateHostName,
        inlineValue = props.textAlign,
        defaultValue = null,
    )
    val (maxLines, _) = resolveStateHostValue(
        stateHostName = props.maxLinesStateHostName,
        inlineValue = props.maxLines,
        defaultValue = Int.MAX_VALUE,
    )
    val (overflow, _) = resolveStateHostValue(
        stateHostName = props.overflowStateHostName,
        inlineValue = props.overflow,
        defaultValue = null,
    )
    val (letterSpacing, _) = resolveStateHostValue(
        stateHostName = props.letterSpacingStateHostName,
        inlineValue = props.letterSpacing,
        defaultValue = -1.0,
    )
    val (lineHeight, _) = resolveStateHostValue(
        stateHostName = props.lineHeightStateHostName,
        inlineValue = props.lineHeight,
        defaultValue = -1.0,
    )
    val (textDecoration, _) = resolveStateHostValue(
        stateHostName = props.textDecorationStateHostName,
        inlineValue = props.textDecoration,
        defaultValue = null,
    )
    val (minLines, _) = resolveStateHostValue(
        stateHostName = props.minLinesStateHostName,
        inlineValue = props.minLines,
        defaultValue = 1,
    )

    Text(
        text = text,
        modifier = modifier,
        fontSize = if (fontSize >= 0) fontSize.sp else TextUnit.Unspecified,
        fontWeight = fontWeight.toFontWeight(),
        fontStyle = fontStyle.toFontStyle(),
        color = color?.let { Color(it) } ?: Color.Unspecified,
        textAlign = textAlign.toTextAlign(),
        maxLines = maxLines,
        overflow = overflow.toTextOverflow(),
        letterSpacing = if (letterSpacing >= 0) letterSpacing.sp else TextUnit.Unspecified,
        lineHeight = if (lineHeight >= 0) lineHeight.sp else TextUnit.Unspecified,
        textDecoration = textDecoration.toTextDecoration(),
        minLines = minLines,
    )
}
