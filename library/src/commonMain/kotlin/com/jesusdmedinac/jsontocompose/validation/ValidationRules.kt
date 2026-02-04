package com.jesusdmedinac.jsontocompose.validation

import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.reflect.KClass

/**
 * Maps each [ComposeType] to its expected [NodeProperties] subclass.
 *
 * Used during validation to verify that a node's properties match its type.
 */
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

/**
 * Component types that are leaf nodes and must not have children.
 */
val LEAF_TYPES: Set<ComposeType> = setOf(
    ComposeType.Text,
    ComposeType.Image,
    ComposeType.TextField,
    ComposeType.Switch,
    ComposeType.Checkbox,
)

/** Valid string values for vertical arrangements (Column, LazyColumn). */
val VALID_VERTICAL_ARRANGEMENTS: Set<String> = setOf(
    "Top", "Bottom", "Center",
    "SpaceEvenly", "SpaceBetween", "SpaceAround",
)

/** Valid string values for horizontal arrangements (Row, LazyRow). */
val VALID_HORIZONTAL_ARRANGEMENTS: Set<String> = setOf(
    "Start", "End", "Center",
    "SpaceEvenly", "SpaceBetween", "SpaceAround",
    "AbsoluteLeft", "AbsoluteCenter", "AbsoluteRight",
    "AbsoluteSpaceBetween", "AbsoluteSpaceEvenly", "AbsoluteSpaceAround",
)

/** Valid string values for two-dimensional alignments (Box contentAlignment). */
val VALID_ALIGNMENTS: Set<String> = setOf(
    "TopStart", "TopCenter", "TopEnd",
    "CenterStart", "Center", "CenterEnd",
    "BottomStart", "BottomCenter", "BottomEnd",
)

/** Valid string values for vertical alignments (Row verticalAlignment). */
val VALID_VERTICAL_ALIGNMENTS: Set<String> = setOf(
    "Top", "CenterVertically", "Bottom",
)

/** Valid string values for horizontal alignments (Column horizontalAlignment). */
val VALID_HORIZONTAL_ALIGNMENTS: Set<String> = setOf(
    "Start", "CenterHorizontally", "End",
)

/** Regex matching valid `#AARRGGBB` hex color strings. */
val HEX_COLOR_REGEX = Regex("^#[0-9A-Fa-f]{8}$")
