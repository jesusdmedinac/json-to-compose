package com.jesusdmedinac.jsontocompose.model

/**
 * Enumerates all supported Compose component types that can be rendered from JSON.
 *
 * Each entry maps to a specific Compose widget and expects a corresponding
 * [NodeProperties] subclass for its configuration.
 */
enum class ComposeType {
    /** Vertical layout container. Uses [NodeProperties.ColumnProps]. */
    Column,
    /** Horizontal layout container. Uses [NodeProperties.RowProps]. */
    Row,
    /** Overlay/stacking layout container. Uses [NodeProperties.BoxProps]. */
    Box,
    /** Displays a text string. Uses [NodeProperties.TextProps]. */
    Text,
    /** Clickable button with a single child. Uses [NodeProperties.ButtonProps]. */
    Button,
    /** Displays an image from URL or local resource. Uses [NodeProperties.ImageProps]. */
    Image,
    /** Editable text input field. Uses [NodeProperties.TextFieldProps]. */
    TextField,
    /** Vertically scrolling lazy list. Uses [NodeProperties.ColumnProps]. */
    LazyColumn,
    /** Horizontally scrolling lazy list. Uses [NodeProperties.RowProps]. */
    LazyRow,
    /** Material Scaffold with topBar, bottomBar, and content slots. Uses [NodeProperties.ScaffoldProps]. */
    Scaffold,
    /** Material Card container with elevation and shape. Uses [NodeProperties.CardProps]. */
    Card,
    /** Material AlertDialog with confirm/dismiss buttons. Uses [NodeProperties.AlertDialogProps]. */
    AlertDialog,
    /** Material TopAppBar with title, navigation icon, and actions. Uses [NodeProperties.TopAppBarProps]. */
    TopAppBar,
    /** Bottom navigation bar container. Uses [NodeProperties.BottomBarProps]. */
    BottomBar,
    /** Single item inside a bottom navigation bar. Uses [NodeProperties.BottomNavigationItemProps]. */
    BottomNavigationItem,
    /** Material toggle switch. Uses [NodeProperties.SwitchProps]. */
    Switch,
    /** Material checkbox. Uses [NodeProperties.CheckboxProps]. */
    Checkbox,
    /** Custom component rendered by a user-provided composable. Uses [NodeProperties.CustomProps]. */
    Custom;

    /**
     * Returns `true` if this type is a layout container (Column, Row, or Box).
     */
    fun isLayout(): Boolean = when (this) {
        Column, Row, Box -> true
        else -> false
    }

    /**
     * Returns `true` if this type holds a single child (Button or Card).
     */
    fun hasChild(): Boolean = when (this) {
        Button, Card -> true
        else -> false
    }
}
