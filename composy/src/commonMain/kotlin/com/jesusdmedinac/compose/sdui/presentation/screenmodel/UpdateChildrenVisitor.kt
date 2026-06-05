package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties

class UpdateChildrenVisitor(
    private val updatedNode: ComposeNode,
    private val updateRecursive: (ComposeNode, ComposeNode) -> ComposeNode
) : NodePropertiesVisitor<NodeProperties> {

    override fun visit(props: NodeProperties.ColumnProps) = 
        props.copy(children = props.children?.map { updateRecursive(it, updatedNode) })

    override fun visit(props: NodeProperties.RowProps) = 
        props.copy(children = props.children?.map { updateRecursive(it, updatedNode) })

    override fun visit(props: NodeProperties.BoxProps) = 
        props.copy(children = props.children?.map { updateRecursive(it, updatedNode) })

    override fun visit(props: NodeProperties.NavigationBarProps) = 
        props.copy(children = props.children?.map { updateRecursive(it, updatedNode) })

    override fun visit(props: NodeProperties.NavigationRailProps) = props.copy(
        header = props.header?.let { updateRecursive(it, updatedNode) },
        children = props.children?.map { updateRecursive(it, updatedNode) }
    )

    override fun visit(props: NodeProperties.TabRowProps) = 
        props.copy(children = props.children?.map { updateRecursive(it, updatedNode) })

    override fun visit(props: NodeProperties.FlowRowProps) = 
        props.copy(children = props.children?.map { updateRecursive(it, updatedNode) })

    override fun visit(props: NodeProperties.FlowColumnProps) = 
        props.copy(children = props.children?.map { updateRecursive(it, updatedNode) })

    override fun visit(props: NodeProperties.SegmentedButtonRowProps) = 
        props.copy(children = props.children?.map { updateRecursive(it, updatedNode) })

    override fun visit(props: NodeProperties.PagerProps) = 
        props.copy(pages = props.pages?.map { updateRecursive(it, updatedNode) })

    override fun visit(props: NodeProperties.SearchBarProps) = props.copy(
        placeholder = props.placeholder?.let { updateRecursive(it, updatedNode) },
        leadingIcon = props.leadingIcon?.let { updateRecursive(it, updatedNode) },
        trailingIcon = props.trailingIcon?.let { updateRecursive(it, updatedNode) },
        children = props.children?.map { updateRecursive(it, updatedNode) }
    )

    override fun visit(props: NodeProperties.BottomBarProps) = 
        props.copy(children = props.children?.map { updateRecursive(it, updatedNode) })

    override fun visit(props: NodeProperties.ButtonProps) = 
        props.copy(child = props.child?.let { updateRecursive(it, updatedNode) })

    override fun visit(props: NodeProperties.CardProps) = 
        props.copy(child = props.child?.let { updateRecursive(it, updatedNode) })

    override fun visit(props: NodeProperties.OutlinedCardProps) = 
        props.copy(child = props.child?.let { updateRecursive(it, updatedNode) })

    override fun visit(props: NodeProperties.SurfaceProps) = 
        props.copy(child = props.child?.let { updateRecursive(it, updatedNode) })

    override fun visit(props: NodeProperties.ModalBottomSheetProps) = 
        props.copy(child = props.child?.let { updateRecursive(it, updatedNode) })

    override fun visit(props: NodeProperties.NavigationDrawerProps) = props.copy(
        drawerContent = props.drawerContent?.map { updateRecursive(it, updatedNode) },
        child = props.child?.let { updateRecursive(it, updatedNode) }
    )

    override fun visit(props: NodeProperties.ScaffoldProps) = props.copy(
        topBar = props.topBar?.let { updateRecursive(it, updatedNode) },
        bottomBar = props.bottomBar?.let { updateRecursive(it, updatedNode) },
        child = props.child?.let { updateRecursive(it, updatedNode) },
        floatingActionButton = props.floatingActionButton?.let { updateRecursive(it, updatedNode) }
    )

    override fun visit(props: NodeProperties.FabProps) = 
        props.copy(icon = props.icon?.let { updateRecursive(it, updatedNode) })

    override fun visit(props: NodeProperties.BadgedBoxProps) = props.copy(
        badge = props.badge?.let { updateRecursive(it, updatedNode) },
        child = props.child?.let { updateRecursive(it, updatedNode) }
    )

    override fun visit(props: NodeProperties.AlertDialogProps) = props.copy(
        confirmButton = props.confirmButton?.let { updateRecursive(it, updatedNode) },
        dismissButton = props.dismissButton?.let { updateRecursive(it, updatedNode) },
        title = props.title?.let { updateRecursive(it, updatedNode) },
        text = props.text?.let { updateRecursive(it, updatedNode) },
        icon = props.icon?.let { updateRecursive(it, updatedNode) }
    )

    override fun visit(props: NodeProperties.TopAppBarProps) = props.copy(
        title = props.title?.let { updateRecursive(it, updatedNode) },
        navigationIcon = props.navigationIcon?.let { updateRecursive(it, updatedNode) },
        actions = props.actions?.map { updateRecursive(it, updatedNode) }
    )

    override fun visit(props: NodeProperties.ExtendedFabProps) = props.copy(
        icon = props.icon?.let { updateRecursive(it, updatedNode) },
        text = props.text?.let { updateRecursive(it, updatedNode) }
    )

    override fun visit(props: NodeProperties.TextFieldProps) = props.copy(
        placeholder = props.placeholder?.let { updateRecursive(it, updatedNode) },
        label = props.label?.let { updateRecursive(it, updatedNode) },
        leadingIcon = props.leadingIcon?.let { updateRecursive(it, updatedNode) },
        trailingIcon = props.trailingIcon?.let { updateRecursive(it, updatedNode) },
        supportingText = props.supportingText?.let { updateRecursive(it, updatedNode) },
        prefix = props.prefix?.let { updateRecursive(it, updatedNode) },
        suffix = props.suffix?.let { updateRecursive(it, updatedNode) }
    )

    override fun visit(props: NodeProperties.ListItemProps) = props.copy(
        headlineContent = props.headlineContent?.let { updateRecursive(it, updatedNode) },
        supportingContent = props.supportingContent?.let { updateRecursive(it, updatedNode) },
        overlineContent = props.overlineContent?.let { updateRecursive(it, updatedNode) },
        leadingContent = props.leadingContent?.let { updateRecursive(it, updatedNode) },
        trailingContent = props.trailingContent?.let { updateRecursive(it, updatedNode) }
    )

    override fun visit(props: NodeProperties.ChipProps) = props.copy(
        label = props.label?.let { updateRecursive(it, updatedNode) },
        leadingIcon = props.leadingIcon?.let { updateRecursive(it, updatedNode) }
    )

    override fun visit(props: NodeProperties.FilterChipProps) = props.copy(
        label = props.label?.let { updateRecursive(it, updatedNode) },
        leadingIcon = props.leadingIcon?.let { updateRecursive(it, updatedNode) }
    )

    override fun visit(props: NodeProperties.InputChipProps) = props.copy(
        label = props.label?.let { updateRecursive(it, updatedNode) },
        leadingIcon = props.leadingIcon?.let { updateRecursive(it, updatedNode) },
        trailingIcon = props.trailingIcon?.let { updateRecursive(it, updatedNode) }
    )

    override fun visit(props: NodeProperties.PlainTooltipProps) = 
        props.copy(anchor = props.anchor?.let { updateRecursive(it, updatedNode) })

    override fun visit(props: NodeProperties.RichTooltipProps) = props.copy(
        anchor = props.anchor?.let { updateRecursive(it, updatedNode) },
        title = props.title?.let { updateRecursive(it, updatedNode) },
        text = props.text?.let { updateRecursive(it, updatedNode) },
        action = props.action?.let { updateRecursive(it, updatedNode) }
    )

    override fun visit(props: NodeProperties.NavigationBarItemProps) = props.copy(
        label = props.label?.let { updateRecursive(it, updatedNode) },
        icon = props.icon?.let { updateRecursive(it, updatedNode) }
    )

    override fun visit(props: NodeProperties.NavigationRailItemProps) = props.copy(
        label = props.label?.let { updateRecursive(it, updatedNode) },
        icon = props.icon?.let { updateRecursive(it, updatedNode) }
    )

    override fun visit(props: NodeProperties.NavigationDrawerItemProps) = props.copy(
        label = props.label?.let { updateRecursive(it, updatedNode) },
        icon = props.icon?.let { updateRecursive(it, updatedNode) },
        badge = props.badge?.let { updateRecursive(it, updatedNode) }
    )

    override fun visit(props: NodeProperties.TabProps) = props.copy(
        text = props.text?.let { updateRecursive(it, updatedNode) },
        icon = props.icon?.let { updateRecursive(it, updatedNode) }
    )

    override fun visit(props: NodeProperties.BottomNavigationItemProps) = props.copy(
        label = props.label?.let { updateRecursive(it, updatedNode) },
        icon = props.icon?.let { updateRecursive(it, updatedNode) }
    )

    override fun visit(props: NodeProperties.SegmentedButtonProps) = props.copy(
        label = props.label?.let { updateRecursive(it, updatedNode) },
        icon = props.icon?.let { updateRecursive(it, updatedNode) }
    )

    override fun visitDefault(props: NodeProperties) = props
}
