package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties

/**
 * Transforms all children of this NodeProperties using the provided transform function.
 * This function handles the exhaustive mapping of all properties that contain ComposeNode instances.
 * If the transform returns null, the child is removed (from lists) or set to null.
 */
fun NodeProperties.mapChildren(transform: (ComposeNode) -> ComposeNode?): NodeProperties = when (this) {
    is NodeProperties.AlertDialogProps -> copy(
        confirmButton = confirmButton?.let(transform),
        dismissButton = dismissButton?.let(transform),
        title = title?.let(transform),
        text = text?.let(transform),
        icon = icon?.let(transform)
    )
    is NodeProperties.BadgeProps -> this
    is NodeProperties.BadgedBoxProps -> copy(
        badge = badge?.let(transform),
        child = child?.let(transform)
    )
    is NodeProperties.BottomBarProps -> copy(children = children?.mapNotNull(transform))
    is NodeProperties.BottomNavigationItemProps -> copy(
        label = label?.let(transform),
        icon = icon?.let(transform)
    )
    is NodeProperties.BoxProps -> copy(children = children?.mapNotNull(transform))
    is NodeProperties.ButtonProps -> copy(child = child?.let(transform))
    is NodeProperties.CardProps -> copy(child = child?.let(transform))
    is NodeProperties.CheckboxProps -> this
    is NodeProperties.ChipProps -> copy(
        label = label?.let(transform),
        leadingIcon = leadingIcon?.let(transform)
    )
    is NodeProperties.ColumnProps -> copy(children = children?.mapNotNull(transform))
    is NodeProperties.CustomProps -> this
    is NodeProperties.DatePickerProps -> this
    is NodeProperties.DividerProps -> this
    is NodeProperties.ExtendedFabProps -> copy(
        icon = icon?.let(transform),
        text = text?.let(transform)
    )
    is NodeProperties.FabProps -> copy(icon = icon?.let(transform))
    is NodeProperties.FilterChipProps -> copy(
        label = label?.let(transform),
        leadingIcon = leadingIcon?.let(transform)
    )
    is NodeProperties.FlowColumnProps -> copy(children = children?.mapNotNull(transform))
    is NodeProperties.FlowRowProps -> copy(children = children?.mapNotNull(transform))
    is NodeProperties.IconProps -> this
    is NodeProperties.ImageProps -> this
    is NodeProperties.InputChipProps -> copy(
        label = label?.let(transform),
        leadingIcon = leadingIcon?.let(transform),
        trailingIcon = trailingIcon?.let(transform)
    )
    is NodeProperties.ListItemProps -> copy(
        headlineContent = headlineContent?.let(transform),
        supportingContent = supportingContent?.let(transform),
        overlineContent = overlineContent?.let(transform),
        leadingContent = leadingContent?.let(transform),
        trailingContent = trailingContent?.let(transform)
    )
    is NodeProperties.ModalBottomSheetProps -> copy(child = child?.let(transform))
    is NodeProperties.NavigationBarItemProps -> copy(
        label = label?.let(transform),
        icon = icon?.let(transform)
    )
    is NodeProperties.NavigationBarProps -> copy(children = children?.mapNotNull(transform))
    is NodeProperties.NavigationDrawerItemProps -> copy(
        label = label?.let(transform),
        icon = icon?.let(transform),
        badge = badge?.let(transform)
    )
    is NodeProperties.NavigationDrawerProps -> copy(
        drawerContent = drawerContent?.mapNotNull(transform),
        child = child?.let(transform)
    )
    is NodeProperties.NavigationRailItemProps -> copy(
        label = label?.let(transform),
        icon = icon?.let(transform)
    )
    is NodeProperties.NavigationRailProps -> copy(
        header = header?.let(transform),
        children = children?.mapNotNull(transform)
    )
    is NodeProperties.OutlinedCardProps -> copy(child = child?.let(transform))
    is NodeProperties.PagerProps -> copy(pages = pages?.mapNotNull(transform))
    is NodeProperties.PlainTooltipProps -> copy(anchor = anchor?.let(transform))
    is NodeProperties.ProgressIndicatorProps -> this
    is NodeProperties.RadioButtonProps -> this
    is NodeProperties.RichTooltipProps -> copy(
        anchor = anchor?.let(transform),
        title = title?.let(transform),
        text = text?.let(transform),
        action = action?.let(transform)
    )
    is NodeProperties.RowProps -> copy(children = children?.mapNotNull(transform))
    is NodeProperties.ScaffoldProps -> copy(
        topBar = topBar?.let(transform),
        bottomBar = bottomBar?.let(transform),
        child = child?.let(transform),
        floatingActionButton = floatingActionButton?.let(transform)
    )
    is NodeProperties.SearchBarProps -> copy(
        placeholder = placeholder?.let(transform),
        leadingIcon = leadingIcon?.let(transform),
        trailingIcon = trailingIcon?.let(transform),
        children = children?.mapNotNull(transform)
    )
    is NodeProperties.SegmentedButtonProps -> copy(
        label = label?.let(transform),
        icon = icon?.let(transform)
    )
    is NodeProperties.SegmentedButtonRowProps -> copy(children = children?.mapNotNull(transform))
    is NodeProperties.SliderProps -> this
    is NodeProperties.SnackbarHostProps -> this
    is NodeProperties.SpacerProps -> this
    is NodeProperties.SurfaceProps -> copy(child = child?.let(transform))
    is NodeProperties.SwitchProps -> this
    is NodeProperties.TabProps -> copy(
        text = text?.let(transform),
        icon = icon?.let(transform)
    )
    is NodeProperties.TabRowProps -> copy(children = children?.mapNotNull(transform))
    is NodeProperties.TextFieldProps -> copy(
        placeholder = placeholder?.let(transform),
        label = label?.let(transform),
        leadingIcon = leadingIcon?.let(transform),
        trailingIcon = trailingIcon?.let(transform),
        supportingText = supportingText?.let(transform),
        prefix = prefix?.let(transform),
        suffix = suffix?.let(transform)
    )
    is NodeProperties.TextProps -> this
    is NodeProperties.TimePickerProps -> this
    is NodeProperties.TopAppBarProps -> copy(
        title = title?.let(transform),
        navigationIcon = navigationIcon?.let(transform),
        actions = actions?.mapNotNull(transform)
    )
}
