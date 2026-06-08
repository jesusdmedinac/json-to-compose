package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import com.jesusdmedinac.jsontocompose.model.ComposeNode

/**
 * Traverses the tree and inserts the [child] into the node with [parentId].
 */
fun ComposeNode.addNodeRecursive(parentId: String, child: ComposeNode): ComposeNode {
    if (this.id == parentId) {
        val visitor = AppendChildVisitor(child)
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
