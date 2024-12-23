package io.github.kotlin.fibonacci

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Composable
fun String.ToCompose(
    modifier: Modifier = Modifier,
    behavior: Behavior? = null,
) {
    Json.decodeFromString<ComposeNode>(this).ToCompose(
        modifier,
        behavior
    )
}

@Composable
fun ComposeNode.ToCompose(
    modifier: Modifier = Modifier,
    behavior: Behavior? = null
) {
    when (type) {
        ComposeType.Column -> ToColumn(modifier)
        ComposeType.Row -> ToRow(modifier)
        ComposeType.Box -> ToBox(modifier)
        ComposeType.Text -> ToText(modifier)
        ComposeType.Button -> ToButton(modifier, behavior)
    }
}

@Composable
fun ComposeNode.ToColumn(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        children?.forEach {
            it.ToCompose()
        }
    }
}

@Composable
fun ComposeNode.ToRow(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
    ) {
        children?.forEach {
            it.ToCompose()
        }
    }
}

@Composable
fun ComposeNode.ToBox(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        children?.forEach {
            it.ToCompose()
        }
    }
}

@Composable
fun ComposeNode.ToText(
    modifier: Modifier = Modifier,
) {
    Text(
        text = text ?: "",
        modifier = modifier,
    )
}

@Composable
fun ComposeNode.ToButton(
    modifier: Modifier = Modifier,
    behavior: Behavior? = null,
) {
    Button(
        onClick = {
            behavior?.onClick(onClickEventName ?: "")
        },
        modifier = modifier,
    ) {
        child?.ToCompose()
    }
}

@Serializable
data class ComposeNode(
    val type: ComposeType,
    val text: String? = null,
    val parent: ComposeNode? = null,
    val child: ComposeNode? = null,
    val onClickEventName: String? = null,
    val children: List<ComposeNode>? = null,
) {
    val id: String = when {
        parent == null -> type.name
        else -> parent.id + "_" + type.name + "_" + ((parent.children ?: emptyList()).size + 1)
    }

    fun countLevels(count: Int = 0): Int =
        parent?.countLevels(count + 1) ?: count

    fun asList(): List<ComposeNode> {
        val list = mutableListOf<ComposeNode>()
        list.add(this)
        child?.let { list.add(it) }
        children?.forEach {
            list.addAll(it.asList())
        }
        return list
    }
}


enum class ComposeType {
    Column,
    Row,
    Box,
    Text,
    Button
}

interface Behavior {
    fun onClick(eventName: String)
}