package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties

class DeleteChildVisitor(
    private val targetId: String,
    private val deleteRecursive: (ComposeNode, String) -> ComposeNode?
) : NodePropertiesVisitor<NodeProperties> {

    override fun visit(props: NodeProperties.ColumnProps) = 
        props.copy(children = props.children?.mapNotNull { deleteRecursive(it, targetId) })

    override fun visit(props: NodeProperties.RowProps) = 
        props.copy(children = props.children?.mapNotNull { deleteRecursive(it, targetId) })

    override fun visit(props: NodeProperties.BoxProps) = 
        props.copy(children = props.children?.mapNotNull { deleteRecursive(it, targetId) })

    override fun visit(props: NodeProperties.NavigationBarProps) = 
        props.copy(children = props.children?.mapNotNull { deleteRecursive(it, targetId) })

    override fun visit(props: NodeProperties.NavigationRailProps) = props.copy(
        header = props.header?.let { deleteRecursive(it, targetId) },
        children = props.children?.mapNotNull { deleteRecursive(it, targetId) }
    )

    override fun visit(props: NodeProperties.TabRowProps) = 
        props.copy(children = props.children?.mapNotNull { deleteRecursive(it, targetId) })

    override fun visit(props: NodeProperties.FlowRowProps) = 
        props.copy(children = props.children?.mapNotNull { deleteRecursive(it, targetId) })

    override fun visit(props: NodeProperties.FlowColumnProps) = 
        props.copy(children = props.children?.mapNotNull { deleteRecursive(it, targetId) })

    override fun visit(props: NodeProperties.SegmentedButtonRowProps) = 
        props.copy(children = props.children?.mapNotNull { deleteRecursive(it, targetId) })

    override fun visit(props: NodeProperties.PagerProps) = 
        props.copy(pages = props.pages?.mapNotNull { deleteRecursive(it, targetId) })

    override fun visit(props: NodeProperties.SearchBarProps) = props.copy(
        placeholder = props.placeholder?.let { deleteRecursive(it, targetId) },
        leadingIcon = props.leadingIcon?.let { deleteRecursive(it, targetId) },
        trailingIcon = props.trailingIcon?.let { deleteRecursive(it, targetId) },
        children = props.children?.mapNotNull { deleteRecursive(it, targetId) }
    )

    override fun visit(props: NodeProperties.BottomBarProps) = 
        props.copy(children = props.children?.mapNotNull { deleteRecursive(it, targetId) })

    override fun visit(props: NodeProperties.ButtonProps) = 
        props.copy(child = props.child?.let { deleteRecursive(it, targetId) })

    override fun visit(props: NodeProperties.CardProps) = 
        props.copy(child = props.child?.let { deleteRecursive(it, targetId) })

    override fun visit(props: NodeProperties.OutlinedCardProps) = 
        props.copy(child = props.child?.let { deleteRecursive(it, targetId) })

    override fun visit(props: NodeProperties.SurfaceProps) = 
        props.copy(child = props.child?.let { deleteRecursive(it, targetId) })

    override fun visit(props: NodeProperties.ModalBottomSheetProps) = 
        props.copy(child = props.child?.let { deleteRecursive(it, targetId) })

    override fun visit(props: NodeProperties.NavigationDrawerProps) = props.copy(
        drawerContent = props.drawerContent?.mapNotNull { deleteRecursive(it, targetId) },
        child = props.child?.let { deleteRecursive(it, targetId) }
    )

    override fun visit(props: NodeProperties.ScaffoldProps) = props.copy(
        topBar = props.topBar?.let { deleteRecursive(it, targetId) },
        bottomBar = props.bottomBar?.let { deleteRecursive(it, targetId) },
        child = props.child?.let { deleteRecursive(it, targetId) },
        floatingActionButton = props.floatingActionButton?.let { deleteRecursive(it, targetId) }
    )

    override fun visit(props: NodeProperties.FabProps) = 
        props.copy(icon = props.icon?.let { deleteRecursive(it, targetId) })

    override fun visit(props: NodeProperties.BadgedBoxProps) = props.copy(
        badge = props.badge?.let { deleteRecursive(it, targetId) },
        child = props.child?.let { deleteRecursive(it, targetId) }
    )

    override fun visit(props: NodeProperties.AlertDialogProps) = props.copy(
        confirmButton = props.confirmButton?.let { deleteRecursive(it, targetId) },
        dismissButton = props.dismissButton?.let { deleteRecursive(it, targetId) },
        title = props.title?.let { deleteRecursive(it, targetId) },
        text = props.text?.let { deleteRecursive(it, targetId) },
        icon = props.icon?.let { deleteRecursive(it, targetId) }
    )

    override fun visit(props: NodeProperties.TopAppBarProps) = props.copy(
        title = props.title?.let { deleteRecursive(it, targetId) },
        navigationIcon = props.navigationIcon?.let { deleteRecursive(it, targetId) },
        actions = props.actions?.mapNotNull { deleteRecursive(it, targetId) }
    )

    override fun visit(props: NodeProperties.ExtendedFabProps) = props.copy(
        icon = props.icon?.let { deleteRecursive(it, targetId) },
        text = props.text?.let { deleteRecursive(it, targetId) }
    )

    override fun visit(props: NodeProperties.TextFieldProps) = props.copy(
        placeholder = props.placeholder?.let { deleteRecursive(it, targetId) },
        label = props.label?.let { deleteRecursive(it, targetId) },
        leadingIcon = props.leadingIcon?.let { deleteRecursive(it, targetId) },
        trailingIcon = props.trailingIcon?.let { deleteRecursive(it, targetId) },
        supportingText = props.supportingText?.let { deleteRecursive(it, targetId) },
        prefix = props.prefix?.let { deleteRecursive(it, targetId) },
        suffix = props.suffix?.let { deleteRecursive(it, targetId) }
    )

    override fun visit(props: NodeProperties.ListItemProps) = props.copy(
        headlineContent = props.headlineContent?.let { deleteRecursive(it, targetId) },
        supportingContent = props.supportingContent?.let { deleteRecursive(it, targetId) },
        overlineContent = props.overlineContent?.let { deleteRecursive(it, targetId) },
        leadingContent = props.leadingContent?.let { deleteRecursive(it, targetId) },
        trailingContent = props.trailingContent?.let { deleteRecursive(it, targetId) }
    )

    override fun visit(props: NodeProperties.ChipProps) = props.copy(
        label = props.label?.let { deleteRecursive(it, targetId) },
        leadingIcon = props.leadingIcon?.let { deleteRecursive(it, targetId) }
    )

    override fun visit(props: NodeProperties.FilterChipProps) = props.copy(
        label = props.label?.let { deleteRecursive(it, targetId) },
        leadingIcon = props.leadingIcon?.let { deleteRecursive(it, targetId) }
    )

    override fun visit(props: NodeProperties.InputChipProps) = props.copy(
        label = props.label?.let { deleteRecursive(it, targetId) },
        leadingIcon = props.leadingIcon?.let { deleteRecursive(it, targetId) },
        trailingIcon = props.trailingIcon?.let { deleteRecursive(it, targetId) }
    )

    override fun visit(props: NodeProperties.PlainTooltipProps) = 
        props.copy(anchor = props.anchor?.let { deleteRecursive(it, targetId) })

    override fun visit(props: NodeProperties.RichTooltipProps) = props.copy(
        anchor = props.anchor?.let { deleteRecursive(it, targetId) },
        title = props.title?.let { deleteRecursive(it, targetId) },
        text = props.text?.let { deleteRecursive(it, targetId) },
        action = props.action?.let { deleteRecursive(it, targetId) }
    )

    override fun visit(props: NodeProperties.NavigationBarItemProps) = props.copy(
        label = props.label?.let { deleteRecursive(it, targetId) },
        icon = props.icon?.let { deleteRecursive(it, targetId) }
    )

    override fun visit(props: NodeProperties.NavigationRailItemProps) = props.copy(
        label = props.label?.let { deleteRecursive(it, targetId) },
        icon = props.icon?.let { deleteRecursive(it, targetId) }
    )

    override fun visit(props: NodeProperties.NavigationDrawerItemProps) = props.copy(
        label = props.label?.let { deleteRecursive(it, targetId) },
        icon = props.icon?.let { deleteRecursive(it, targetId) },
        badge = props.badge?.let { deleteRecursive(it, targetId) }
    )

    override fun visit(props: NodeProperties.TabProps) = props.copy(
        text = props.text?.let { deleteRecursive(it, targetId) },
        icon = props.icon?.let { deleteRecursive(it, targetId) }
    )

    override fun visit(props: NodeProperties.BottomNavigationItemProps) = props.copy(
        label = props.label?.let { deleteRecursive(it, targetId) },
        icon = props.icon?.let { deleteRecursive(it, targetId) }
    )

    override fun visit(props: NodeProperties.SegmentedButtonProps) = props.copy(
        label = props.label?.let { deleteRecursive(it, targetId) },
        icon = props.icon?.let { deleteRecursive(it, targetId) }
    )

    override fun visitDefault(props: NodeProperties) = props
}
