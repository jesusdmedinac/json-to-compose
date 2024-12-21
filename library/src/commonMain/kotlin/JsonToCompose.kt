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
        ComposeType.Layout.Column -> ToColumn(modifier)
        ComposeType.Layout.Row -> ToRow(modifier)
        ComposeType.Layout.Box -> ToBox(modifier)
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
    val child: ComposeNode? = null,
    val onClickEventName: String? = null,
    val children: List<ComposeNode>? = null,
)

@Serializable
sealed class ComposeType {
    @Serializable
    sealed class Layout : ComposeType() {
        data object Column : Layout()
        data object Row : Layout()
        data object Box : Layout()
    }
    data object Text : ComposeType()
    data object Button : ComposeType()
}

interface Behavior {
    fun onClick(eventName: String)
}