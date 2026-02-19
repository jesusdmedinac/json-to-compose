package com.jesusdmedinac.jsontocompose.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json

/**
 * Represents a single node in the server-driven UI tree.
 *
 * Each node has a [type] that determines which Compose component is rendered,
 * optional [properties] that configure the component, and an optional [composeModifier]
 * that applies layout and styling modifiers.
 *
 * Nodes form a tree structure: container types (Column, Row, Box, etc.) hold children
 * inside their [properties], while leaf types (Text, Image, etc.) have no children.
 *
 * @property type The component type that determines which Compose widget is rendered.
 * @property properties Type-specific configuration for the component. Must match the expected
 *   [NodeProperties] subclass for the given [type] (e.g., [NodeProperties.TextProps] for [ComposeType.Text]).
 * @property parent Reference to the parent node in the tree. Transient — not serialized.
 * @property composeModifier Modifiers applied to this node (padding, size, background, etc.).
 * @property editMode Whether this node is in edit mode. Transient — not serialized.
 */
@Serializable
data class ComposeNode(
    val type: ComposeType,
    val properties: NodeProperties? = null,

    @Transient
    val parent: ComposeNode? = null,
    val composeModifier: ComposeModifier = ComposeModifier(),
    @Transient
    val editMode: Boolean = true,
) {
    /**
     * Auto-generated identifier based on the node's position in the tree.
     * Root nodes use their depth level; child nodes combine the parent ID,
     * type name, and sibling index.
     */
    val id: String = when {
        parent == null -> "${countLevels()}"
        else -> {
            parent.id + "_" + type.name + "_" + (properties.children().size + 1)
        }
    }

    private fun NodeProperties?.children(): List<ComposeNode> = when(this) {
        is NodeProperties.ColumnProps -> children
        is NodeProperties.RowProps -> children
        is NodeProperties.BoxProps -> children
        else -> null
    } ?: emptyList()

    /**
     * Returns the depth of this node from the root (0 for root nodes).
     *
     * @param count Accumulator used during recursion. Callers should use the default value.
     */
    fun countLevels(count: Int = 0): Int =
        parent?.countLevels(count + 1) ?: count

    /**
     * Returns the list of ancestor nodes from the immediate parent up to the root.
     */
    fun parents(): List<ComposeNode> {
        val list = mutableListOf<ComposeNode>()
        parent?.let {
            list.add(it)
            list.addAll(it.parents())
        }
        return list
    }

    /**
     * Flattens this node and all its descendants into a single list (pre-order traversal).
     *
     * Includes children from container properties (Column, Row, Box) and single-child
     * properties (Button, Card).
     */
    fun asList(): List<ComposeNode> {
        val list = mutableListOf<ComposeNode>()
        list.add(this)
        val child = when (properties) {
            is NodeProperties.ButtonProps -> properties.child
            is NodeProperties.CardProps -> properties.child
            is NodeProperties.AlertDialogProps -> null
            else -> null
        }
        child?.let { list.add(it) }
        val children = properties.children()
        children.forEach {
            list.addAll(it.asList())
        }
        return list
    }

    /** Returns the JSON representation of this node using kotlinx.serialization. */
    override fun toString(): String = Json.encodeToString(this)
}
