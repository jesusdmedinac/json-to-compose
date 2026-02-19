package com.jesusdmedinac.compose.sdui

import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeShape
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

// region Components Screen

fun componentsScreen(): ComposeNode = ComposeNode(
    type = ComposeType.LazyColumn,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.FillMaxSize)
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            sectionHeader("Layout Components"),
            layoutDemos(),
            sectionDivider(),
            sectionHeader("Content Components"),
            contentDemos(),
            sectionDivider(),
            sectionHeader("Interactive Input Components"),
            inputDemos(),
            sectionDivider(),
            sectionHeader("Container Components"),
            containerDemos(),
            sectionDivider(),
            sectionHeader("Lazy List Components"),
            lazyListDemos(),
            sectionDivider(),
            sectionHeader("Navigation Components"),
            navigationDemos(),
            sectionDivider(),
            sectionHeader("Custom Renderer Demo"),
            customRendererDemos(),
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

// region Layout Demos

private fun layoutDemos(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            columnDemo(),
            rowDemo(),
            boxDemo(),
        )
    )
)

private fun columnDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Column (SpaceEvenly)"),
            ComposeNode(
                type = ComposeType.Column,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.Height(120),
                        ComposeModifier.Operation.BackgroundColor(DemoPalette.surface),
                        ComposeModifier.Operation.Padding(8),
                    )
                ),
                properties = NodeProperties.ColumnProps(
                    verticalArrangement = "SpaceEvenly",
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "Item 1"),
                        ),
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "Item 2"),
                        ),
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "Item 3"),
                        ),
                    )
                )
            )
        )
    )
)

private fun rowDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Row (SpaceEvenly)"),
            ComposeNode(
                type = ComposeType.Row,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.BackgroundColor(DemoPalette.surface),
                        ComposeModifier.Operation.Padding(8),
                    )
                ),
                properties = NodeProperties.RowProps(
                    horizontalArrangement = "SpaceEvenly",
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "Left"),
                        ),
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "Center"),
                        ),
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "Right"),
                        ),
                    )
                )
            )
        )
    )
)

private fun boxDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Box (Center alignment)"),
            ComposeNode(
                type = ComposeType.Box,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.Height(80),
                        ComposeModifier.Operation.BackgroundColor(DemoPalette.surface),
                    )
                ),
                properties = NodeProperties.BoxProps(
                    contentAlignment = "Center",
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "Behind"),
                        ),
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "In Front (Centered)"),
                        ),
                    )
                )
            )
        )
    )
)

// endregion

// region Content Demos

private fun contentDemos(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            textDemo(),
            imageUrlDemo(),
            imageResourceDemo(),
        )
    )
)

private fun textDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Text Styles"),
            ComposeNode(
                type = ComposeType.Text,
                composeModifier = ComposeModifier(
                    operations = listOf(ComposeModifier.Operation.Padding(8))
                ),
                properties = NodeProperties.TextProps(text = "Standard Text Node"),
            ),
        )
    )
)

private fun imageUrlDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Image from URL"),
            ComposeNode(
                type = ComposeType.Image,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.Width(200),
                        ComposeModifier.Operation.Height(150),
                    )
                ),
                properties = NodeProperties.ImageProps(
                    url = "https://relatos.jesusdmedinac.com/_astro/carta-al-lector.OLllKYCu_Z1cdMQV.webp",
                    contentDescription = "Image loaded from URL",
                    contentScale = "Fit"
                ),
            ),
        )
    )
)

private fun imageResourceDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Image from Resource"),
            ComposeNode(
                type = ComposeType.Image,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.Width(100),
                        ComposeModifier.Operation.Height(100),
                    )
                ),
                properties = NodeProperties.ImageProps(
                    resourceName = "compose-multiplatform",
                    contentDescription = "Image loaded from drawable resource",
                    contentScale = "Fit"
                ),
            ),
        )
    )
)

// endregion

// region Input Demos

private fun inputDemos(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            buttonDemo(),
            textFieldDemo(),
            switchDemo(),
            checkboxDemo(),
        )
    )
)

private fun buttonDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Button"),
            ComposeNode(
                type = ComposeType.Button,
                properties = NodeProperties.ButtonProps(
                    onClickEventName = "button_clicked",
                    child = ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Click Me"),
                    )
                ),
            ),
        )
    )
)

private fun textFieldDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("TextField"),
            ComposeNode(
                type = ComposeType.TextField,
                composeModifier = ComposeModifier(
                    operations = listOf(ComposeModifier.Operation.FillMaxWidth)
                ),
                properties = NodeProperties.TextFieldProps(
                    valueStateHostName = "text_field_value"
                ),
            ),
        )
    )
)

private fun switchDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Switch"),
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

private fun checkboxDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Checkbox"),
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
                            properties = NodeProperties.TextProps(text = "Accept terms"),
                        ),
                    )
                )
            ),
        )
    )
)

// endregion

// region Container Demos

private fun containerDemos(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            cardDemo(),
            scaffoldDemo(),
            alertDialogDemo(),
        )
    )
)

private fun cardDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Card"),
            ComposeNode(
                type = ComposeType.Card,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.Padding(8),
                    )
                ),
                properties = NodeProperties.CardProps(
                    elevation = 4,
                    cornerRadius = 12,
                    child = ComposeNode(
                        type = ComposeType.Column,
                        properties = NodeProperties.ColumnProps(
                            children = listOf(
                                ComposeNode(
                                    type = ComposeType.Text,
                                    composeModifier = ComposeModifier(
                                        operations = listOf(ComposeModifier.Operation.Padding(16))
                                    ),
                                    properties = NodeProperties.TextProps(text = "Card Title"),
                                ),
                                ComposeNode(
                                    type = ComposeType.Text,
                                    composeModifier = ComposeModifier(
                                        operations = listOf(ComposeModifier.Operation.Padding(16))
                                    ),
                                    properties = NodeProperties.TextProps(
                                        text = "Card body with 4dp elevation and 12dp rounded corners"
                                    ),
                                ),
                            )
                        )
                    )
                )
            ),
        )
    )
)

private fun scaffoldDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Scaffold (with Top/Bottom bars)"),
            ComposeNode(
                type = ComposeType.Scaffold,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.Height(200),
                        ComposeModifier.Operation.Border(1, DemoPalette.divider, ComposeShape.RoundedCorner(all = 4)),
                    )
                ),
                properties = NodeProperties.ScaffoldProps(
                    topBar = ComposeNode(
                        type = ComposeType.TopAppBar,
                        properties = NodeProperties.TopAppBarProps(
                            title = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Mini App"),
                            )
                        )
                    ),
                    bottomBar = ComposeNode(
                        type = ComposeType.BottomBar,
                        properties = NodeProperties.BottomBarProps(
                            children = listOf(
                                ComposeNode(
                                    type = ComposeType.BottomNavigationItem,
                                    properties = NodeProperties.BottomNavigationItemProps(
                                        selected = true,
                                        label = ComposeNode(
                                            type = ComposeType.Text,
                                            properties = NodeProperties.TextProps(text = "Home"),
                                        ),
                                        icon = ComposeNode(
                                            type = ComposeType.Text,
                                            properties = NodeProperties.TextProps(text = "H"),
                                        ),
                                    )
                                ),
                                ComposeNode(
                                    type = ComposeType.BottomNavigationItem,
                                    properties = NodeProperties.BottomNavigationItemProps(
                                        selected = false,
                                        label = ComposeNode(
                                            type = ComposeType.Text,
                                            properties = NodeProperties.TextProps(text = "Profile"),
                                        ),
                                        icon = ComposeNode(
                                            type = ComposeType.Text,
                                            properties = NodeProperties.TextProps(text = "P"),
                                        ),
                                    )
                                ),
                            )
                        )
                    ),
                    child = ComposeNode(
                        type = ComposeType.Box,
                        composeModifier = ComposeModifier(
                            operations = listOf(ComposeModifier.Operation.FillMaxSize)
                        ),
                        properties = NodeProperties.BoxProps(
                            contentAlignment = "Center",
                            children = listOf(
                                ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Inner Content"),
                                )
                            )
                        ),
                    )
                )
            ),
        )
    )
)

private fun alertDialogDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("AlertDialog"),
            ComposeNode(
                type = ComposeType.Button,
                properties = NodeProperties.ButtonProps(
                    onClickEventName = "show_dialog",
                    child = ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Show Dialog"),
                    )
                ),
            ),
            ComposeNode(
                type = ComposeType.AlertDialog,
                properties = NodeProperties.AlertDialogProps(
                    title = ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Confirm Action"),
                    ),
                    text = ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(
                            text = "Do you want to proceed with this action?"
                        ),
                    ),
                    confirmButton = ComposeNode(
                        type = ComposeType.Button,
                        properties = NodeProperties.ButtonProps(
                            onClickEventName = "dialog_confirm",
                            child = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Confirm"),
                            )
                        )
                    ),
                    dismissButton = ComposeNode(
                        type = ComposeType.Button,
                        properties = NodeProperties.ButtonProps(
                            onClickEventName = "dialog_dismiss",
                            child = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Cancel"),
                            )
                        )
                    ),
                    visibilityStateHostName = "dialog_visibility",
                )
            ),
        )
    )
)

// endregion

// region Lazy List Demos

private fun lazyListDemos(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            lazyColumnDemo(),
            lazyRowDemo(),
        )
    )
)

private fun lazyColumnDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("LazyColumn"),
            ComposeNode(
                type = ComposeType.LazyColumn,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.Height(100),
                        ComposeModifier.Operation.BackgroundColor(DemoPalette.surface),
                    )
                ),
                properties = NodeProperties.ColumnProps(
                    children = (1..5).map { i ->
                        ComposeNode(
                            type = ComposeType.Text,
                            composeModifier = ComposeModifier(
                                operations = listOf(ComposeModifier.Operation.Padding(4))
                            ),
                            properties = NodeProperties.TextProps(text = "Item $i"),
                        )
                    }
                )
            ),
        )
    )
)

private fun lazyRowDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("LazyRow"),
            ComposeNode(
                type = ComposeType.LazyRow,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                    )
                ),
                properties = NodeProperties.RowProps(
                    children = (1..5).map { i ->
                        ComposeNode(
                            type = ComposeType.Text,
                            composeModifier = ComposeModifier(
                                operations = listOf(
                                    ComposeModifier.Operation.Padding(8),
                                    ComposeModifier.Operation.BackgroundColor("#FFE3F2FD"),
                                )
                            ),
                            properties = NodeProperties.TextProps(text = "Item $i"),
                        )
                    }
                )
            ),
        )
    )
)

// endregion

// region Navigation Demos

private fun navigationDemos(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            topAppBarDemo(),
            bottomBarDemo(),
        )
    )
)

private fun topAppBarDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("TopAppBar"),
            ComposeNode(
                type = ComposeType.TopAppBar,
                properties = NodeProperties.TopAppBarProps(
                    title = ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "My App"),
                    ),
                    backgroundColor = DemoPalette.primaryArgb,
                )
            ),
        )
    )
)

private fun bottomBarDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("BottomBar"),
            ComposeNode(
                type = ComposeType.BottomBar,
                properties = NodeProperties.BottomBarProps(
                    backgroundColor = DemoPalette.primaryArgb,
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.BottomNavigationItem,
                            properties = NodeProperties.BottomNavigationItemProps(
                                selected = true,
                                label = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Home"),
                                ),
                                icon = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "H"),
                                ),
                            )
                        ),
                        ComposeNode(
                            type = ComposeType.BottomNavigationItem,
                            properties = NodeProperties.BottomNavigationItemProps(
                                selected = false,
                                label = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Settings"),
                                ),
                                icon = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "S"),
                                ),
                            )
                        ),
                    )
                )
            ),
        )
    )
)

// endregion

// region Custom Renderer Demos

private fun customRendererDemos(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Custom Renderer (ProductCard)"),
            ComposeNode(
                type = ComposeType.Custom,
                properties = NodeProperties.CustomProps(
                    customType = "ProductCard",
                    customData = buildJsonObject {
                        put("title", JsonPrimitive("Custom Product"))
                        put("price", JsonPrimitive("99.99"))
                    }
                )
            ),
        )
    )
)

// endregion
