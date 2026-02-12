package com.jesusdmedinac.compose.sdui

import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties

/**
 * Design palette for the demo showcase app.
 * All screens should reference these constants for visual consistency.
 */
object DemoPalette {
    const val primary = "#FF1565C0"
    const val primaryDark = "#FF0D47A1"
    const val secondary = "#FF42A5F5"
    const val accent = "#FFFF5722"
    const val surface = "#FFF5F5F5"
    const val divider = "#FFE0E0E0"
    const val background = "#FFFFFFFF"

    val primaryArgb: Int = 0xFF1565C0.toInt()
    val primaryDarkArgb: Int = 0xFF0D47A1.toInt()
}

fun sectionHeader(title: String): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(
            ComposeModifier.Operation.FillMaxWidth,
            ComposeModifier.Operation.BackgroundColor(DemoPalette.primary),
            ComposeModifier.Operation.Padding(12),
        )
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(text = title),
            )
        )
    )
)

fun sectionDivider(): ComposeNode = ComposeNode(
    type = ComposeType.Box,
    composeModifier = ComposeModifier(
        operations = listOf(
            ComposeModifier.Operation.FillMaxWidth,
            ComposeModifier.Operation.Height(2),
            ComposeModifier.Operation.BackgroundColor(DemoPalette.divider),
        )
    ),
    properties = NodeProperties.BoxProps(children = emptyList()),
)

fun demoLabel(label: String): ComposeNode = ComposeNode(
    type = ComposeType.Text,
    composeModifier = ComposeModifier(
        operations = listOf(
            ComposeModifier.Operation.Padding(8),
        )
    ),
    properties = NodeProperties.TextProps(text = label),
)
