package io.github.kotlin.fibonacci

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Composable
fun String.ToCompose(behavior: Behavior? = null) {
    Json.decodeFromString<ComposeNode>(this).ToCompose(behavior)
}

@Composable
fun ComposeNode.ToCompose(
    behavior: Behavior? = null
) {
    when (type) {
        ComposeType.Column -> ToColumn()
        ComposeType.Row -> ToRow()
        ComposeType.Box -> ToBox()
        ComposeType.Text -> ToText()
        ComposeType.Button -> ToButton(behavior)
    }
}

@Composable
fun ComposeNode.ToColumn() {
    Column {
        children?.forEach {
            it.ToCompose()
        }
    }
}

@Composable
fun ComposeNode.ToRow() {
    Row {
        children?.forEach {
            it.ToCompose()
        }
    }
}

@Composable
fun ComposeNode.ToBox() {
    Box {
        children?.forEach {
            it.ToCompose()
        }
    }
}

@Composable
fun ComposeNode.ToText() {
    Text(text = text ?: "")
}

@Composable
fun ComposeNode.ToButton(behavior: Behavior? = null) {
    Button(onClick = {
        behavior?.onClick(onClickEventName ?: "")
    }) {
        child?.ToCompose()
    }
}

@Serializable
data class ComposeNode(
    val type: ComposeType,
    val text: String? = null,
    val child: ComposeNode? = null,
    val onClickEventName: String? = null,
    val children: List<ComposeNode>? = null,
)

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