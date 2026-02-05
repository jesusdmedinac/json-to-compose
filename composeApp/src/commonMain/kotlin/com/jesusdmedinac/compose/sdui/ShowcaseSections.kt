package com.jesusdmedinac.compose.sdui

import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeShape
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

// --- Section Builders ---

fun sectionHeader(title: String): ComposeNode {
    return ComposeNode(
        type = ComposeType.Text,
        properties = NodeProperties.TextProps(text = title),
        composeModifier = ComposeModifier(
            operations = listOf(
                ComposeModifier.Operation.FillMaxWidth,
                ComposeModifier.Operation.BackgroundColor("#EEEEEE"),
                ComposeModifier.Operation.Padding(16),
            )
        )
    )
}

fun layoutSection(): List<ComposeNode> {
    return listOf(
        sectionHeader("Layouts"),
        ComposeNode(
            type = ComposeType.Column,
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Padding(16),
                    ComposeModifier.Operation.FillMaxWidth
                )
            ),
            properties = NodeProperties.ColumnProps(
                verticalArrangement = "SpaceBetween",
                horizontalAlignment = "CenterHorizontally",
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Column Item 1"),
                        composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(4)))
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Column Item 2"),
                        composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(4)))
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Column Item 3"),
                        composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(4)))
                    )
                )
            )
        ),
        ComposeNode(
            type = ComposeType.Row,
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Padding(16),
                    ComposeModifier.Operation.FillMaxWidth
                )
            ),
            properties = NodeProperties.RowProps(
                horizontalArrangement = "SpaceEvenly",
                verticalAlignment = "CenterVertically",
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Row 1"),
                        composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(4)))
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Row 2"),
                        composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(4)))
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Row 3"),
                        composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(4)))
                    )
                )
            )
        ),
        ComposeNode(
            type = ComposeType.Box,
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Padding(16),
                    ComposeModifier.Operation.FillMaxWidth,
                    ComposeModifier.Operation.Height(100),
                    ComposeModifier.Operation.BackgroundColor("#E0E0E0")
                )
            ),
            properties = NodeProperties.BoxProps(
                contentAlignment = "Center",
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Box Bottom Layer"),
                        composeModifier = ComposeModifier(
                            operations = listOf(
                                ComposeModifier.Operation.Padding(16),
                                ComposeModifier.Operation.BackgroundColor("#B0B0B0")
                            )
                        )
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Box Top Layer"),
                        composeModifier = ComposeModifier(
                            operations = listOf(
                                ComposeModifier.Operation.Padding(8),
                                ComposeModifier.Operation.BackgroundColor("#FFFFFF")
                            )
                        )
                    )
                )
            )
        )
    )
}

fun contentSection(): List<ComposeNode> {
    return listOf(
        sectionHeader("Content"),
        ComposeNode(
            type = ComposeType.Column,
            composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(16))),
            properties = NodeProperties.ColumnProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "This is a styled Text component"),
                        composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(8)))
                    ),
                    ComposeNode(
                        type = ComposeType.Image,
                        properties = NodeProperties.ImageProps(
                            url = "https://relatos.jesusdmedinac.com/_astro/carta-al-lector.OLllKYCu_Z1cdMQV.webp",
                            contentDescription = "Image from URL",
                            contentScale = "Fit"
                        ),
                        composeModifier = ComposeModifier(
                            operations = listOf(
                                ComposeModifier.Operation.Height(150),
                                ComposeModifier.Operation.FillMaxWidth,
                                ComposeModifier.Operation.Padding(8)
                            )
                        )
                    ),
                    ComposeNode(
                        type = ComposeType.Image,
                        properties = NodeProperties.ImageProps(
                            resourceName = "compose-multiplatform",
                            contentDescription = "Image from Resource",
                            contentScale = "Fit"
                        ),
                        composeModifier = ComposeModifier(
                            operations = listOf(
                                ComposeModifier.Operation.Height(100),
                                ComposeModifier.Operation.FillMaxWidth,
                                ComposeModifier.Operation.Padding(8)
                            )
                        )
                    )
                )
            )
        )
    )
}

fun inputSection(): List<ComposeNode> {
    return listOf(
        sectionHeader("Input"),
        ComposeNode(
            type = ComposeType.Column,
            composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(16))),
            properties = NodeProperties.ColumnProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Button,
                        properties = NodeProperties.ButtonProps(
                            onClickEventName = "onButtonClick",
                            child = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Click Me")
                            )
                        ),
                        composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(8)))
                    ),
                    // TextFieldProps does not have a 'label' property.
                    // Adding a Text node above to simulate label.
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Enter text:"),
                        composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(4)))
                    ),
                    ComposeNode(
                        type = ComposeType.TextField,
                        properties = NodeProperties.TextFieldProps(
                            valueStateHostName = "text_field_value"
                        ),
                        composeModifier = ComposeModifier(
                            operations = listOf(
                                ComposeModifier.Operation.Padding(8),
                                ComposeModifier.Operation.FillMaxWidth
                            )
                        )
                    ),
                    ComposeNode(
                        type = ComposeType.Row,
                        properties = NodeProperties.RowProps(
                            verticalAlignment = "CenterVertically",
                            children = listOf(
                                ComposeNode(
                                    type = ComposeType.Switch,
                                    properties = NodeProperties.SwitchProps(
                                        checkedStateHostName = "switch_state",
                                        onCheckedChangeEventName = "onSwitchToggle",
                                        enabledStateHostName = "switch_enabled"
                                    )
                                ),
                                ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Toggle Switch"),
                                    composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(8)))
                                )
                            )
                        ),
                        composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(8)))
                    ),
                    ComposeNode(
                        type = ComposeType.Row,
                        properties = NodeProperties.RowProps(
                            verticalAlignment = "CenterVertically",
                            children = listOf(
                                ComposeNode(
                                    type = ComposeType.Checkbox,
                                    properties = NodeProperties.CheckboxProps(
                                        checkedStateHostName = "checkbox_state",
                                        onCheckedChangeEventName = "onCheckboxToggle",
                                        enabledStateHostName = "checkbox_enabled"
                                    )
                                ),
                                ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Check Box"),
                                    composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(8)))
                                )
                            )
                        ),
                        composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(8)))
                    )
                )
            )
        )
    )
}

fun containerSection(): List<ComposeNode> {
    return listOf(
        sectionHeader("Containers"),
        ComposeNode(
            type = ComposeType.Column,
            composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(16))),
            properties = NodeProperties.ColumnProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Card,
                        composeModifier = ComposeModifier(
                            operations = listOf(
                                ComposeModifier.Operation.FillMaxWidth,
                                ComposeModifier.Operation.Padding(8)
                            )
                        ),
                        properties = NodeProperties.CardProps(
                            elevation = 4,
                            cornerRadius = 8,
                            child = ComposeNode(
                                type = ComposeType.Column,
                                composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(16))),
                                properties = NodeProperties.ColumnProps(
                                    children = listOf(
                                        ComposeNode(
                                            type = ComposeType.Text,
                                            properties = NodeProperties.TextProps(text = "Card Title")
                                        ),
                                        ComposeNode(
                                            type = ComposeType.Text,
                                            properties = NodeProperties.TextProps(text = "Card Content with elevation and rounded corners.")
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    // Mini Scaffold
                    ComposeNode(
                        type = ComposeType.Scaffold,
                        composeModifier = ComposeModifier(
                            operations = listOf(
                                ComposeModifier.Operation.FillMaxWidth,
                                ComposeModifier.Operation.Height(150),
                                ComposeModifier.Operation.Padding(8),
                                ComposeModifier.Operation.Border(1, "#CCCCCC", ComposeShape.Rectangle)
                            )
                        ),
                        properties = NodeProperties.ScaffoldProps(
                            topBar = ComposeNode(
                                type = ComposeType.TopAppBar,
                                properties = NodeProperties.TopAppBarProps(
                                    title = ComposeNode(
                                        type = ComposeType.Text,
                                        properties = NodeProperties.TextProps(text = "Mini Scaffold")
                                    )
                                )
                            ),
                            child = ComposeNode(
                                type = ComposeType.Box,
                                properties = NodeProperties.BoxProps(
                                    contentAlignment = "Center",
                                    children = listOf(
                                        ComposeNode(
                                            type = ComposeType.Text,
                                            properties = NodeProperties.TextProps(text = "Scaffold Body")
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    ComposeNode(
                        type = ComposeType.Button,
                        properties = NodeProperties.ButtonProps(
                            onClickEventName = "onShowDialog",
                            child = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Show Alert Dialog")
                            )
                        ),
                        composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(8)))
                    ),
                    ComposeNode(
                        type = ComposeType.AlertDialog,
                        properties = NodeProperties.AlertDialogProps(
                            visibilityStateHostName = "dialog_visible",
                            title = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Alert")
                            ),
                            text = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "This is a demo dialog.")
                            ),
                            confirmButton = ComposeNode(
                                type = ComposeType.Button,
                                properties = NodeProperties.ButtonProps(
                                    onClickEventName = "onDialogConfirm",
                                    child = ComposeNode(
                                        type = ComposeType.Text,
                                        properties = NodeProperties.TextProps(text = "OK")
                                    )
                                )
                            ),
                            dismissButton = ComposeNode(
                                type = ComposeType.Button,
                                properties = NodeProperties.ButtonProps(
                                    onClickEventName = "onDialogDismiss",
                                    child = ComposeNode(
                                        type = ComposeType.Text,
                                        properties = NodeProperties.TextProps(text = "Cancel")
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    )
}

fun navigationSection(): List<ComposeNode> {
    return listOf(
        sectionHeader("Navigation"),
        ComposeNode(
            type = ComposeType.Column,
            composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(16))),
            properties = NodeProperties.ColumnProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.TopAppBar,
                        properties = NodeProperties.TopAppBarProps(
                            title = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Top App Bar Demo")
                            ),
                            navigationIcon = ComposeNode(
                                type = ComposeType.Button,
                                properties = NodeProperties.ButtonProps(
                                    onClickEventName = "onButtonClick",
                                    child = ComposeNode(
                                        type = ComposeType.Text,
                                        properties = NodeProperties.TextProps(text = "Menu")
                                    )
                                )
                            )
                        )
                    ),
                    ComposeNode(
                        type = ComposeType.Box,
                        composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Height(16))),
                        properties = NodeProperties.BoxProps()
                    ),
                    ComposeNode(
                        type = ComposeType.BottomBar,
                        properties = NodeProperties.BottomBarProps(
                            children = listOf(
                                ComposeNode(
                                    type = ComposeType.BottomNavigationItem,
                                    properties = NodeProperties.BottomNavigationItemProps(
                                        selected = true,
                                        label = ComposeNode(
                                            type = ComposeType.Text,
                                            properties = NodeProperties.TextProps(text = "Item 1")
                                        ),
                                        icon = ComposeNode(
                                            type = ComposeType.Text,
                                            properties = NodeProperties.TextProps(text = "1")
                                        )
                                    )
                                ),
                                ComposeNode(
                                    type = ComposeType.BottomNavigationItem,
                                    properties = NodeProperties.BottomNavigationItemProps(
                                        selected = false,
                                        label = ComposeNode(
                                            type = ComposeType.Text,
                                            properties = NodeProperties.TextProps(text = "Item 2")
                                        ),
                                        icon = ComposeNode(
                                            type = ComposeType.Text,
                                            properties = NodeProperties.TextProps(text = "2")
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    )
}

fun lazyListSection(): List<ComposeNode> {
    return listOf(
        sectionHeader("Lazy Lists"),
        ComposeNode(
            type = ComposeType.Column,
            composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(16))),
            properties = NodeProperties.ColumnProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Lazy Row:"),
                        // Fixed Padding usage: only value constructor available
                        composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(8)))
                    ),
                    ComposeNode(
                        type = ComposeType.LazyRow,
                        composeModifier = ComposeModifier(
                            operations = listOf(
                                ComposeModifier.Operation.FillMaxWidth,
                                ComposeModifier.Operation.Height(60) // Fixed height to prevent crash
                            )
                        ),
                        properties = NodeProperties.RowProps(
                            children = (1..10).map { i ->
                                ComposeNode(
                                    type = ComposeType.Box,
                                    composeModifier = ComposeModifier(
                                        operations = listOf(
                                            ComposeModifier.Operation.Width(100),
                                            ComposeModifier.Operation.Height(50),
                                            ComposeModifier.Operation.Padding(4),
                                            ComposeModifier.Operation.BackgroundColor("#DDDDDD"),
                                            ComposeModifier.Operation.Border(1, "#000000", ComposeShape.Rectangle)
                                        )
                                    ),
                                    properties = NodeProperties.BoxProps(
                                        contentAlignment = "Center",
                                        children = listOf(
                                            ComposeNode(
                                                type = ComposeType.Text,
                                                properties = NodeProperties.TextProps(text = "Item $i")
                                            )
                                        )
                                    )
                                )
                            }
                        )
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Lazy Column (Nested with Fixed Height):"),
                        // Fixed Padding usage
                        composeModifier = ComposeModifier(
                            operations = listOf(
                                ComposeModifier.Operation.Padding(16)
                            )
                        )
                    ),
                    ComposeNode(
                        type = ComposeType.LazyColumn,
                        composeModifier = ComposeModifier(
                            operations = listOf(
                                ComposeModifier.Operation.FillMaxWidth,
                                ComposeModifier.Operation.Height(150), // Fixed height to prevent crash
                                ComposeModifier.Operation.Border(1, "#999999", ComposeShape.Rectangle)
                            )
                        ),
                        properties = NodeProperties.ColumnProps(
                            children = (1..10).map { i ->
                                ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "List Item $i"),
                                    composeModifier = ComposeModifier(
                                        operations = listOf(
                                            ComposeModifier.Operation.Padding(8),
                                            ComposeModifier.Operation.FillMaxWidth
                                        )
                                    )
                                )
                            }
                        )
                    )
                )
            )
        )
    )
}

fun modifiersSection(): List<ComposeNode> {
    return listOf(
        sectionHeader("Modifiers"),
        ComposeNode(
            type = ComposeType.Column,
            composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(16))),
            properties = NodeProperties.ColumnProps(
                children = listOf(
                    modifierExample("Padding (16dp)",
                        ComposeModifier(operations = listOf(
                            ComposeModifier.Operation.BackgroundColor("#FFCCBC"),
                            ComposeModifier.Operation.Padding(16),
                            ComposeModifier.Operation.BackgroundColor("#FF5722")
                        ))
                    ),
                    modifierExample("Width (150dp) & Height (50dp)",
                        ComposeModifier(operations = listOf(
                            ComposeModifier.Operation.Width(150),
                            ComposeModifier.Operation.Height(50),
                            ComposeModifier.Operation.BackgroundColor("#8BC34A")
                        ))
                    ),
                    modifierExample("Border (2dp Blue)",
                        ComposeModifier(operations = listOf(
                            ComposeModifier.Operation.Padding(8),
                            ComposeModifier.Operation.Border(2, "#2196F3", ComposeShape.Rectangle),
                            ComposeModifier.Operation.Padding(8)
                        ))
                    ),
                    modifierExample("Rounded Corners (16dp)",
                        ComposeModifier(operations = listOf(
                            ComposeModifier.Operation.Width(100),
                            ComposeModifier.Operation.Height(50),
                            ComposeModifier.Operation.Clip(ComposeShape.RoundedCorner(all = 16)),
                            ComposeModifier.Operation.BackgroundColor("#9C27B0")
                        ))
                    ),
                    modifierExample("Circle Shape",
                        ComposeModifier(operations = listOf(
                            ComposeModifier.Operation.Width(60),
                            ComposeModifier.Operation.Height(60),
                            ComposeModifier.Operation.Clip(ComposeShape.Circle),
                            ComposeModifier.Operation.BackgroundColor("#E91E63")
                        ))
                    ),
                    modifierExample("Shadow (Elevation 8dp)",
                        ComposeModifier(operations = listOf(
                            ComposeModifier.Operation.Padding(8),
                            ComposeModifier.Operation.Shadow(8, ComposeShape.Rectangle),
                            ComposeModifier.Operation.BackgroundColor("#FFFFFF"),
                            ComposeModifier.Operation.Padding(16)
                        ))
                    ),
                    modifierExample("Alpha (0.5)",
                        ComposeModifier(operations = listOf(
                            ComposeModifier.Operation.Width(100),
                            ComposeModifier.Operation.Height(50),
                            ComposeModifier.Operation.BackgroundColor("#000000"),
                            ComposeModifier.Operation.Alpha(0.5f)
                        ))
                    ),
                    modifierExample("Rotate (45 deg)",
                        ComposeModifier(operations = listOf(
                            ComposeModifier.Operation.Width(50),
                            ComposeModifier.Operation.Height(50),
                            ComposeModifier.Operation.Rotate(45f),
                            ComposeModifier.Operation.BackgroundColor("#FFC107")
                        ))
                    ),
                    // FillMaxSize example wrapped in a fixed-size Box
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "FillMaxSize (inside 100x100 Box)"),
                        // Fixed Padding usage
                        composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(8)))
                    ),
                    ComposeNode(
                        type = ComposeType.Box,
                        composeModifier = ComposeModifier(operations = listOf(
                            ComposeModifier.Operation.Width(100),
                            ComposeModifier.Operation.Height(100),
                            ComposeModifier.Operation.Border(1, "#000000", ComposeShape.Rectangle)
                        )),
                        properties = NodeProperties.BoxProps(
                            children = listOf(
                                ComposeNode(
                                    type = ComposeType.Box,
                                    composeModifier = ComposeModifier(operations = listOf(
                                        ComposeModifier.Operation.FillMaxSize,
                                        ComposeModifier.Operation.BackgroundColor("#00BCD4")
                                    )),
                                    properties = NodeProperties.BoxProps()
                                )
                            )
                        )
                    )
                )
            )
        )
    )
}

fun modifierExample(label: String, modifier: ComposeModifier): ComposeNode {
    return ComposeNode(
        type = ComposeType.Column,
        // Fixed Padding usage
        composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(12))),
        properties = NodeProperties.ColumnProps(
            children = listOf(
                ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = label),
                    // Fixed Padding usage
                    composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(4)))
                ),
                ComposeNode(
                    type = ComposeType.Box,
                    composeModifier = modifier,
                    properties = NodeProperties.BoxProps(
                        children = listOf(
                             // Empty box content or minimal text to show the modifier effect
                        )
                    )
                )
            )
        )
    )
}

fun customSection(): List<ComposeNode> {
    return listOf(
        sectionHeader("Custom Components"),
        ComposeNode(
            type = ComposeType.Column,
            composeModifier = ComposeModifier(operations = listOf(ComposeModifier.Operation.Padding(16))),
            properties = NodeProperties.ColumnProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Custom,
                        properties = NodeProperties.CustomProps(
                            customType = "ProductCard",
                            customData = buildJsonObject {
                                put("title", JsonPrimitive("Super Widget"))
                                put("price", JsonPrimitive("19.99"))
                            }
                        )
                    )
                )
            )
        )
    )
}
