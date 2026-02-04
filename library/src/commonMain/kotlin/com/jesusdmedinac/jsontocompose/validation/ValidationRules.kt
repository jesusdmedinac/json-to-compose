package com.jesusdmedinac.jsontocompose.validation

import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.reflect.KClass

val EXPECTED_PROPERTIES_TYPE: Map<ComposeType, KClass<out NodeProperties>> = mapOf(
    ComposeType.Column to NodeProperties.ColumnProps::class,
    ComposeType.Row to NodeProperties.RowProps::class,
    ComposeType.Box to NodeProperties.BoxProps::class,
    ComposeType.Text to NodeProperties.TextProps::class,
    ComposeType.Button to NodeProperties.ButtonProps::class,
    ComposeType.Image to NodeProperties.ImageProps::class,
    ComposeType.TextField to NodeProperties.TextFieldProps::class,
    ComposeType.LazyColumn to NodeProperties.ColumnProps::class,
    ComposeType.LazyRow to NodeProperties.RowProps::class,
    ComposeType.Scaffold to NodeProperties.ScaffoldProps::class,
    ComposeType.Card to NodeProperties.CardProps::class,
    ComposeType.AlertDialog to NodeProperties.AlertDialogProps::class,
    ComposeType.TopAppBar to NodeProperties.TopAppBarProps::class,
    ComposeType.BottomBar to NodeProperties.BottomBarProps::class,
    ComposeType.BottomNavigationItem to NodeProperties.BottomNavigationItemProps::class,
    ComposeType.Switch to NodeProperties.SwitchProps::class,
    ComposeType.Checkbox to NodeProperties.CheckboxProps::class,
    ComposeType.Custom to NodeProperties.CustomProps::class,
)

val LEAF_TYPES: Set<ComposeType> = setOf(
    ComposeType.Text,
    ComposeType.Image,
    ComposeType.TextField,
    ComposeType.Switch,
    ComposeType.Checkbox,
)

val VALID_VERTICAL_ARRANGEMENTS: Set<String> = setOf(
    "Top", "Bottom", "Center",
    "SpaceEvenly", "SpaceBetween", "SpaceAround",
)

val VALID_HORIZONTAL_ARRANGEMENTS: Set<String> = setOf(
    "Start", "End", "Center",
    "SpaceEvenly", "SpaceBetween", "SpaceAround",
    "AbsoluteLeft", "AbsoluteCenter", "AbsoluteRight",
    "AbsoluteSpaceBetween", "AbsoluteSpaceEvenly", "AbsoluteSpaceAround",
)

val VALID_ALIGNMENTS: Set<String> = setOf(
    "TopStart", "TopCenter", "TopEnd",
    "CenterStart", "Center", "CenterEnd",
    "BottomStart", "BottomCenter", "BottomEnd",
)

val VALID_VERTICAL_ALIGNMENTS: Set<String> = setOf(
    "Top", "CenterVertically", "Bottom",
)

val VALID_HORIZONTAL_ALIGNMENTS: Set<String> = setOf(
    "Start", "CenterHorizontally", "End",
)

val HEX_COLOR_REGEX = Regex("^#[0-9A-Fa-f]{8}$")
