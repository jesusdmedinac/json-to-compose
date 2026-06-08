package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import com.jesusdmedinac.jsontocompose.model.ComposeNode

/**
 * Traverses the tree and inserts the [child] into the node with [parentId].
 */
fun ComposeNode.addNodeRecursive(parentId: String, child: ComposeNode): ComposeNode {
    if (this.id == parentId) {
        val childWithParent = child.copy(parent = this, id = ComposeNode.generateId(child.type, this))
        val visitor = AppendChildVisitor(childWithParent)
        val updatedProps = this.properties.accept(visitor)
        return this.copy(properties = updatedProps)
    }
    
    val visitor = UpdateChildrenVisitor(child) { node, newChild ->
        node.addNodeRecursive(parentId, newChild)
    }
    val updatedProps = this.properties.accept(visitor)
    return this.copy(properties = updatedProps)
}

/**
 * Traverses the tree and removes the node with [targetId].
 */
fun ComposeNode.deleteNodeRecursive(targetId: String): ComposeNode? {
    if (this.id == targetId) return null
    val visitor = DeleteChildVisitor(targetId) { node, id -> 
        node.deleteNodeRecursive(id) 
    }
    val updatedProps = this.properties.accept(visitor)
    return this.copy(properties = updatedProps)
}

/**
 * Traverses the tree and reorders the child with [targetId] in the specified [direction].
 */
fun ComposeNode.reorderNodeRecursive(targetId: String, direction: EditorIntent.ReorderNode.Direction): ComposeNode {
    val visitor = ReorderChildVisitor(targetId, direction) { node, id, dir ->
        node.reorderNodeRecursive(id, dir)
    }
    val updatedProps = this.properties.accept(visitor)
    return this.copy(properties = updatedProps)
}

/**
 * Recursively checks if a node with the given [targetId] exists in the tree.
 */
fun ComposeNode.nodeExists(targetId: String): Boolean {
    if (this.id == targetId) return true
    return this.children().any { it.nodeExists(targetId) }
}

/**
 * Traverses the tree and applies [transform] to the node with [targetId].
 */
fun ComposeNode.updateNodeRecursive(targetId: String, transform: (ComposeNode) -> ComposeNode): ComposeNode {
    if (this.id == targetId) return transform(this)
    val newProps = when (val props = this.properties) {
        is com.jesusdmedinac.jsontocompose.model.NodeProperties.ColumnProps -> props.copy(children = props.children?.map { it.updateNodeRecursive(targetId, transform) })
        is com.jesusdmedinac.jsontocompose.model.NodeProperties.RowProps -> props.copy(children = props.children?.map { it.updateNodeRecursive(targetId, transform) })
        is com.jesusdmedinac.jsontocompose.model.NodeProperties.BoxProps -> props.copy(children = props.children?.map { it.updateNodeRecursive(targetId, transform) })
        is com.jesusdmedinac.jsontocompose.model.NodeProperties.ButtonProps -> props.copy(child = props.child?.updateNodeRecursive(targetId, transform))
        is com.jesusdmedinac.jsontocompose.model.NodeProperties.ScaffoldProps -> props.copy(child = props.child?.updateNodeRecursive(targetId, transform))
        is com.jesusdmedinac.jsontocompose.model.NodeProperties.CardProps -> props.copy(child = props.child?.updateNodeRecursive(targetId, transform))
        is com.jesusdmedinac.jsontocompose.model.NodeProperties.OutlinedCardProps -> props.copy(child = props.child?.updateNodeRecursive(targetId, transform))
        is com.jesusdmedinac.jsontocompose.model.NodeProperties.TabRowProps -> props.copy(children = props.children?.map { it.updateNodeRecursive(targetId, transform) })
        is com.jesusdmedinac.jsontocompose.model.NodeProperties.TabProps -> props.copy(
            text = props.text?.updateNodeRecursive(targetId, transform),
            icon = props.icon?.updateNodeRecursive(targetId, transform)
        )
        is com.jesusdmedinac.jsontocompose.model.NodeProperties.BottomBarProps -> props.copy(children = props.children?.map { it.updateNodeRecursive(targetId, transform) })
        is com.jesusdmedinac.jsontocompose.model.NodeProperties.FlowRowProps -> props.copy(children = props.children?.map { it.updateNodeRecursive(targetId, transform) })
        is com.jesusdmedinac.jsontocompose.model.NodeProperties.FlowColumnProps -> props.copy(children = props.children?.map { it.updateNodeRecursive(targetId, transform) })
        is com.jesusdmedinac.jsontocompose.model.NodeProperties.SurfaceProps -> props.copy(child = props.child?.updateNodeRecursive(targetId, transform))
        is com.jesusdmedinac.jsontocompose.model.NodeProperties.FabProps -> props.copy(icon = props.icon?.updateNodeRecursive(targetId, transform))
        else -> props
    }
    return this.copy(properties = newProps)
}
