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
            spacerDemo(),
            dividerDemo(),
            flowLayoutDemos(),
            surfaceDemo(),
            spacedByDemo(),
        )
    )
)

private fun spacedByDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Arrangement.spacedBy"),
            ComposeNode(
                type = ComposeType.Column,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.BackgroundColor(DemoPalette.surface),
                        ComposeModifier.Operation.Padding(8)
                    )
                ),
                properties = NodeProperties.ColumnProps(
                    verticalArrangement = "SpacedBy:16",
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "Column with 16dp spacing")
                        ),
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "Item 2")
                        ),
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "Item 3")
                        )
                    )
                )
            ),
            ComposeNode(
                type = ComposeType.Spacer,
                properties = NodeProperties.SpacerProps,
                composeModifier = ComposeModifier(
                    operations = listOf(ComposeModifier.Operation.Height(8))
                )
            ),
            ComposeNode(
                type = ComposeType.Row,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.BackgroundColor(DemoPalette.surface),
                        ComposeModifier.Operation.Padding(8)
                    )
                ),
                properties = NodeProperties.RowProps(
                    horizontalArrangement = "SpacedBy:24",
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "Row")
                        ),
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "with")
                        ),
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "24dp")
                        ),
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "spacing")
                        )
                    )
                )
            )
        )
    )
)

private fun surfaceDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Surface"),
            ComposeNode(
                type = ComposeType.Surface,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.Padding(8)
                    )
                ),
                properties = NodeProperties.SurfaceProps(
                    tonalElevation = 4,
                    shadowElevation = 2,
                    shape = ComposeShape.RoundedCorner(all = 12),
                    child = ComposeNode(
                        type = ComposeType.Column,
                        composeModifier = ComposeModifier(
                            operations = listOf(ComposeModifier.Operation.Padding(16))
                        ),
                        properties = NodeProperties.ColumnProps(
                            children = listOf(
                                ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(
                                        text = "Surface with Tonal Elevation (4dp)",
                                        fontWeight = "Bold"
                                    )
                                ),
                                ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(
                                        text = "And RoundedCorner(12)"
                                    )
                                )
                            )
                        )
                    )
                )
            ),
            ComposeNode(
                type = ComposeType.Surface,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.Padding(8)
                    )
                ),
                properties = NodeProperties.SurfaceProps(
                    color = "#FFBBDEFB", // Light Blue
                    shape = ComposeShape.Circle,
                    child = ComposeNode(
                        type = ComposeType.Box,
                        composeModifier = ComposeModifier(
                            operations = listOf(ComposeModifier.Operation.Padding(16))
                        ),
                        properties = NodeProperties.BoxProps(
                            contentAlignment = "Center",
                            children = listOf(
                                ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Circular Surface")
                                )
                            )
                        )
                    )
                )
            )
        )
    )
)

private fun flowLayoutDemos(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Flow Layouts (FlowRow & FlowColumn)"),
            ComposeNode(
                type = ComposeType.Column,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.BackgroundColor(DemoPalette.surface),
                        ComposeModifier.Operation.Padding(8)
                    )
                ),
                properties = NodeProperties.ColumnProps(
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "FlowRow (Wrapping children)")
                        ),
                        ComposeNode(
                            type = ComposeType.FlowRow,
                            properties = NodeProperties.FlowRowProps(
                                horizontalArrangement = "Start",
                                verticalArrangement = "Top",
                                children = (1..10).map { i ->
                                    ComposeNode(
                                        type = ComposeType.Text,
                                        composeModifier = ComposeModifier(
                                            operations = listOf(
                                                ComposeModifier.Operation.Padding(4),
                                                ComposeModifier.Operation.BackgroundColor("#FFE0E0E0")
                                            )
                                        ),
                                        properties = NodeProperties.TextProps(text = "Item $i")
                                    )
                                }
                            )
                        ),
                        ComposeNode(
                            type = ComposeType.Spacer,
                            properties = NodeProperties.SpacerProps,
                            composeModifier = ComposeModifier(
                                operations = listOf(ComposeModifier.Operation.Height(16))
                            )
                        ),
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "FlowColumn (Fixed height: 100dp)")
                        ),
                        ComposeNode(
                            type = ComposeType.FlowColumn,
                            composeModifier = ComposeModifier(
                                operations = listOf(ComposeModifier.Operation.Height(100))
                            ),
                            properties = NodeProperties.FlowColumnProps(
                                children = (1..6).map { i ->
                                    ComposeNode(
                                        type = ComposeType.Text,
                                        composeModifier = ComposeModifier(
                                            operations = listOf(
                                                ComposeModifier.Operation.Padding(4),
                                                ComposeModifier.Operation.BackgroundColor("#FFB2DFDB")
                                            )
                                        ),
                                        properties = NodeProperties.TextProps(text = "Col $i")
                                    )
                                }
                            )
                        )
                    )
                )
            )
        )
    )
)

private fun dividerDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Dividers"),
            ComposeNode(
                type = ComposeType.Column,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.BackgroundColor(DemoPalette.surface),
                        ComposeModifier.Operation.Padding(8)
                    )
                ),
                properties = NodeProperties.ColumnProps(
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "Horizontal Divider (Default)")
                        ),
                        ComposeNode(
                            type = ComposeType.HorizontalDivider,
                            properties = NodeProperties.DividerProps()
                        ),
                        ComposeNode(
                            type = ComposeType.Text,
                            composeModifier = ComposeModifier(
                                operations = listOf(ComposeModifier.Operation.Padding(0))
                            ),
                            properties = NodeProperties.TextProps(text = "Horizontal Divider (Red, 4dp)")
                        ),
                        ComposeNode(
                            type = ComposeType.HorizontalDivider,
                            properties = NodeProperties.DividerProps(
                                thickness = 4,
                                color = "#FFFF0000"
                            )
                        ),
                        ComposeNode(
                            type = ComposeType.Row,
                            composeModifier = ComposeModifier(
                                operations = listOf(
                                    ComposeModifier.Operation.Height(40),
                                    ComposeModifier.Operation.Padding(8)
                                )
                            ),
                            properties = NodeProperties.RowProps(
                                verticalAlignment = "CenterVertically",
                                horizontalArrangement = "SpaceEvenly",
                                children = listOf(
                                    ComposeNode(
                                        type = ComposeType.Text,
                                        properties = NodeProperties.TextProps(text = "Left")
                                    ),
                                    ComposeNode(
                                        type = ComposeType.VerticalDivider,
                                        properties = NodeProperties.DividerProps(
                                            thickness = 2,
                                            color = "#FF0000FF"
                                        )
                                    ),
                                    ComposeNode(
                                        type = ComposeType.Text,
                                        properties = NodeProperties.TextProps(text = "Right")
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

private fun spacerDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Spacer (Height 16)"),
            ComposeNode(
                type = ComposeType.Column,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.BackgroundColor(DemoPalette.surface),
                        ComposeModifier.Operation.Padding(8)
                    )
                ),
                properties = NodeProperties.ColumnProps(
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "Top Text")
                        ),
                        ComposeNode(
                            type = ComposeType.Spacer,
                            properties = NodeProperties.SpacerProps,
                            composeModifier = ComposeModifier(
                                operations = listOf(ComposeModifier.Operation.Height(16))
                            )
                        ),
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "Bottom Text (16dp below)")
                        )
                    )
                )
            )
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
            demoLabel("Text Styles & Properties"),
            ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(
                    text = "Large Bold Title",
                    fontSize = 24.0,
                    fontWeight = "Bold",
                    color = "#FF1A237E"
                ),
            ),
            ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(
                    text = "Medium Italic Subtitle",
                    fontSize = 18.0,
                    fontStyle = "Italic",
                    color = "#FF3F51B5"
                ),
            ),
            ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(
                    text = "Center aligned text with custom line height and letter spacing to demonstrate typography control.",
                    textAlign = "Center",
                    lineHeight = 24.0,
                    letterSpacing = 2.0
                ),
            ),
            ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(
                    text = "Underlined text",
                    textDecoration = "Underline"
                ),
            ),
            ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(
                    text = "This is a very long text that should be truncated with ellipsis after two lines because of the maxLines and overflow properties applied to it.",
                    maxLines = 2,
                    overflow = "Ellipsis"
                ),
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
            demoLabel("Button Variants"),
            ComposeNode(
                type = ComposeType.Row,
                composeModifier = ComposeModifier(
                    operations = listOf(ComposeModifier.Operation.FillMaxWidth)
                ),
                properties = NodeProperties.RowProps(
                    horizontalArrangement = "SpaceEvenly",
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Button,
                            properties = NodeProperties.ButtonProps(
                                child = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Filled"),
                                )
                            ),
                        ),
                        ComposeNode(
                            type = ComposeType.ElevatedButton,
                            properties = NodeProperties.ButtonProps(
                                child = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Elevated"),
                                )
                            ),
                        ),
                    )
                )
            ),
            ComposeNode(
                type = ComposeType.Row,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.Padding(8)
                    )
                ),
                properties = NodeProperties.RowProps(
                    horizontalArrangement = "SpaceEvenly",
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.OutlinedButton,
                            properties = NodeProperties.ButtonProps(
                                child = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Outlined"),
                                )
                            ),
                        ),
                        ComposeNode(
                            type = ComposeType.FilledTonalButton,
                            properties = NodeProperties.ButtonProps(
                                child = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Tonal"),
                                )
                            ),
                        ),
                    )
                )
            ),
            ComposeNode(
                type = ComposeType.Row,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.Padding(8)
                    )
                ),
                properties = NodeProperties.RowProps(
                    verticalAlignment = "CenterVertically",
                    horizontalArrangement = "SpaceEvenly",
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.TextButton,
                            properties = NodeProperties.ButtonProps(
                                child = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Text Button"),
                                )
                            ),
                        ),
                        ComposeNode(
                            type = ComposeType.IconButton,
                            properties = NodeProperties.ButtonProps(
                                child = ComposeNode(
                                    type = ComposeType.Icon,
                                    properties = NodeProperties.IconProps(iconName = "Favorite"),
                                )
                            ),
                        ),
                    )
                )
            ),
            demoLabel("Floating Action Buttons"),
            ComposeNode(
                type = ComposeType.Row,
                composeModifier = ComposeModifier(
                    operations = listOf(ComposeModifier.Operation.FillMaxWidth)
                ),
                properties = NodeProperties.RowProps(
                    verticalAlignment = "CenterVertically",
                    horizontalArrangement = "SpaceEvenly",
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.FloatingActionButton,
                            properties = NodeProperties.FabProps(
                                icon = ComposeNode(
                                    type = ComposeType.Icon,
                                    properties = NodeProperties.IconProps(iconName = "Add"),
                                )
                            ),
                        ),
                        ComposeNode(
                            type = ComposeType.ExtendedFloatingActionButton,
                            properties = NodeProperties.ExtendedFabProps(
                                text = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Compose"),
                                ),
                                icon = ComposeNode(
                                    type = ComposeType.Icon,
                                    properties = NodeProperties.IconProps(iconName = "Edit"),
                                )
                            ),
                        ),
                    )
                )
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
            demoLabel("TextField (Basic)"),
            ComposeNode(
                type = ComposeType.TextField,
                composeModifier = ComposeModifier(
                    operations = listOf(ComposeModifier.Operation.FillMaxWidth)
                ),
                properties = NodeProperties.TextFieldProps(
                    valueStateHostName = "text_field_value",
                    label = ComposeNode(ComposeType.Text, NodeProperties.TextProps(text = "Enter text")),
                    placeholder = ComposeNode(ComposeType.Text, NodeProperties.TextProps(text = "Placeholder...")),
                ),
            ),
            demoLabel("TextField (Error & Supporting text)"),
            ComposeNode(
                type = ComposeType.TextField,
                composeModifier = ComposeModifier(
                    operations = listOf(ComposeModifier.Operation.FillMaxWidth)
                ),
                properties = NodeProperties.TextFieldProps(
                    valueStateHostName = "text_field_value",
                    isError = true,
                    supportingText = ComposeNode(ComposeType.Text, NodeProperties.TextProps(text = "Please enter a valid value")),
                ),
            ),
            demoLabel("OutlinedTextField (Icons & Keyboard)"),
            ComposeNode(
                type = ComposeType.OutlinedTextField,
                composeModifier = ComposeModifier(
                    operations = listOf(ComposeModifier.Operation.FillMaxWidth)
                ),
                properties = NodeProperties.TextFieldProps(
                    valueStateHostName = "text_field_value",
                    label = ComposeNode(ComposeType.Text, NodeProperties.TextProps(text = "Email address")),
                    leadingIcon = ComposeNode(ComposeType.Icon, NodeProperties.IconProps(iconName = "Email")),
                    trailingIcon = ComposeNode(ComposeType.Icon, NodeProperties.IconProps(iconName = "Clear")),
                    keyboardType = "Email",
                    singleLine = true,
                ),
            ),
            demoLabel("TextField (Password & Prefix/Suffix)"),
            ComposeNode(
                type = ComposeType.TextField,
                composeModifier = ComposeModifier(
                    operations = listOf(ComposeModifier.Operation.FillMaxWidth)
                ),
                properties = NodeProperties.TextFieldProps(
                    valueStateHostName = "text_field_value",
                    label = ComposeNode(ComposeType.Text, NodeProperties.TextProps(text = "Password")),
                    visualTransformation = "Password",
                    keyboardType = "Password",
                    prefix = ComposeNode(ComposeType.Text, NodeProperties.TextProps(text = "key: ")),
                    suffix = ComposeNode(ComposeType.Text, NodeProperties.TextProps(text = " *")),
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
            demoLabel("Elevated Card"),
            ComposeNode(
                type = ComposeType.ElevatedCard,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.Padding(8),
                    )
                ),
                properties = NodeProperties.CardProps(
                    elevation = 12,
                    cornerRadius = 24,
                    child = ComposeNode(
                        type = ComposeType.Column,
                        properties = NodeProperties.ColumnProps(
                            children = listOf(
                                ComposeNode(
                                    type = ComposeType.Text,
                                    composeModifier = ComposeModifier(
                                        operations = listOf(ComposeModifier.Operation.Padding(16))
                                    ),
                                    properties = NodeProperties.TextProps(
                                        text = "High elevation (12dp) with very rounded corners (24dp)"
                                    ),
                                ),
                            )
                        )
                    )
                )
            ),
            demoLabel("Outlined Card"),
            ComposeNode(
                type = ComposeType.OutlinedCard,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.Padding(8),
                    )
                ),
                properties = NodeProperties.OutlinedCardProps(
                    borderColor = DemoPalette.primary, // Purple border
                    cornerRadius = 8,
                    child = ComposeNode(
                        type = ComposeType.Column,
                        properties = NodeProperties.ColumnProps(
                            children = listOf(
                                ComposeNode(
                                    type = ComposeType.Text,
                                    composeModifier = ComposeModifier(
                                        operations = listOf(ComposeModifier.Operation.Padding(16))
                                    ),
                                    properties = NodeProperties.TextProps(
                                        text = "Border outlined in purple with 8dp rounded corners"
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
                        ComposeModifier.Operation.Border(
                            1,
                            DemoPalette.divider,
                            ComposeShape.RoundedCorner(all = 4)
                        ),
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
            navigationBarDemo(),
            navigationRailDemo(),
            tabRowDemo(),
        )
    )
)

private fun navigationBarDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Material 3 NavigationBar"),
            ComposeNode(
                type = ComposeType.NavigationBar,
                properties = NodeProperties.NavigationBarProps(
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.NavigationBarItem,
                            properties = NodeProperties.NavigationBarItemProps(
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
                            type = ComposeType.NavigationBarItem,
                            properties = NodeProperties.NavigationBarItemProps(
                                selected = false,
                                label = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Search"),
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

private fun navigationRailDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Material 3 NavigationRail"),
            ComposeNode(
                type = ComposeType.NavigationRail,
                properties = NodeProperties.NavigationRailProps(
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.NavigationRailItem,
                            properties = NodeProperties.NavigationRailItemProps(
                                selected = true,
                                label = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Menu 1"),
                                ),
                                icon = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "1"),
                                ),
                            )
                        ),
                        ComposeNode(
                            type = ComposeType.NavigationRailItem,
                            properties = NodeProperties.NavigationRailItemProps(
                                selected = false,
                                label = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Menu 2"),
                                ),
                                icon = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "2"),
                                ),
                            )
                        ),
                    )
                )
            ),
        )
    )
)

private fun tabRowDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Material 3 TabRow"),
            ComposeNode(
                type = ComposeType.TabRow,
                properties = NodeProperties.TabRowProps(
                    selectedTabIndex = 0,
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Tab,
                            properties = NodeProperties.TabProps(
                                selected = true,
                                text = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Tab 1"),
                                ),
                            )
                        ),
                        ComposeNode(
                            type = ComposeType.Tab,
                            properties = NodeProperties.TabProps(
                                selected = false,
                                text = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Tab 2"),
                                ),
                            )
                        ),
                    )
                )
            ),
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
                    backgroundColor = DemoPalette.primary,
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
                    backgroundColor = DemoPalette.primary,
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
