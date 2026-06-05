package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties

enum class ReorderDirection { UP, DOWN }

class ReorderChildVisitor(
    private val targetId: String,
    private val direction: ReorderDirection
) : NodePropertiesVisitor<NodeProperties> {

    private fun reorder(list: List<ComposeNode>?): List<ComposeNode>? {
        if (list == null) return null
        val index = list.indexOfFirst { it.id == targetId }
        if (index == -1) return list

        val mutableList = list.toMutableList()
        when (direction) {
            ReorderDirection.UP -> {
                if (index > 0) {
                    val temp = mutableList[index - 1]
                    mutableList[index - 1] = mutableList[index]
                    mutableList[index] = temp
                }
            }
            ReorderDirection.DOWN -> {
                if (index < mutableList.size - 1) {
                    val temp = mutableList[index + 1]
                    mutableList[index + 1] = mutableList[index]
                    mutableList[index] = temp
                }
            }
        }
        return mutableList.toList()
    }

    override fun visit(props: NodeProperties.ColumnProps) = props.copy(children = reorder(props.children))
    override fun visit(props: NodeProperties.RowProps) = props.copy(children = reorder(props.children))
    override fun visit(props: NodeProperties.BoxProps) = props.copy(children = reorder(props.children))
    override fun visit(props: NodeProperties.NavigationBarProps) = props.copy(children = reorder(props.children))
    override fun visit(props: NodeProperties.NavigationRailProps) = props.copy(children = reorder(props.children))
    override fun visit(props: NodeProperties.TabRowProps) = props.copy(children = reorder(props.children))
    override fun visit(props: NodeProperties.FlowRowProps) = props.copy(children = reorder(props.children))
    override fun visit(props: NodeProperties.FlowColumnProps) = props.copy(children = reorder(props.children))
    override fun visit(props: NodeProperties.SegmentedButtonRowProps) = props.copy(children = reorder(props.children))
    override fun visit(props: NodeProperties.PagerProps) = props.copy(pages = reorder(props.pages))
    override fun visit(props: NodeProperties.SearchBarProps) = props.copy(children = reorder(props.children))
    override fun visit(props: NodeProperties.BottomBarProps) = props.copy(children = reorder(props.children))
    override fun visit(props: NodeProperties.NavigationDrawerProps) = props.copy(drawerContent = reorder(props.drawerContent))
    override fun visit(props: NodeProperties.TopAppBarProps) = props.copy(actions = reorder(props.actions))

    override fun visitDefault(props: NodeProperties) = props
}
