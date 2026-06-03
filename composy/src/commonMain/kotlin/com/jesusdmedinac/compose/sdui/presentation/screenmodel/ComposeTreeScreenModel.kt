package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import cafe.adriel.voyager.core.model.ScreenModel
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ComposeTreeScreenModel : ScreenModel, ComposeTreeBehavior {
    private val _state = MutableStateFlow(ComposeTreeState())
    val state: StateFlow<ComposeTreeState> = _state.asStateFlow()

    override fun onAddNewNodeToChildren(composeNode: ComposeNode) {
        _state.update { state ->
            val updatedParent = addNodeToChildren(composeNode)
            val updatedRoot = updateNode(updatedParent)
            state.copy(
                composeNodeRoot = updatedRoot,
            )
        }
    }

    override fun onComposeNodeSelected(composeNode: ComposeNode?) {
        _state.update { state ->
            state.copy(
                selectedComposeNode = composeNode
            )
        }
    }

    override fun saveNode(composeNode: ComposeNode) {
        _state.update { state ->
            val updatedRoot = updateNode(composeNode)
            state.copy(
                composeNodeRoot = updatedRoot
            )
        }
    }

    override fun onNodeExpanded(composeNode: ComposeNode) {
        _state.update { state ->
            val updatedCollapsedNodes = if (state.collapsedNodes.contains(composeNode)) {
                state.collapsedNodes.filter { it.id != composeNode.id }
            } else {
                state.collapsedNodes.plus(composeNode)
            }
            state.copy(
                collapsedNodes = updatedCollapsedNodes
            )
        }
    }

    private fun addNodeToChildren(composeNode: ComposeNode): ComposeNode {
        val updatedNode = composeNode // Removed applyDefaultTextIfComposeTypeIsText
        val parentNode = updatedNode.parent ?: return updatedNode
        val updatedProperties = when (val props = parentNode.properties) {
            is NodeProperties.ColumnProps -> props.copy(children = (props.children ?: emptyList()) + updatedNode)
            is NodeProperties.RowProps -> props.copy(children = (props.children ?: emptyList()) + updatedNode)
            is NodeProperties.BoxProps -> props.copy(children = (props.children ?: emptyList()) + updatedNode)
            is NodeProperties.NavigationBarProps -> props.copy(children = (props.children ?: emptyList()) + updatedNode)
            is NodeProperties.NavigationRailProps -> props.copy(children = (props.children ?: emptyList()) + updatedNode)
            is NodeProperties.TabRowProps -> props.copy(children = (props.children ?: emptyList()) + updatedNode)
            is NodeProperties.FlowRowProps -> props.copy(children = (props.children ?: emptyList()) + updatedNode)
            is NodeProperties.FlowColumnProps -> props.copy(children = (props.children ?: emptyList()) + updatedNode)
            is NodeProperties.SegmentedButtonRowProps -> props.copy(children = (props.children ?: emptyList()) + updatedNode)
            is NodeProperties.PagerProps -> props.copy(pages = (props.pages ?: emptyList()) + updatedNode)
            is NodeProperties.SearchBarProps -> props.copy(children = (props.children ?: emptyList()) + updatedNode)
            is NodeProperties.BottomBarProps -> props.copy(children = (props.children ?: emptyList()) + updatedNode)
            is NodeProperties.ButtonProps -> props.copy(child = updatedNode)
            is NodeProperties.CardProps -> props.copy(child = updatedNode)
            is NodeProperties.OutlinedCardProps -> props.copy(child = updatedNode)
            is NodeProperties.SurfaceProps -> props.copy(child = updatedNode)
            is NodeProperties.ModalBottomSheetProps -> props.copy(child = updatedNode)
            is NodeProperties.ScaffoldProps -> props.copy(child = updatedNode)
            is NodeProperties.NavigationDrawerProps -> props.copy(child = updatedNode)
            is NodeProperties.FabProps -> props.copy(icon = updatedNode)
            is NodeProperties.BadgedBoxProps -> props.copy(child = updatedNode)
            else -> return updatedNode
        }
        return parentNode.copy(properties = updatedProperties)
    }

    private fun updateNode(updatedNode: ComposeNode): ComposeNode =
        updateNodeRecursive(_state.value.composeNodeRoot, updatedNode)

    private fun updateNodeRecursive(currentNode: ComposeNode, updatedNode: ComposeNode): ComposeNode {
        if (currentNode.id == updatedNode.id) return updatedNode
        val updatedProps = when (val props = currentNode.properties) {
            is NodeProperties.ColumnProps -> props.copy(children = props.children?.map { updateNodeRecursive(it, updatedNode) })
            is NodeProperties.RowProps -> props.copy(children = props.children?.map { updateNodeRecursive(it, updatedNode) })
            is NodeProperties.BoxProps -> props.copy(children = props.children?.map { updateNodeRecursive(it, updatedNode) })
            is NodeProperties.NavigationBarProps -> props.copy(children = props.children?.map { updateNodeRecursive(it, updatedNode) })
            is NodeProperties.NavigationRailProps -> props.copy(
                header = props.header?.let { updateNodeRecursive(it, updatedNode) },
                children = props.children?.map { updateNodeRecursive(it, updatedNode) }
            )
            is NodeProperties.TabRowProps -> props.copy(children = props.children?.map { updateNodeRecursive(it, updatedNode) })
            is NodeProperties.FlowRowProps -> props.copy(children = props.children?.map { updateNodeRecursive(it, updatedNode) })
            is NodeProperties.FlowColumnProps -> props.copy(children = props.children?.map { updateNodeRecursive(it, updatedNode) })
            is NodeProperties.SegmentedButtonRowProps -> props.copy(children = props.children?.map { updateNodeRecursive(it, updatedNode) })
            is NodeProperties.PagerProps -> props.copy(pages = props.pages?.map { updateNodeRecursive(it, updatedNode) })
            is NodeProperties.SearchBarProps -> props.copy(
                placeholder = props.placeholder?.let { updateNodeRecursive(it, updatedNode) },
                leadingIcon = props.leadingIcon?.let { updateNodeRecursive(it, updatedNode) },
                trailingIcon = props.trailingIcon?.let { updateNodeRecursive(it, updatedNode) },
                children = props.children?.map { updateNodeRecursive(it, updatedNode) }
            )
            is NodeProperties.BottomBarProps -> props.copy(children = props.children?.map { updateNodeRecursive(it, updatedNode) })
            is NodeProperties.ButtonProps -> props.copy(child = props.child?.let { updateNodeRecursive(it, updatedNode) })
            is NodeProperties.CardProps -> props.copy(child = props.child?.let { updateNodeRecursive(it, updatedNode) })
            is NodeProperties.OutlinedCardProps -> props.copy(child = props.child?.let { updateNodeRecursive(it, updatedNode) })
            is NodeProperties.SurfaceProps -> props.copy(child = props.child?.let { updateNodeRecursive(it, updatedNode) })
            is NodeProperties.ModalBottomSheetProps -> props.copy(child = props.child?.let { updateNodeRecursive(it, updatedNode) })
            is NodeProperties.NavigationDrawerProps -> props.copy(
                drawerContent = props.drawerContent?.map { updateNodeRecursive(it, updatedNode) },
                child = props.child?.let { updateNodeRecursive(it, updatedNode) }
            )
            is NodeProperties.ScaffoldProps -> props.copy(
                topBar = props.topBar?.let { updateNodeRecursive(it, updatedNode) },
                bottomBar = props.bottomBar?.let { updateNodeRecursive(it, updatedNode) },
                child = props.child?.let { updateNodeRecursive(it, updatedNode) },
                floatingActionButton = props.floatingActionButton?.let { updateNodeRecursive(it, updatedNode) }
            )
            is NodeProperties.FabProps -> props.copy(icon = props.icon?.let { updateNodeRecursive(it, updatedNode) })
            is NodeProperties.BadgedBoxProps -> props.copy(
                badge = props.badge?.let { updateNodeRecursive(it, updatedNode) },
                child = props.child?.let { updateNodeRecursive(it, updatedNode) }
            )
            is NodeProperties.AlertDialogProps -> props.copy(
                confirmButton = props.confirmButton?.let { updateNodeRecursive(it, updatedNode) },
                dismissButton = props.dismissButton?.let { updateNodeRecursive(it, updatedNode) },
                title = props.title?.let { updateNodeRecursive(it, updatedNode) },
                text = props.text?.let { updateNodeRecursive(it, updatedNode) },
                icon = props.icon?.let { updateNodeRecursive(it, updatedNode) }
            )
            is NodeProperties.TopAppBarProps -> props.copy(
                title = props.title?.let { updateNodeRecursive(it, updatedNode) },
                navigationIcon = props.navigationIcon?.let { updateNodeRecursive(it, updatedNode) },
                actions = props.actions?.map { updateNodeRecursive(it, updatedNode) }
            )
            is NodeProperties.ExtendedFabProps -> props.copy(
                icon = props.icon?.let { updateNodeRecursive(it, updatedNode) },
                text = props.text?.let { updateNodeRecursive(it, updatedNode) }
            )
            is NodeProperties.TextFieldProps -> props.copy(
                placeholder = props.placeholder?.let { updateNodeRecursive(it, updatedNode) },
                label = props.label?.let { updateNodeRecursive(it, updatedNode) },
                leadingIcon = props.leadingIcon?.let { updateNodeRecursive(it, updatedNode) },
                trailingIcon = props.trailingIcon?.let { updateNodeRecursive(it, updatedNode) },
                supportingText = props.supportingText?.let { updateNodeRecursive(it, updatedNode) },
                prefix = props.prefix?.let { updateNodeRecursive(it, updatedNode) },
                suffix = props.suffix?.let { updateNodeRecursive(it, updatedNode) }
            )
            is NodeProperties.ListItemProps -> props.copy(
                headlineContent = props.headlineContent?.let { updateNodeRecursive(it, updatedNode) },
                supportingContent = props.supportingContent?.let { updateNodeRecursive(it, updatedNode) },
                overlineContent = props.overlineContent?.let { updateNodeRecursive(it, updatedNode) },
                leadingContent = props.leadingContent?.let { updateNodeRecursive(it, updatedNode) },
                trailingContent = props.trailingContent?.let { updateNodeRecursive(it, updatedNode) }
            )
            is NodeProperties.ChipProps -> props.copy(
                label = props.label?.let { updateNodeRecursive(it, updatedNode) },
                leadingIcon = props.leadingIcon?.let { updateNodeRecursive(it, updatedNode) }
            )
            is NodeProperties.FilterChipProps -> props.copy(
                label = props.label?.let { updateNodeRecursive(it, updatedNode) },
                leadingIcon = props.leadingIcon?.let { updateNodeRecursive(it, updatedNode) }
            )
            is NodeProperties.InputChipProps -> props.copy(
                label = props.label?.let { updateNodeRecursive(it, updatedNode) },
                leadingIcon = props.leadingIcon?.let { updateNodeRecursive(it, updatedNode) },
                trailingIcon = props.trailingIcon?.let { updateNodeRecursive(it, updatedNode) }
            )
            is NodeProperties.PlainTooltipProps -> props.copy(
                anchor = props.anchor?.let { updateNodeRecursive(it, updatedNode) }
            )
            is NodeProperties.RichTooltipProps -> props.copy(
                anchor = props.anchor?.let { updateNodeRecursive(it, updatedNode) },
                title = props.title?.let { updateNodeRecursive(it, updatedNode) },
                text = props.text?.let { updateNodeRecursive(it, updatedNode) },
                action = props.action?.let { updateNodeRecursive(it, updatedNode) }
            )
            is NodeProperties.NavigationBarItemProps -> props.copy(
                label = props.label?.let { updateNodeRecursive(it, updatedNode) },
                icon = props.icon?.let { updateNodeRecursive(it, updatedNode) }
            )
            is NodeProperties.NavigationRailItemProps -> props.copy(
                label = props.label?.let { updateNodeRecursive(it, updatedNode) },
                icon = props.icon?.let { updateNodeRecursive(it, updatedNode) }
            )
            is NodeProperties.NavigationDrawerItemProps -> props.copy(
                label = props.label?.let { updateNodeRecursive(it, updatedNode) },
                icon = props.icon?.let { updateNodeRecursive(it, updatedNode) },
                badge = props.badge?.let { updateNodeRecursive(it, updatedNode) }
            )
            is NodeProperties.TabProps -> props.copy(
                text = props.text?.let { updateNodeRecursive(it, updatedNode) },
                icon = props.icon?.let { updateNodeRecursive(it, updatedNode) }
            )
            is NodeProperties.BottomNavigationItemProps -> props.copy(
                label = props.label?.let { updateNodeRecursive(it, updatedNode) },
                icon = props.icon?.let { updateNodeRecursive(it, updatedNode) }
            )
            is NodeProperties.SegmentedButtonProps -> props.copy(
                label = props.label?.let { updateNodeRecursive(it, updatedNode) },
                icon = props.icon?.let { updateNodeRecursive(it, updatedNode) }
            )
            is NodeProperties.BadgeProps -> props
            else -> return currentNode
        }
        return currentNode.copy(properties = updatedProps)
    }
}

data class ComposeTreeState(
    val composeNodeRoot: ComposeNode = ComposeNode(
        type = ComposeType.Box,
    ),
    val selectedComposeNode: ComposeNode? = null,
    val collapsedNodes: List<ComposeNode> = emptyList(),
) {
    fun isSelected(composeNode: ComposeNode): Boolean {
        return selectedComposeNode?.id == composeNode.id
    }
    fun isParentExpanded(composeNode: ComposeNode): Boolean {
        val parents = composeNode.parents()
        return parents.none { parent -> collapsedNodes.any { it.id == parent.id } }
    }
}

interface ComposeTreeBehavior {
    fun onAddNewNodeToChildren(composeNode: ComposeNode)
    fun onComposeNodeSelected(composeNode: ComposeNode?)
    fun saveNode(composeNode: ComposeNode)
    fun onNodeExpanded(composeNode: ComposeNode)

    companion object {
        val Default = object : ComposeTreeBehavior {
            override fun onAddNewNodeToChildren(composeNode: ComposeNode) {
                TODO("onAddNewNodeToChildren is not implemented")
            }

            override fun onComposeNodeSelected(composeNode: ComposeNode?) {
                TODO("onComposeNodeSelected is not implemented")
            }

            override fun saveNode(composeNode: ComposeNode) {
                TODO("saveNode is not yet implemented")
            }

            override fun onNodeExpanded(composeNode: ComposeNode) {
                TODO("onNodeExpanded is not implemented")
            }
        }
    }
}