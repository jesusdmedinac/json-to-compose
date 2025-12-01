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
            val parentProps = parent.properties as? NodeProperties.LayoutProps
            val children = parentProps?.children ?: emptyList()

            parent.id + "_" + type.name + "_" + (children.size + 1)
        }
    }

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
        val singleChildProps = properties as? NodeProperties.ButtonProps
        val child = singleChildProps?.child
        child?.let { list.add(it) }
        val layoutProps = properties as? NodeProperties.LayoutProps
        val children = layoutProps?.children
        children?.forEach {
            list.addAll(it.asList())
        }
        return list
    }

    override fun toString(): String = Json.encodeToString(this)
}
