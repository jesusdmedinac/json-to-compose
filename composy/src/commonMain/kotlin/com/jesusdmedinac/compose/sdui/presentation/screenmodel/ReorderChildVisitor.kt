package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties

class ReorderChildVisitor(
    private val targetId: String,
    private val direction: EditorIntent.ReorderNode.Direction,
    private val reorderRecursive: (ComposeNode, String, EditorIntent.ReorderNode.Direction) -> ComposeNode
) : NodePropertiesVisitor<NodeProperties> {

    private fun reorderList(children: List<ComposeNode>?): List<ComposeNode>? {
        if (children == null) return null
        val index = children.indexOfFirst { it.id == targetId }
        if (index != -1) {
            val newList = children.toMutableList()
            val swapIndex = when (direction) {
                EditorIntent.ReorderNode.Direction.UP -> index - 1
                EditorIntent.ReorderNode.Direction.DOWN -> index + 1
            }
            if (swapIndex in newList.indices) {
                val temp = newList[index]
                newList[index] = newList[swapIndex]
                newList[swapIndex] = temp
            }
            return newList
        }
        return children.map { reorderRecursive(it, targetId, direction) }
    }

    private fun reorderSingle(child: ComposeNode?): ComposeNode? {
        return child?.let { reorderRecursive(it, targetId, direction) }
    }

    override fun visit(props: NodeProperties.ColumnProps) = props.copy(children = reorderList(props.children))
    override fun visit(props: NodeProperties.RowProps) = props.copy(children = reorderList(props.children))
    override fun visit(props: NodeProperties.BoxProps) = props.copy(children = reorderList(props.children))
    override fun visit(props: NodeProperties.NavigationBarProps) = props.copy(children = reorderList(props.children))
    override fun visit(props: NodeProperties.NavigationRailProps) = props.copy(
        header = reorderSingle(props.header),
        children = reorderList(props.children)
    )
    override fun visit(props: NodeProperties.TabRowProps) = props.copy(children = reorderList(props.children))
    override fun visit(props: NodeProperties.FlowRowProps) = props.copy(children = reorderList(props.children))
    override fun visit(props: NodeProperties.FlowColumnProps) = props.copy(children = reorderList(props.children))
    override fun visit(props: NodeProperties.SegmentedButtonRowProps) = props.copy(children = reorderList(props.children))
    override fun visit(props: NodeProperties.PagerProps) = props.copy(pages = reorderList(props.pages))
    
    override fun visit(props: NodeProperties.SearchBarProps) = props.copy(
        placeholder = reorderSingle(props.placeholder),
        leadingIcon = reorderSingle(props.leadingIcon),
        trailingIcon = reorderSingle(props.trailingIcon),
        children = reorderList(props.children)
    )
    
    override fun visit(props: NodeProperties.BottomBarProps) = props.copy(children = reorderList(props.children))

    override fun visit(props: NodeProperties.ButtonProps) = props.copy(child = reorderSingle(props.child))
    override fun visit(props: NodeProperties.CardProps) = props.copy(child = reorderSingle(props.child))
    override fun visit(props: NodeProperties.OutlinedCardProps) = props.copy(child = reorderSingle(props.child))
    override fun visit(props: NodeProperties.SurfaceProps) = props.copy(child = reorderSingle(props.child))
    override fun visit(props: NodeProperties.ModalBottomSheetProps) = props.copy(child = reorderSingle(props.child))
    
    override fun visit(props: NodeProperties.NavigationDrawerProps) = props.copy(
        drawerContent = reorderList(props.drawerContent),
        child = reorderSingle(props.child)
    )
    
    override fun visit(props: NodeProperties.ScaffoldProps) = props.copy(
        topBar = reorderSingle(props.topBar),
        bottomBar = reorderSingle(props.bottomBar),
        child = reorderSingle(props.child),
        floatingActionButton = reorderSingle(props.floatingActionButton)
    )
    
    override fun visit(props: NodeProperties.FabProps) = props.copy(icon = reorderSingle(props.icon))
    
    override fun visit(props: NodeProperties.BadgedBoxProps) = props.copy(
        badge = reorderSingle(props.badge),
        child = reorderSingle(props.child)
    )
    
    override fun visit(props: NodeProperties.AlertDialogProps) = props.copy(
        confirmButton = reorderSingle(props.confirmButton),
        dismissButton = reorderSingle(props.dismissButton),
        title = reorderSingle(props.title),
        text = reorderSingle(props.text),
        icon = reorderSingle(props.icon)
    )
    
    override fun visit(props: NodeProperties.TopAppBarProps) = props.copy(
        title = reorderSingle(props.title),
        navigationIcon = reorderSingle(props.navigationIcon),
        actions = reorderList(props.actions)
    )
    
    override fun visit(props: NodeProperties.ExtendedFabProps) = props.copy(
        icon = reorderSingle(props.icon),
        text = reorderSingle(props.text)
    )
    
    override fun visit(props: NodeProperties.TextFieldProps) = props.copy(
        placeholder = reorderSingle(props.placeholder),
        label = reorderSingle(props.label),
        leadingIcon = reorderSingle(props.leadingIcon),
        trailingIcon = reorderSingle(props.trailingIcon),
        supportingText = reorderSingle(props.supportingText),
        prefix = reorderSingle(props.prefix),
        suffix = reorderSingle(props.suffix)
    )
    
    override fun visit(props: NodeProperties.ListItemProps) = props.copy(
        headlineContent = reorderSingle(props.headlineContent),
        supportingContent = reorderSingle(props.supportingContent),
        overlineContent = reorderSingle(props.overlineContent),
        leadingContent = reorderSingle(props.leadingContent),
        trailingContent = reorderSingle(props.trailingContent)
    )
    
    override fun visit(props: NodeProperties.ChipProps) = props.copy(
        label = reorderSingle(props.label),
        leadingIcon = reorderSingle(props.leadingIcon)
    )
    
    override fun visit(props: NodeProperties.FilterChipProps) = props.copy(
        label = reorderSingle(props.label),
        leadingIcon = reorderSingle(props.leadingIcon)
    )
    
    override fun visit(props: NodeProperties.InputChipProps) = props.copy(
        label = reorderSingle(props.label),
        leadingIcon = reorderSingle(props.leadingIcon),
        trailingIcon = reorderSingle(props.trailingIcon)
    )
    
    override fun visit(props: NodeProperties.PlainTooltipProps) = props.copy(anchor = reorderSingle(props.anchor))
    
    override fun visit(props: NodeProperties.RichTooltipProps) = props.copy(
        anchor = reorderSingle(props.anchor),
        title = reorderSingle(props.title),
        text = reorderSingle(props.text),
        action = reorderSingle(props.action)
    )
    
    override fun visit(props: NodeProperties.NavigationBarItemProps) = props.copy(
        label = reorderSingle(props.label),
        icon = reorderSingle(props.icon)
    )
    
    override fun visit(props: NodeProperties.NavigationRailItemProps) = props.copy(
        label = reorderSingle(props.label),
        icon = reorderSingle(props.icon)
    )
    
    override fun visit(props: NodeProperties.NavigationDrawerItemProps) = props.copy(
        label = reorderSingle(props.label),
        icon = reorderSingle(props.icon),
        badge = reorderSingle(props.badge)
    )
    
    override fun visit(props: NodeProperties.TabProps) = props.copy(
        text = reorderSingle(props.text),
        icon = reorderSingle(props.icon)
    )
    
    override fun visit(props: NodeProperties.BottomNavigationItemProps) = props.copy(
        label = reorderSingle(props.label),
        icon = reorderSingle(props.icon)
    )
    
    override fun visit(props: NodeProperties.SegmentedButtonProps) = props.copy(
        label = reorderSingle(props.label),
        icon = reorderSingle(props.icon)
    )

    override fun visitDefault(props: NodeProperties) = props
}
