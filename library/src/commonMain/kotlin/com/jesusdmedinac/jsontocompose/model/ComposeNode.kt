package com.jesusdmedinac.jsontocompose.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json

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

    fun countLevels(count: Int = 0): Int =
        parent?.countLevels(count + 1) ?: count

    fun parents(): List<ComposeNode> {
        val list = mutableListOf<ComposeNode>()
        parent?.let {
            list.add(it)
            list.addAll(it.parents())
        }
        return list
    }

    fun asList(): List<ComposeNode> {
        val list = mutableListOf<ComposeNode>()
        list.add(this)
        val child = when (properties) {
            is NodeProperties.ButtonProps -> properties.child
            is NodeProperties.CardProps -> properties.child
            else -> null
        }
        child?.let { list.add(it) }
        val children = properties.children()
        children.forEach {
            list.addAll(it.asList())
        }
        return list
    }

    override fun toString(): String = Json.encodeToString(this)
}
