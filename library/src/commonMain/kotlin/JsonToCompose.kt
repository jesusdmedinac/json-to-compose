package com.jesusdmedinac.jsontocompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json

@Composable
fun String.ToCompose(
    composeModifier: ComposeModifier = ComposeModifier(),
    behavior: Behavior? = null,
) {
    Json.decodeFromString<ComposeNode>(this).ToCompose(
        composeModifier,
        behavior
    )
}

@Composable
fun ComposeNode.ToCompose(
    composeModifier: ComposeModifier = ComposeModifier(),
    behavior: Behavior? = null
) {
    when (type) {
        ComposeType.Column -> ToColumn(composeModifier)
        ComposeType.Row -> ToRow(composeModifier)
        ComposeType.Box -> ToBox(composeModifier)
        ComposeType.Text -> ToText(composeModifier)
        ComposeType.Button -> ToButton(composeModifier, behavior)
    }
}

@Composable
fun ComposeNode.ToColumn(
    composeModifier: ComposeModifier = ComposeModifier(),
) {
    Column(
        modifier = Modifier from composeModifier,
    ) {
        children?.forEach {
            it.ToCompose()
        }
    }
}

@Composable
fun ComposeNode.ToRow(
    composeModifier: ComposeModifier = ComposeModifier(),
) {
    Row(
        modifier = Modifier from composeModifier,
    ) {
        children?.forEach {
            it.ToCompose()
        }
    }
}

@Composable
fun ComposeNode.ToBox(
    composeModifier: ComposeModifier = ComposeModifier(),
) {
    Box(
        modifier = Modifier from composeModifier,
    ) {
        children?.forEach {
            it.ToCompose()
        }
    }
}

@Composable
fun ComposeNode.ToText(
    composeModifier: ComposeModifier = ComposeModifier(),
) {
    Text(
        text = text ?: "",
        modifier = Modifier from composeModifier,
    )
}

@Composable
fun ComposeNode.ToButton(
    composeModifier: ComposeModifier = ComposeModifier(),
    behavior: Behavior? = null,
) {
    Button(
        onClick = {
            behavior?.onClick(onClickEventName ?: "")
        },
        modifier = Modifier from composeModifier,
    ) {
        child?.ToCompose()
    }
}

@Serializable
data class ComposeNode(
    val type: ComposeType,
    val text: String? = null,
    @Transient
    val parent: ComposeNode? = null,
    val child: ComposeNode? = null,
    val onClickEventName: String? = null,
    val children: List<ComposeNode>? = null,
    val composeModifier: ComposeModifier = ComposeModifier(),
) {
    val id: String = when {
        parent == null -> "${countLevels()}"
        else -> parent.id + "_" + type.name + "_" + ((parent.children ?: emptyList()).size + 1)
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
    Button;

    fun isLayout(): Boolean = when (this) {
        Column, Row, Box -> true
        else -> false
    }

    fun hasChild(): Boolean = when (this) {
        Button -> true
        else -> false
    }
}

@Serializable
data class ComposeModifier(
    val operations: List<Operation> = emptyList()
) {
    fun then(operation: Operation): ComposeModifier =
        copy(operations = operations + operation)

    @Serializable
    sealed class Operation {
        @Serializable
        data class Padding(val value: Int) : Operation()

        @Serializable
        data class Margin(val value: Int) : Operation()

        @Serializable
        data class Width(val value: Int) : Operation()

        @Serializable
        data class Height(val value: Int) : Operation()

        @Serializable
        data class BackgroundColor(val color: Int) : Operation()
    }
}

infix fun Modifier.from(composeModifier: ComposeModifier): Modifier {
    var result = this
    composeModifier.operations.forEach { operation ->
        result = when (operation) {
            is ComposeModifier.Operation.Padding -> result.padding(operation.value.dp)
            is ComposeModifier.Operation.Margin -> result.padding(operation.value.dp) // Usar padding como margen
            is ComposeModifier.Operation.Width -> result.width(operation.value.dp)
            is ComposeModifier.Operation.Height -> result.height(operation.value.dp)
            is ComposeModifier.Operation.BackgroundColor -> result.background(Color(operation.color))
        }
    }
    return result
}

interface Behavior {
    fun onClick(eventName: String)
}