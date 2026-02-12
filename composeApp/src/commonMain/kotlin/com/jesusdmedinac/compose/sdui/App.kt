package com.jesusdmedinac.compose.sdui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.LocalCustomRenderers
import com.jesusdmedinac.jsontocompose.LocalDrawableResources
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.ToCompose
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state.MutableStateHost
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state.StateHost
import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeShape
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import json_to_compose.composeapp.generated.resources.Res
import json_to_compose.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.ui.tooling.preview.Preview

// region Navigation routes

@Serializable data object CatalogRoute
@Serializable data object ComponentsRoute
@Serializable data object StylesRoute

val LocalNavController = compositionLocalOf<NavHostController> {
    error("No NavHostController provided")
}

// endregion

// region Helper functions

fun sectionHeader(title: String): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(
            ComposeModifier.Operation.FillMaxWidth,
            ComposeModifier.Operation.BackgroundColor("#FF1565C0"),
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
            ComposeModifier.Operation.BackgroundColor("#FFE0E0E0"),
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

// endregion

// region Section builders

fun appTitleHeader(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(
            ComposeModifier.Operation.FillMaxWidth,
            ComposeModifier.Operation.BackgroundColor("#FF0D47A1"),
            ComposeModifier.Operation.Padding(24),
        )
    ),
    properties = NodeProperties.ColumnProps(
        horizontalAlignment = "CenterHorizontally",
        children = listOf(
            ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(text = "json-to-compose Showcase"),
            ),
            ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(text = "All UI rendered from ComposeNode trees"),
                composeModifier = ComposeModifier(
                    operations = listOf(ComposeModifier.Operation.Padding(4))
                ),
            ),
        )
    )
)

// --- Layout Components ---

fun columnDemo(): ComposeNode = ComposeNode(
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
                        ComposeModifier.Operation.BackgroundColor("#FFF5F5F5"),
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

fun rowDemo(): ComposeNode = ComposeNode(
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
                        ComposeModifier.Operation.BackgroundColor("#FFF5F5F5"),
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

fun boxDemo(): ComposeNode = ComposeNode(
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
                        ComposeModifier.Operation.BackgroundColor("#FFF5F5F5"),
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

// --- Content Components ---

fun textDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Text"),
            ComposeNode(
                type = ComposeType.Text,
                composeModifier = ComposeModifier(
                    operations = listOf(ComposeModifier.Operation.Padding(8))
                ),
                properties = NodeProperties.TextProps(text = "Hello from json-to-compose!"),
            ),
        )
    )
)

fun imageUrlDemo(): ComposeNode = ComposeNode(
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

fun imageResourceDemo(): ComposeNode = ComposeNode(
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

// --- Input Components ---

fun buttonDemo(): ComposeNode = ComposeNode(
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

fun textFieldDemo(): ComposeNode = ComposeNode(
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

fun switchDemo(): ComposeNode = ComposeNode(
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

fun checkboxDemo(): ComposeNode = ComposeNode(
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
                            properties = NodeProperties.TextProps(text = "Accept terms and conditions"),
                        ),
                    )
                )
            ),
        )
    )
)

// --- Containers ---

fun cardDemo(): ComposeNode = ComposeNode(
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

fun scaffoldDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Scaffold"),
            ComposeNode(
                type = ComposeType.Scaffold,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.Height(200),
                    )
                ),
                properties = NodeProperties.ScaffoldProps(
                    topBar = ComposeNode(
                        type = ComposeType.TopAppBar,
                        properties = NodeProperties.TopAppBarProps(
                            title = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Scaffold Demo"),
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
                    child = ComposeNode(
                        type = ComposeType.Text,
                        composeModifier = ComposeModifier(
                            operations = listOf(ComposeModifier.Operation.Padding(16))
                        ),
                        properties = NodeProperties.TextProps(text = "Content inside Scaffold"),
                    )
                )
            ),
        )
    )
)

fun alertDialogDemo(): ComposeNode = ComposeNode(
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

// --- Navigation ---

fun topAppBarDemo(): ComposeNode = ComposeNode(
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
                    navigationIcon = ComposeNode(
                        type = ComposeType.Button,
                        properties = NodeProperties.ButtonProps(
                            onClickEventName = "button_clicked",
                            child = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "<"),
                            )
                        )
                    ),
                    actions = listOf(
                        ComposeNode(
                            type = ComposeType.Button,
                            properties = NodeProperties.ButtonProps(
                                onClickEventName = "button_clicked",
                                child = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Action"),
                                )
                            )
                        ),
                    ),
                )
            ),
        )
    )
)

fun bottomBarDemo(): ComposeNode = ComposeNode(
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
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.BottomNavigationItem,
                            properties = NodeProperties.BottomNavigationItemProps(
                                selected = true,
                                onClickEventName = "button_clicked",
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
                                onClickEventName = "button_clicked",
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
                        ComposeNode(
                            type = ComposeType.BottomNavigationItem,
                            properties = NodeProperties.BottomNavigationItemProps(
                                selected = false,
                                onClickEventName = "button_clicked",
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
        )
    )
)

// --- Lazy Lists ---

fun lazyColumnDemo(): ComposeNode = ComposeNode(
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
                        ComposeModifier.Operation.BackgroundColor("#FFF5F5F5"),
                    )
                ),
                properties = NodeProperties.ColumnProps(
                    children = (1..10).map { i ->
                        ComposeNode(
                            type = ComposeType.Text,
                            composeModifier = ComposeModifier(
                                operations = listOf(ComposeModifier.Operation.Padding(4))
                            ),
                            properties = NodeProperties.TextProps(text = "LazyColumn item $i"),
                        )
                    }
                )
            ),
        )
    )
)

fun lazyRowDemo(): ComposeNode = ComposeNode(
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
                    children = (1..10).map { i ->
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

// --- Modifiers Showcase ---

fun paddingDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Padding"),
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
                                text = "24dp padding (visible via background)"
                            ),
                        ),
                    )
                )
            ),
        )
    )
)

fun fillMaxWidthDemo(): ComposeNode = ComposeNode(
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
                            properties = NodeProperties.TextProps(text = "Full width box"),
                        ),
                    )
                )
            ),
        )
    )
)

fun widthHeightDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Width(100) + Height(50)"),
            ComposeNode(
                type = ComposeType.Box,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.Width(100),
                        ComposeModifier.Operation.Height(50),
                        ComposeModifier.Operation.BackgroundColor("#FF64B5F6"),
                    )
                ),
                properties = NodeProperties.BoxProps(
                    contentAlignment = "Center",
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "100x50"),
                        ),
                    )
                )
            ),
        )
    )
)

fun backgroundColorDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("BackgroundColor"),
            ComposeNode(
                type = ComposeType.Box,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.Height(40),
                        ComposeModifier.Operation.BackgroundColor("#FFFF5722"),
                    )
                ),
                properties = NodeProperties.BoxProps(
                    contentAlignment = "Center",
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "Solid hex color background"),
                        ),
                    )
                )
            ),
        )
    )
)

fun backgroundShapeDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Background + RoundedCorner Shape"),
            ComposeNode(
                type = ComposeType.Box,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.Height(50),
                        ComposeModifier.Operation.Background(
                            color = "#FF4CAF50",
                            shape = ComposeShape.RoundedCorner(all = 16)
                        ),
                    )
                ),
                properties = NodeProperties.BoxProps(
                    contentAlignment = "Center",
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(
                                text = "Rounded corner background (16dp)"
                            ),
                        ),
                    )
                )
            ),
        )
    )
)

fun borderDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Border"),
            ComposeNode(
                type = ComposeType.Box,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.Height(50),
                        ComposeModifier.Operation.Border(
                            width = 2,
                            color = "#FF1565C0",
                            shape = ComposeShape.RoundedCorner(all = 8)
                        ),
                    )
                ),
                properties = NodeProperties.BoxProps(
                    contentAlignment = "Center",
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(
                                text = "2dp border with 8dp rounded corners"
                            ),
                        ),
                    )
                )
            ),
        )
    )
)

fun shadowDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Shadow"),
            ComposeNode(
                type = ComposeType.Box,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.Height(50),
                        ComposeModifier.Operation.Shadow(
                            elevation = 8,
                            shape = ComposeShape.RoundedCorner(all = 8),
                            clip = false
                        ),
                        ComposeModifier.Operation.BackgroundColor("#FFFFFFFF"),
                    )
                ),
                properties = NodeProperties.BoxProps(
                    contentAlignment = "Center",
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(
                                text = "8dp elevation shadow"
                            ),
                        ),
                    )
                )
            ),
        )
    )
)

fun clipDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Clip (Circle)"),
            ComposeNode(
                type = ComposeType.Box,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.Width(80),
                        ComposeModifier.Operation.Height(80),
                        ComposeModifier.Operation.Clip(shape = ComposeShape.Circle),
                        ComposeModifier.Operation.BackgroundColor("#FF42A5F5"),
                    )
                ),
                properties = NodeProperties.BoxProps(
                    contentAlignment = "Center",
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "Clip"),
                        ),
                    )
                )
            ),
        )
    )
)

fun alphaDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Alpha (50% vs 100%)"),
            ComposeNode(
                type = ComposeType.Row,
                composeModifier = ComposeModifier(
                    operations = listOf(ComposeModifier.Operation.FillMaxWidth)
                ),
                properties = NodeProperties.RowProps(
                    horizontalArrangement = "SpaceEvenly",
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Text,
                            composeModifier = ComposeModifier(
                                operations = listOf(
                                    ComposeModifier.Operation.Alpha(0.5f),
                                    ComposeModifier.Operation.Padding(8),
                                    ComposeModifier.Operation.BackgroundColor("#FF2196F3"),
                                )
                            ),
                            properties = NodeProperties.TextProps(text = "50% opacity"),
                        ),
                        ComposeNode(
                            type = ComposeType.Text,
                            composeModifier = ComposeModifier(
                                operations = listOf(
                                    ComposeModifier.Operation.Alpha(1.0f),
                                    ComposeModifier.Operation.Padding(8),
                                    ComposeModifier.Operation.BackgroundColor("#FF2196F3"),
                                )
                            ),
                            properties = NodeProperties.TextProps(text = "100% opacity"),
                        ),
                    )
                )
            ),
        )
    )
)

fun rotateDemo(): ComposeNode = ComposeNode(
    type = ComposeType.Column,
    composeModifier = ComposeModifier(
        operations = listOf(ComposeModifier.Operation.Padding(8))
    ),
    properties = NodeProperties.ColumnProps(
        children = listOf(
            demoLabel("Rotate (15 degrees)"),
            ComposeNode(
                type = ComposeType.Box,
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.FillMaxWidth,
                        ComposeModifier.Operation.Height(60),
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
                            properties = NodeProperties.TextProps(text = "Rotated 15 degrees"),
                        ),
                    )
                )
            ),
        )
    )
)

// --- Custom Renderer ---

fun customRendererDemo(): ComposeNode = ComposeNode(
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

@Composable
@Preview
fun App() {
    val navController = rememberNavController()

    val drawableResources = mapOf(
        "compose-multiplatform" to Res.drawable.compose_multiplatform
    )

    val textFieldHost = remember { MutableStateHost("") }
    val dialogHost = remember { MutableStateHost(false) }
    val switchStateHost = remember { MutableStateHost(false) }
    val switchEnabledHost = remember { MutableStateHost(true) }
    val checkboxCheckedHost = remember { MutableStateHost(false) }
    val checkboxEnabledHost = remember { MutableStateHost(true) }

    val catalogSelectedHost = remember { MutableStateHost(true) }
    val componentsSelectedHost = remember { MutableStateHost(false) }
    val stylesSelectedHost = remember { MutableStateHost(false) }

    fun updateTabSelection(tab: String) {
        catalogSelectedHost.onStateChange(tab == "catalog")
        componentsSelectedHost.onStateChange(tab == "components")
        stylesSelectedHost.onStateChange(tab == "styles")
    }

    val behaviors = mapOf(
        "button_clicked" to object : Behavior {
            override fun invoke() {
                println("Button clicked: button_clicked")
            }
        },
        "show_dialog" to object : Behavior {
            override fun invoke() {
                dialogHost.onStateChange(true)
            }
        },
        "dialog_confirm" to object : Behavior {
            override fun invoke() {
                println("Dialog confirmed")
                dialogHost.onStateChange(false)
            }
        },
        "dialog_dismiss" to object : Behavior {
            override fun invoke() {
                println("Dialog dismissed")
                dialogHost.onStateChange(false)
            }
        },
        "switch_toggled" to object : Behavior {
            override fun invoke() {
                println("Switch toggled: ${switchStateHost.state}")
            }
        },
        "checkbox_toggled" to object : Behavior {
            override fun invoke() {
                println("Checkbox toggled: ${checkboxCheckedHost.state}")
            }
        },
        "navigate_catalog" to object : Behavior {
            override fun invoke() {
                updateTabSelection("catalog")
                navController.navigate(CatalogRoute) {
                    popUpTo<CatalogRoute> { inclusive = false }
                    launchSingleTop = true
                }
            }
        },
        "navigate_components" to object : Behavior {
            override fun invoke() {
                updateTabSelection("components")
                navController.navigate(ComponentsRoute) {
                    popUpTo<CatalogRoute> { inclusive = false }
                    launchSingleTop = true
                }
            }
        },
        "navigate_styles" to object : Behavior {
            override fun invoke() {
                updateTabSelection("styles")
                navController.navigate(StylesRoute) {
                    popUpTo<CatalogRoute> { inclusive = false }
                    launchSingleTop = true
                }
            }
        },
    )

    val stateHosts = mapOf<String, StateHost<*>>(
        "text_field_value" to textFieldHost,
        "dialog_visibility" to dialogHost,
        "switch_state" to switchStateHost,
        "checkbox_checked" to checkboxCheckedHost,
        "checkbox_enabled" to checkboxEnabledHost,
        "switch_enabled" to switchEnabledHost,
        "catalog_selected" to catalogSelectedHost,
        "components_selected" to componentsSelectedHost,
        "styles_selected" to stylesSelectedHost,
    )

    val customRenderers: Map<String, @Composable (ComposeNode) -> Unit> = mapOf(
        "ProductCard" to { node ->
            val customProps = node.properties as? NodeProperties.CustomProps
            val customData = customProps?.customData
            val title = customData?.get("title")?.jsonPrimitive?.content ?: "No title"
            val price = customData?.get("price")?.jsonPrimitive?.content ?: "0.00"
            Column {
                Text(text = "Product: $title")
                Text(text = "Price: $$price")
            }
        },
        "NavHost" to {
            val currentNavController = LocalNavController.current
            NavHost(
                navController = currentNavController,
                startDestination = CatalogRoute,
            ) {
                composable<CatalogRoute> {
                    ComposeNode(
                        type = ComposeType.Text,
                        composeModifier = ComposeModifier(
                            operations = listOf(ComposeModifier.Operation.Padding(16))
                        ),
                        properties = NodeProperties.TextProps(text = "Catalog Screen"),
                    ).toString().ToCompose()
                }
                composable<ComponentsRoute> {
                    ComposeNode(
                        type = ComposeType.Text,
                        composeModifier = ComposeModifier(
                            operations = listOf(ComposeModifier.Operation.Padding(16))
                        ),
                        properties = NodeProperties.TextProps(text = "Components Screen"),
                    ).toString().ToCompose()
                }
                composable<StylesRoute> {
                    ComposeNode(
                        type = ComposeType.Text,
                        composeModifier = ComposeModifier(
                            operations = listOf(ComposeModifier.Operation.Padding(16))
                        ),
                        properties = NodeProperties.TextProps(text = "Styles Screen"),
                    ).toString().ToCompose()
                }
            }
        },
    )

    CompositionLocalProvider(LocalNavController provides navController) {
        CompositionProviders(
            drawableResources = drawableResources,
            behaviors = behaviors,
            stateHosts = stateHosts,
            customRenderers = customRenderers,
        ) {
            MaterialTheme {
                ComposeNode(
                    type = ComposeType.Scaffold,
                    composeModifier = ComposeModifier(
                        operations = listOf(ComposeModifier.Operation.FillMaxSize)
                    ),
                    properties = NodeProperties.ScaffoldProps(
                        topBar = ComposeNode(
                            type = ComposeType.TopAppBar,
                            properties = NodeProperties.TopAppBarProps(
                                title = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "json-to-compose"),
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
                                            selectedStateHostName = "catalog_selected",
                                            onClickEventName = "navigate_catalog",
                                            label = ComposeNode(
                                                type = ComposeType.Text,
                                                properties = NodeProperties.TextProps(text = "Catalog"),
                                            ),
                                            icon = ComposeNode(
                                                type = ComposeType.Text,
                                                properties = NodeProperties.TextProps(text = "C"),
                                            ),
                                        )
                                    ),
                                    ComposeNode(
                                        type = ComposeType.BottomNavigationItem,
                                        properties = NodeProperties.BottomNavigationItemProps(
                                            selectedStateHostName = "components_selected",
                                            onClickEventName = "navigate_components",
                                            label = ComposeNode(
                                                type = ComposeType.Text,
                                                properties = NodeProperties.TextProps(text = "Components"),
                                            ),
                                            icon = ComposeNode(
                                                type = ComposeType.Text,
                                                properties = NodeProperties.TextProps(text = "Co"),
                                            ),
                                        )
                                    ),
                                    ComposeNode(
                                        type = ComposeType.BottomNavigationItem,
                                        properties = NodeProperties.BottomNavigationItemProps(
                                            selectedStateHostName = "styles_selected",
                                            onClickEventName = "navigate_styles",
                                            label = ComposeNode(
                                                type = ComposeType.Text,
                                                properties = NodeProperties.TextProps(text = "Styles"),
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
                        child = ComposeNode(
                            type = ComposeType.Custom,
                            properties = NodeProperties.CustomProps(
                                customType = "NavHost",
                            )
                        ),
                    )
                ).toString().ToCompose()
            }
        }
    }
}

@Composable
fun CompositionProviders(
    drawableResources: Map<String, DrawableResource>,
    behaviors: Map<String, Behavior>,
    stateHosts: Map<String, StateHost<*>>,
    customRenderers: Map<String, @Composable (ComposeNode) -> Unit>,
    content: @Composable () -> Unit
) {
    DrawableResourcesComposition(drawableResources = drawableResources) {
        BehaviorComposition(behaviors = behaviors) {
            StateHostComposition(stateHosts = stateHosts) {
                CustomRenderersComposition(customRenderers = customRenderers) {
                    content()
                }
            }
        }
    }
}

@Composable
fun DrawableResourcesComposition(
    drawableResources: Map<String, DrawableResource>,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalDrawableResources provides drawableResources) {
        content()
    }
}

@Composable
fun BehaviorComposition(
    behaviors: Map<String, Behavior>,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalBehavior provides behaviors) {
        content()
    }
}

@Composable
fun StateHostComposition(
    stateHosts: Map<String, StateHost<*>>,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalStateHost provides stateHosts) {
        content()
    }
}

@Composable
fun CustomRenderersComposition(
    customRenderers: Map<String, @Composable (ComposeNode) -> Unit>,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalCustomRenderers provides customRenderers) {
        content()
    }
}
