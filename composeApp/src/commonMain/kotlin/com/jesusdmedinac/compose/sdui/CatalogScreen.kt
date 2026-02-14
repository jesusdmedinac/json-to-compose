package com.jesusdmedinac.compose.sdui

import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeShape
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties

// region Catalog Screen

fun catalogScreen(): ComposeNode = ComposeNode(
    type = ComposeType.LazyColumn,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.FillMaxSize)
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            catalogHero(),
            sectionDivider(),
            sectionHeader("Component Categories"),
            catalogCategoryCards(),
            sectionDivider(),
            sectionHeader("Featured Components"),
            featuredButton(),
            featuredSwitch(),
            featuredCheckbox(),
            sectionDivider(),
            sectionHeader("Modifier Highlights"),
            modifierHighlights(),
            ComposeNode(
                type = ComposeType.Spacer,
                composeModifier = ComposeModifier(
                    operations = listOf(ComposeModifier.Operation.Height(56))
                ),
            )
        )
    )
)

// endregion

// region Hero

private fun catalogHero(): ComposeNode = ComposeNode(
    type = ComposeType.Box,
    composeModifier = ComposeModifier(
        operations = listOf(
            ComposeModifier.Operation.FillMaxWidth,
            ComposeModifier.Operation.Height(180),
            ComposeModifier.Operation.BackgroundColor(DemoPalette.primaryDark),
        )
    ),
    properties = NodeProperties.BoxProps(
        contentAlignment = "Center",
        children = listOf(
            ComposeNode(
                type = ComposeType.Column,
                properties = NodeProperties.ColumnProps(
                    horizontalAlignment = "CenterHorizontally",
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "JSON to Compose"),
                        ),
                        ComposeNode(
                            type = ComposeType.Text,
                            composeModifier = ComposeModifier(
                                operations = listOf(ComposeModifier.Operation.Padding(8))
                            ),
                            properties = NodeProperties.TextProps(
                                text = "Server-Driven UI for Compose Multiplatform"
                            ),
                        ),
                        ComposeNode(
                            type = ComposeType.Text,
                            composeModifier = ComposeModifier(
                                operations = listOf(ComposeModifier.Operation.Padding(4))
                            ),
                            properties = NodeProperties.TextProps(
                                text = "18 Components | 14 Modifiers | All Platforms"
                            ),
                        ),
                    )
                )
            )
        )
    )
)

// endregion

// region Category Cards

private fun catalogCategoryCards(): ComposeNode = ComposeNode(
    type = ComposeType.LazyRow,
    composeModifier = ComposeModifier(
        operations = listOf(
            ComposeModifier.Operation.FillMaxWidth,
            ComposeModifier.Operation.Padding(8),
        )
    ),
    properties = NodeProperties.RowProps(
        children = listOf(
            categoryCard("Layout", "Column, Row, Box", 3, DemoPalette.primary),
            categoryCard("Content", "Text, Image", 2, DemoPalette.secondary),
            categoryCard("Input", "Button, TextField, Switch, Checkbox", 4, DemoPalette.accent),
            categoryCard("Containers", "Card, Scaffold, AlertDialog", 3, "#FF388E3C"),
        )
    )
)

private fun categoryCard(
    name: String,
    components: String,
    count: Int,
    color: String,
): ComposeNode = ComposeNode(
    type = ComposeType.Card,
    composeModifier = ComposeModifier(
        operations = listOf(
            ComposeModifier.Operation.Width(160),
            ComposeModifier.Operation.Padding(4),
        )
    ),
    properties = NodeProperties.CardProps(
        elevation = 4,
        cornerRadius = 12,
        child = ComposeNode(
            type = ComposeType.Column,
            composeModifier = ComposeModifier(
                operations = listOf(ComposeModifier.Operation.FillMaxWidth)
            ),
            properties = NodeProperties.ColumnProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Box,
                        composeModifier = ComposeModifier(
                            operations = listOf(
                                ComposeModifier.Operation.FillMaxWidth,
                                ComposeModifier.Operation.Height(8),
                                ComposeModifier.Operation.BackgroundColor(color),
                            )
                        ),
                        properties = NodeProperties.BoxProps(children = emptyList()),
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        composeModifier = ComposeModifier(
                            operations = listOf(
                                ComposeModifier.Operation.Padding(8),
                            )
                        ),
                        properties = NodeProperties.TextProps(text = name),
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        composeModifier = ComposeModifier(
                            operations = listOf(
                                ComposeModifier.Operation.Padding(8),
                            )
                        ),
                        properties = NodeProperties.TextProps(text = components),
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        composeModifier = ComposeModifier(
                            operations = listOf(
                                ComposeModifier.Operation.Padding(8),
                            )
                        ),
                        properties = NodeProperties.TextProps(text = "$count components"),
                    ),
                )
            )
        )
    )
)

// endregion

// region Featured Components

private fun featuredButton(): ComposeNode = ComposeNode(
    type = ComposeType.Card,
    composeModifier = ComposeModifier(
        operations = listOf(
            ComposeModifier.Operation.FillMaxWidth,
            ComposeModifier.Operation.Padding(8),
        )
    ),
    properties = NodeProperties.CardProps(
        elevation = 2,
        cornerRadius = 8,
        child = ComposeNode(
            type = ComposeType.Column,
            composeModifier = ComposeModifier(
                operations = listOf(ComposeModifier.Operation.Padding(12))
            ),
            properties = NodeProperties.ColumnProps(
                children = listOf(
                    demoLabel("Button"),
                    ComposeNode(
                        type = ComposeType.Text,
                        composeModifier = ComposeModifier(
                            operations = listOf(ComposeModifier.Operation.Padding(4))
                        ),
                        properties = NodeProperties.TextProps(
                            text = "Clickable button with event-driven behavior"
                        ),
                    ),
                    ComposeNode(
                        type = ComposeType.Button,
                        properties = NodeProperties.ButtonProps(
                            onClickEventName = "button_clicked",
                            child = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Try Me"),
                            )
                        ),
                    ),
                )
            )
        )
    )
)

private fun featuredSwitch(): ComposeNode = ComposeNode(
    type = ComposeType.Card,
    composeModifier = ComposeModifier(
        operations = listOf(
            ComposeModifier.Operation.FillMaxWidth,
            ComposeModifier.Operation.Padding(8),
        )
    ),
    properties = NodeProperties.CardProps(
        elevation = 2,
        cornerRadius = 8,
        child = ComposeNode(
            type = ComposeType.Column,
            composeModifier = ComposeModifier(
                operations = listOf(ComposeModifier.Operation.Padding(12))
            ),
            properties = NodeProperties.ColumnProps(
                children = listOf(
                    demoLabel("Switch"),
                    ComposeNode(
                        type = ComposeType.Text,
                        composeModifier = ComposeModifier(
                            operations = listOf(ComposeModifier.Operation.Padding(4))
                        ),
                        properties = NodeProperties.TextProps(
                            text = "Toggle with StateHost-backed state"
                        ),
                    ),
                    ComposeNode(
                        type = ComposeType.Switch,
                        properties = NodeProperties.SwitchProps(
                            checkedStateHostName = "switch_state",
                            onCheckedChangeEventName = "switch_toggled",
                            enabledStateHostName = "switch_enabled",
                        ),
                    ),
                )
            )
        )
    )
)

private fun featuredCheckbox(): ComposeNode = ComposeNode(
    type = ComposeType.Card,
    composeModifier = ComposeModifier(
        operations = listOf(
            ComposeModifier.Operation.FillMaxWidth,
            ComposeModifier.Operation.Padding(8),
        )
    ),
    properties = NodeProperties.CardProps(
        elevation = 2,
        cornerRadius = 8,
        child = ComposeNode(
            type = ComposeType.Column,
            composeModifier = ComposeModifier(
                operations = listOf(ComposeModifier.Operation.Padding(12))
            ),
            properties = NodeProperties.ColumnProps(
                children = listOf(
                    demoLabel("Checkbox"),
                    ComposeNode(
                        type = ComposeType.Text,
                        composeModifier = ComposeModifier(
                            operations = listOf(ComposeModifier.Operation.Padding(4))
                        ),
                        properties = NodeProperties.TextProps(
                            text = "Interactive checkbox with event handling"
                        ),
                    ),
                    ComposeNode(
                        type = ComposeType.Row,
                        properties = NodeProperties.RowProps(
                            verticalAlignment = "CenterVertically",
                            children = listOf(
                                ComposeNode(
                                    type = ComposeType.Checkbox,
                                    properties = NodeProperties.CheckboxProps(
                                        checkedStateHostName = "checkbox_checked",
                                        onCheckedChangeEventName = "checkbox_toggled",
                                        enabledStateHostName = "checkbox_enabled",
                                    )
                                ),
                                ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Check me"),
                                ),
                            )
                        )
                    ),
                )
            )
        )
    )
)

// endregion

// region Modifier Highlights

private fun modifierHighlights(): ComposeNode = ComposeNode(
    type = ComposeType.LazyRow,
    composeModifier = ComposeModifier(
        operations = listOf(
            ComposeModifier.Operation.FillMaxWidth,
            ComposeModifier.Operation.Padding(8),
        )
    ),
    properties = NodeProperties.RowProps(
        children = listOf(
            modifierHighlightShadow(),
            modifierHighlightClip(),
            modifierHighlightBorder(),
            modifierHighlightRotate(),
        )
    )
)

private fun modifierHighlightShadow(): ComposeNode = ComposeNode(
    type = ComposeType.Card,
    composeModifier = ComposeModifier(
        operations = listOf(
            ComposeModifier.Operation.Width(140),
            ComposeModifier.Operation.Padding(4),
        )
    ),
    properties = NodeProperties.CardProps(
        elevation = 2,
        cornerRadius = 8,
        child = ComposeNode(
            type = ComposeType.Column,
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Padding(12),
                )
            ),
            properties = NodeProperties.ColumnProps(
                horizontalAlignment = "CenterHorizontally",
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        composeModifier = ComposeModifier(
                            operations = listOf(ComposeModifier.Operation.Padding(4))
                        ),
                        properties = NodeProperties.TextProps(text = "Shadow"),
                    ),
                    ComposeNode(
                        type = ComposeType.Box,
                        composeModifier = ComposeModifier(
                            operations = listOf(
                                ComposeModifier.Operation.Width(80),
                                ComposeModifier.Operation.Height(80),
                                ComposeModifier.Operation.Shadow(
                                    elevation = 8,
                                    shape = ComposeShape.RoundedCorner(all = 12),
                                    clip = false,
                                ),
                                ComposeModifier.Operation.BackgroundColor(DemoPalette.background),
                            )
                        ),
                        properties = NodeProperties.BoxProps(
                            contentAlignment = "Center",
                            children = listOf(
                                ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "8dp"),
                                ),
                            )
                        ),
                    ),
                )
            )
        )
    )
)

private fun modifierHighlightClip(): ComposeNode = ComposeNode(
    type = ComposeType.Card,
    composeModifier = ComposeModifier(
        operations = listOf(
            ComposeModifier.Operation.Width(140),
            ComposeModifier.Operation.Padding(4),
        )
    ),
    properties = NodeProperties.CardProps(
        elevation = 2,
        cornerRadius = 8,
        child = ComposeNode(
            type = ComposeType.Column,
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Padding(12),
                )
            ),
            properties = NodeProperties.ColumnProps(
                horizontalAlignment = "CenterHorizontally",
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        composeModifier = ComposeModifier(
                            operations = listOf(ComposeModifier.Operation.Padding(4))
                        ),
                        properties = NodeProperties.TextProps(text = "Clip"),
                    ),
                    ComposeNode(
                        type = ComposeType.Box,
                        composeModifier = ComposeModifier(
                            operations = listOf(
                                ComposeModifier.Operation.Width(80),
                                ComposeModifier.Operation.Height(80),
                                ComposeModifier.Operation.Clip(shape = ComposeShape.Circle),
                                ComposeModifier.Operation.BackgroundColor(DemoPalette.secondary),
                            )
                        ),
                        properties = NodeProperties.BoxProps(
                            contentAlignment = "Center",
                            children = listOf(
                                ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Circle"),
                                ),
                            )
                        ),
                    ),
                )
            )
        )
    )
)

private fun modifierHighlightBorder(): ComposeNode = ComposeNode(
    type = ComposeType.Card,
    composeModifier = ComposeModifier(
        operations = listOf(
            ComposeModifier.Operation.Width(140),
            ComposeModifier.Operation.Padding(4),
        )
    ),
    properties = NodeProperties.CardProps(
        elevation = 2,
        cornerRadius = 8,
        child = ComposeNode(
            type = ComposeType.Column,
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Padding(12),
                )
            ),
            properties = NodeProperties.ColumnProps(
                horizontalAlignment = "CenterHorizontally",
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        composeModifier = ComposeModifier(
                            operations = listOf(ComposeModifier.Operation.Padding(4))
                        ),
                        properties = NodeProperties.TextProps(text = "Border"),
                    ),
                    ComposeNode(
                        type = ComposeType.Box,
                        composeModifier = ComposeModifier(
                            operations = listOf(
                                ComposeModifier.Operation.Width(80),
                                ComposeModifier.Operation.Height(80),
                                ComposeModifier.Operation.Border(
                                    width = 3,
                                    color = DemoPalette.accent,
                                    shape = ComposeShape.RoundedCorner(all = 12),
                                ),
                            )
                        ),
                        properties = NodeProperties.BoxProps(
                            contentAlignment = "Center",
                            children = listOf(
                                ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "3dp"),
                                ),
                            )
                        ),
                    ),
                )
            )
        )
    )
)

private fun modifierHighlightRotate(): ComposeNode = ComposeNode(
    type = ComposeType.Card,
    composeModifier = ComposeModifier(
        operations = listOf(
            ComposeModifier.Operation.Width(140),
            ComposeModifier.Operation.Padding(4),
        )
    ),
    properties = NodeProperties.CardProps(
        elevation = 2,
        cornerRadius = 8,
        child = ComposeNode(
            type = ComposeType.Column,
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Padding(12),
                )
            ),
            properties = NodeProperties.ColumnProps(
                horizontalAlignment = "CenterHorizontally",
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        composeModifier = ComposeModifier(
                            operations = listOf(ComposeModifier.Operation.Padding(4))
                        ),
                        properties = NodeProperties.TextProps(text = "Rotate"),
                    ),
                    ComposeNode(
                        type = ComposeType.Box,
                        composeModifier = ComposeModifier(
                            operations = listOf(
                                ComposeModifier.Operation.Width(80),
                                ComposeModifier.Operation.Height(80),
                            )
                        ),
                        properties = NodeProperties.BoxProps(
                            contentAlignment = "Center",
                            children = listOf(
                                ComposeNode(
                                    type = ComposeType.Text,
                                    composeModifier = ComposeModifier(
                                        operations = listOf(
                                            ComposeModifier.Operation.Rotate(15f),
                                            ComposeModifier.Operation.Padding(8),
                                            ComposeModifier.Operation.BackgroundColor("#FFFFAB91"),
                                        )
                                    ),
                                    properties = NodeProperties.TextProps(text = "15 deg"),
                                ),
                            )
                        ),
                    ),
                )
            )
        )
    )
)

// endregion
