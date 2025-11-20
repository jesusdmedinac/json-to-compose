package com.jesusdmedinac.jsontocompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

// 1. Definimos un CompositionLocal para proveer el mapa de recursos
val LocalDrawableResources = staticCompositionLocalOf<Map<String, DrawableResource>> { emptyMap() }

@Composable
fun String.ToCompose(
    behavior: Behavior? = null,
) {
    Json.decodeFromString<ComposeNode>(this).ToCompose(
        behavior
    )
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
        ComposeType.Image -> ToImage()
        ComposeType.TextField -> ToTextField()
        ComposeType.LazyColumn -> ToLazyColumn()
        ComposeType.LazyRow -> ToLazyRow()
        ComposeType.Scaffold -> ToScaffold()
    }
}

@Composable
fun ComposeNode.ToColumn(
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
) {
    Text(
        text = text ?: "",
        modifier = Modifier from composeModifier,
    )
}

@Composable
fun ComposeNode.ToButton(
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

@Composable
fun ComposeNode.ToImage() {
    val props = properties as? NodeProperties.ImageProps ?: return
    val modifier = Modifier from composeModifier
    
    // 2. Obtenemos el mapa de recursos del entorno actual
    val drawableResources = LocalDrawableResources.current
    
    when {
        props.url != null -> {
            AsyncImage(
                model = props.url,
                contentDescription = props.contentDescription,
                modifier = modifier
            )
        }
        props.resourceName != null -> {
            // 3. Buscamos el recurso compilado usando el nombre (String)
            val resource = drawableResources[props.resourceName]
            
            if (resource != null) {
                Image(
                    painter = painterResource(resource),
                    contentDescription = props.contentDescription,
                    modifier = modifier
                )
            } else {
                // Fallback visual si el recurso no se encuentra en el mapa
                Box(modifier = modifier.background(Color.LightGray)) {
                    Text("Res not found: ${props.resourceName}", modifier = Modifier.padding(4.dp))
                }
            }
        }
        else -> {
            Box(modifier = modifier.background(Color.Magenta))
        }
    }
}

@Composable
fun ComposeNode.ToTextField() {
    // TODO: Implement TextField support
}

@Composable
fun ComposeNode.ToLazyColumn() {
    // TODO: Implement LazyColumn support
}

@Composable
fun ComposeNode.ToLazyRow() {
    // TODO: Implement LazyRow support
}

@Composable
fun ComposeNode.ToScaffold() {
    // TODO: Implement Scaffold support
}

@Serializable
data class ComposeNode(
    val type: ComposeType,
    val text: String? = null,
    val onClickEventName: String? = null,
    val properties: NodeProperties? = null,

    @Transient
    val parent: ComposeNode? = null,
    val child: ComposeNode? = null,
    val children: List<ComposeNode>? = null,
    val composeModifier: ComposeModifier = ComposeModifier(),
    @Transient
    val editMode: Boolean = true,
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

@Serializable
sealed interface NodeProperties {
    @Serializable
    data class ImageProps(
        val url: String? = null,
        val resourceName: String? = null,
        val contentDescription: String? = null,
        val contentScale: String = "Fit"
    ) : NodeProperties
}

enum class ComposeType {
    Column,
    Row,
    Box,
    Text,
    Button,
    Image,
    TextField,
    LazyColumn,
    LazyRow,
    Scaffold;

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
    val operations: List<Operation> = emptyList(),
) {
    @Serializable
    sealed class Operation(
        @Transient
        val modifierOperation: ModifierOperation? = null,
    ) {
        @Serializable
        data class Padding(val value: Int) : Operation(
            modifierOperation = ModifierOperation.Padding
        )

        @Serializable
        data object FillMaxSize : Operation(
            modifierOperation = ModifierOperation.FillMaxSize
        )

        @Serializable
        data object FillMaxWidth : Operation(
            modifierOperation = ModifierOperation.FillMaxWidth
        )

        @Serializable
        data object FillMaxHeight : Operation(
            modifierOperation = ModifierOperation.FillMaxHeight
        )

        @Serializable
        data class Width(val value: Int) : Operation(
            modifierOperation = ModifierOperation.Width
        )

        @Serializable
        data class Height(val value: Int) : Operation(
            modifierOperation = ModifierOperation.Height
        )

        @Serializable
        data class BackgroundColor(val hexColor: String) : Operation(
            modifierOperation = ModifierOperation.BackgroundColor
        )
    }
}

enum class ModifierOperation {
    Padding,
    FillMaxSize,
    FillMaxWidth,
    FillMaxHeight,
    Width,
    Height,
    BackgroundColor;
}

infix fun Modifier.from(composeModifier: ComposeModifier): Modifier {
    var result = this
    composeModifier.operations.forEach { operation ->
        result = when (operation) {
            is ComposeModifier.Operation.Padding -> result.padding(operation.value.dp)
            is ComposeModifier.Operation.FillMaxSize -> result.fillMaxSize()
            is ComposeModifier.Operation.FillMaxWidth -> result.fillMaxWidth()
            is ComposeModifier.Operation.FillMaxHeight -> result.fillMaxHeight()
            is ComposeModifier.Operation.Width -> result.width(operation.value.dp)
            is ComposeModifier.Operation.Height -> result.height(operation.value.dp)
            is ComposeModifier.Operation.BackgroundColor -> result.background(operation.hexColor.toColorInt())
        }
    }
    return result
}

fun String.toColorInt(): Color {
    if (!startsWith("#") || length != 9) {
        return Color.White
    }

    val colorLong = removePrefix("#").toLong(16)
    val alpha = (colorLong shr 24 and 0xFF).toInt()
    val red = (colorLong shr 16 and 0xFF).toInt()
    val green = (colorLong shr 8 and 0xFF).toInt()
    val blue = (colorLong and 0xFF).toInt()

    return Color(red, green, blue, alpha)
}

interface Behavior {
    fun onClick(eventName: String)
}
