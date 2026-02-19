package com.jesusdmedinac.compose.sdui

import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeShape
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties

// region Styles Screen

fun stylesScreen(): ComposeNode = ComposeNode(
    type = ComposeType.LazyColumn,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.FillMaxSize)
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            sectionHeader("Sizing Modifiers"),
            sizingDemos(),
            sectionDivider(),
            sectionHeader("Color Palette"),
            colorPaletteDemos(),
            sectionDivider(),
            sectionHeader("Shapes and Clipping"),
            shapeDemos(),
            sectionDivider(),
            sectionHeader("Border vs Shadow"),
            borderShadowDemos(),
            sectionDivider(),
            sectionHeader("Alpha and Rotation"),
            alphaRotationDemos(),
            sectionDivider(),
            sectionHeader("Combined Modifiers"),
            combinedModifiersDemo(),
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

// region Sizing Demos

private fun sizingDemos(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            stylesPaddingDemo(),
            stylesFillMaxWidthDemo(),
            stylesWidthHeightDemo(),
            stylesFillMaxSizeDemo(),
        )
    )
)

private fun stylesPaddingDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Padding (24dp inner)"),
            ComposeNode(
                type = ComposeType.Box,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.BackgroundColor("#FFBBDEFB"),
                    )
                ),
                properties = NodeProperties.BoxProps(
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Text,
                            composeModifier = ComposeModifier(
                                operations = listOf(
                                    ComposeModifier.Operation.Padding(24),
                                )
                            ),
                            properties = NodeProperties.TextProps(
                                text = "Inside 24dp padding"
                            ),
                        ),
                    )
                )
            ),
        )
    )
)

private fun stylesFillMaxWidthDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("FillMaxWidth"),
            ComposeNode(
                type = ComposeType.Box,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.Height(40),
                        ComposeModifier.Operation.BackgroundColor("#FF90CAF9"),
                    )
                ),
                properties = NodeProperties.BoxProps(
                    contentAlignment = "Center",
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "100% Width"),
                        ),
                    )
                )
            ),
        )
    )
)

private fun stylesWidthHeightDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Width(120) + Height(60)"),
            ComposeNode(
                type = ComposeType.Box,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.Width(120),
                        ComposeModifier.Operation.Height(60),
                        ComposeModifier.Operation.BackgroundColor("#FF64B5F6"),
                    )
                ),
                properties = NodeProperties.BoxProps(
                    contentAlignment = "Center",
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "Fixed Size"),
                        ),
                    )
                )
            ),
        )
    )
)

private fun stylesFillMaxSizeDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("FillMaxSize (in 100dp container)"),
            ComposeNode(
                type = ComposeType.Box,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.Height(100),
                        ComposeModifier.Operation.BackgroundColor(DemoPalette.surface),
                    )
                ),
                properties = NodeProperties.BoxProps(
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Box,
                            composeModifier = ComposeModifier(
                                operations = listOf(
                                    ComposeModifier.Operation.FillMaxSize,
                                    ComposeModifier.Operation.Padding(16),
                                    ComposeModifier.Operation.BackgroundColor("#FF42A5F5"),
                                )
                            ),
                            properties = NodeProperties.BoxProps(
                                contentAlignment = "Center",
                                children = listOf(
                                    ComposeNode(
                                        type = ComposeType.Text,
                                        properties = NodeProperties.TextProps(text = "Filled Parent"),
                                    )
                                )
                            ),
                        )
                    )
                )
            ),
        )
    )
)

// endregion

// region Color Palette Demos

private fun colorPaletteDemos(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(16))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            colorBox("Primary", DemoPalette.primary),
            colorBox("Primary Dark", DemoPalette.primaryDark),
            colorBox("Secondary", DemoPalette.secondary),
            colorBox("Accent", DemoPalette.accent),
            colorBox("Surface", DemoPalette.surface),
        )
    )
)

private fun colorBox(name: String, hex: String): ComposeNode = ComposeNode(
    type = ComposeType.Row,
    composeModifier = ComposeModifier(
        operations = listOf(
            ComposeModifier.Operation.FillMaxWidth,
            ComposeModifier.Operation.Padding(4),
        )
    ),
    properties = NodeProperties.RowProps(
        verticalAlignment = "CenterVertically",
        children = listOf(
            ComposeNode(
                type = ComposeType.Box,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.Width(50),
                        ComposeModifier.Operation.Height(50),
                        ComposeModifier.Operation.BackgroundColor(hex),
                        ComposeModifier.Operation.Border(1, DemoPalette.divider, ComposeShape.RoundedCorner(all = 4)),
                    )
                ),
                properties = NodeProperties.BoxProps(children = emptyList()),
            ),
            ComposeNode(
                type = ComposeType.Column,
                composeModifier = ComposeModifier(
                    operations = listOf(ComposeModifier.Operation.Padding(12))
                ),
                properties = NodeProperties.ColumnProps(
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = name),
                        ),
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = hex),
                        ),
                    )
                )
            )
        )
    )
)

// endregion

// region Shape Demos

private fun shapeDemos(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Background + Shapes"),
            ComposeNode(
                type = ComposeType.Row,
                composeModifier = ComposeModifier(
                    operations = listOf(ComposeModifier.Operation.FillMaxWidth)
                ),
                properties = NodeProperties.RowProps(
                    horizontalArrangement = "SpaceEvenly",
                    children = listOf(
                        shapeBox("Circle", ComposeShape.Circle),
                        shapeBox("Rounded 16", ComposeShape.RoundedCorner(all = 16)),
                        shapeBox("Custom Corner", ComposeShape.RoundedCorner(topStart = 24, bottomEnd = 24)),
                    )
                )
            ),
            demoLabel("Clipping"),
            ComposeNode(
                type = ComposeType.Row,
                composeModifier = ComposeModifier(
                    operations = listOf(ComposeModifier.Operation.FillMaxWidth)
                ),
                properties = NodeProperties.RowProps(
                    horizontalArrangement = "SpaceEvenly",
                    children = listOf(
                        clipBox("Circle Clip", ComposeShape.Circle),
                        clipBox("Rounded Clip", ComposeShape.RoundedCorner(all = 12)),
                    )
                )
            ),
        )
    )
)

private fun shapeBox(label: String, shape: ComposeShape): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    properties = NodeProperties.ColumnProps(
        horizontalAlignment = "CenterHorizontally",
        children = listOf(
            ComposeNode(
                type = ComposeType.Box,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.Width(80),
                        ComposeModifier.Operation.Height(80),
                        ComposeModifier.Operation.Background(DemoPalette.secondary, shape),
                    )
                ),
                properties = NodeProperties.BoxProps(children = emptyList()),
            ),
            ComposeNode(
                type = ComposeType.Text,
                composeModifier = ComposeModifier(
                    operations = listOf(ComposeModifier.Operation.Padding(4))
                ),
                properties = NodeProperties.TextProps(text = label),
            ),
        )
    )
)

private fun clipBox(label: String, shape: ComposeShape): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    properties = NodeProperties.ColumnProps(
        horizontalAlignment = "CenterHorizontally",
        children = listOf(
            ComposeNode(
                type = ComposeType.Box,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.Width(80),
                        ComposeModifier.Operation.Height(80),
                        ComposeModifier.Operation.Clip(shape),
                        ComposeModifier.Operation.BackgroundColor(DemoPalette.accent),
                    )
                ),
                properties = NodeProperties.BoxProps(children = emptyList()),
            ),
            ComposeNode(
                type = ComposeType.Text,
                composeModifier = ComposeModifier(
                    operations = listOf(ComposeModifier.Operation.Padding(4))
                ),
                properties = NodeProperties.TextProps(text = label),
            ),
        )
    )
)

// endregion

// region Border and Shadow Demos

private fun borderShadowDemos(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Borders"),
            ComposeNode(
                type = ComposeType.Row,
                composeModifier = ComposeModifier(
                    operations = listOf(ComposeModifier.Operation.FillMaxWidth)
                ),
                properties = NodeProperties.RowProps(
                    horizontalArrangement = "SpaceEvenly",
                    children = listOf(
                        borderBox("1dp Solid", 1, DemoPalette.primary),
                        borderBox("4dp Rounded", 4, DemoPalette.accent, ComposeShape.RoundedCorner(all = 12)),
                    )
                )
            ),
            demoLabel("Shadows"),
            ComposeNode(
                type = ComposeType.Row,
                composeModifier = ComposeModifier(
                    operations = listOf(ComposeModifier.Operation.FillMaxWidth, ComposeModifier.Operation.Padding(16))
                ),
                properties = NodeProperties.RowProps(
                    horizontalArrangement = "SpaceEvenly",
                    children = listOf(
                        shadowBox("4dp Elevation", 4),
                        shadowBox("12dp Elevation", 12),
                    )
                )
            ),
        )
    )
)

private fun borderBox(label: String, width: Int, color: String, shape: ComposeShape = ComposeShape.RoundedCorner(all = 0)): ComposeNode = ComposeNode(
    type = ComposeType.Box,
    composeModifier = ComposeModifier(
        operations = listOf(
            ComposeModifier.Operation.Width(140),
            ComposeModifier.Operation.Height(60),
            ComposeModifier.Operation.Border(width, color, shape),
        )
    ),
    properties = NodeProperties.BoxProps(
        contentAlignment = "Center",
        children = listOf(
            ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(text = label),
            )
        )
    )
)

private fun shadowBox(label: String, elevation: Int): ComposeNode = ComposeNode(
    type = ComposeType.Box,
    composeModifier = ComposeModifier(
        operations = listOf(
            ComposeModifier.Operation.Width(140),
            ComposeModifier.Operation.Height(60),
            ComposeModifier.Operation.Shadow(elevation, ComposeShape.RoundedCorner(all = 8), clip = false),
            ComposeModifier.Operation.BackgroundColor(DemoPalette.background),
        )
    ),
    properties = NodeProperties.BoxProps(
        contentAlignment = "Center",
        children = listOf(
            ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(text = label),
            )
        )
    )
)

// endregion

// region Alpha and Rotation Demos

private fun alphaRotationDemos(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Alpha Gallery"),
            ComposeNode(
                type = ComposeType.Row,
                composeModifier = ComposeModifier(
                    operations = listOf(ComposeModifier.Operation.FillMaxWidth)
                ),
                properties = NodeProperties.RowProps(
                    horizontalArrangement = "SpaceEvenly",
                    children = listOf(
                        alphaBox("25%", 0.25f),
                        alphaBox("50%", 0.5f),
                        alphaBox("75%", 0.75f),
                        alphaBox("100%", 1.0f),
                    )
                )
            ),
            demoLabel("Rotation Gallery"),
            ComposeNode(
                type = ComposeType.Row,
                composeModifier = ComposeModifier(
                    operations = listOf(ComposeModifier.Operation.FillMaxWidth, ComposeModifier.Operation.Padding(16))
                ),
                properties = NodeProperties.RowProps(
                    horizontalArrangement = "SpaceEvenly",
                    children = listOf(
                        rotateBox("15°", 15f),
                        rotateBox("45°", 45f),
                        rotateBox("90°", 90f),
                        rotateBox("180°", 180f),
                    )
                )
            ),
        )
    )
)

private fun alphaBox(label: String, alpha: Float): ComposeNode = ComposeNode(
    type = ComposeType.Box,
    composeModifier = ComposeModifier(
        operations = listOf(
            ComposeModifier.Operation.Width(70),
            ComposeModifier.Operation.Height(70),
            ComposeModifier.Operation.Alpha(alpha),
            ComposeModifier.Operation.BackgroundColor(DemoPalette.primary),
        )
    ),
    properties = NodeProperties.BoxProps(
        contentAlignment = "Center",
        children = listOf(
            ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(text = label),
            )
        )
    )
)

private fun rotateBox(label: String, degrees: Float): ComposeNode = ComposeNode(
    type = ComposeType.Box,
    composeModifier = ComposeModifier(
        operations = listOf(
            ComposeModifier.Operation.Width(70),
            ComposeModifier.Operation.Height(70),
            ComposeModifier.Operation.Rotate(degrees),
            ComposeModifier.Operation.BackgroundColor(DemoPalette.secondary),
        )
    ),
    properties = NodeProperties.BoxProps(
        contentAlignment = "Center",
        children = listOf(
            ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(text = label),
            )
        )
    )
)

// endregion

// region Combined Modifiers Demo

private fun combinedModifiersDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("All-in-one Modifier Node"),
            ComposeNode(
                type = ComposeType.Box,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.Height(120),
                        ComposeModifier.Operation.Padding(16),
                        ComposeModifier.Operation.Rotate(-5f),
                        ComposeModifier.Operation.Shadow(8, ComposeShape.RoundedCorner(all = 16), clip = true),
                        ComposeModifier.Operation.Border(2, DemoPalette.accent, ComposeShape.RoundedCorner(all = 16)),
                        ComposeModifier.Operation.BackgroundColor("#FFFBE9E7"),
                    )
                ),
                properties = NodeProperties.BoxProps(
                    contentAlignment = "Center",
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(
                                text = "Shadow + Border + Clip + Rotate + Padding"
                            ),
                        )
                    )
                )
            )
        )
    )
)

// endregion
