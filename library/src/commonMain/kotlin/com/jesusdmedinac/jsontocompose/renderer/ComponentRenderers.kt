package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.contentColorFor
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.LocalCustomRenderers
import com.jesusdmedinac.jsontocompose.LocalDrawableResources
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.renderer.toAlignment
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.renderer.toHorizontalArrangement
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.renderer.toHorizontalsAlignment
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.renderer.toVerticalAlignment
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.renderer.toVerticalArrangement
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state.StateHost
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import org.jetbrains.compose.resources.painterResource
import kotlin.collections.get

@Composable
fun ComposeNode.ToColumn() {
    val props = properties as? NodeProperties.ColumnProps ?: return
    val children = props.children
    val verticalArrangement = props.verticalArrangement
        ?.toVerticalArrangement()
        ?: Arrangement.Top
    val horizontalAlignment = props.horizontalAlignment
        ?.toHorizontalsAlignment()
        ?: Alignment.Start
    val modifier = (Modifier from composeModifier).testTag(type.name)
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
    ) {
        children?.forEach {
            it.ToCompose()
        }
    }
}

@Composable
fun ComposeNode.ToRow() {
    val props = properties as? NodeProperties.RowProps ?: return
    val children = props.children
    val horizontalArrangement = props.horizontalArrangement
        ?.toHorizontalArrangement()
        ?: Arrangement.Start
    val verticalAlignment = props.verticalAlignment
        ?.toVerticalAlignment()
        ?: Alignment.Top
    val modifier = (Modifier from composeModifier).testTag(type.name)
    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
    ) {
        children?.forEach {
            it.ToCompose()
        }
    }
}

@Composable
fun ComposeNode.ToBox() {
    val props = properties as? NodeProperties.BoxProps ?: return
    val children = props.children
    val contentAlignment = props.contentAlignment
        ?.toAlignment()
        ?: Alignment.TopStart
    val propagateMinConstraints = props.propagateMinConstraints ?: false
    val modifier = (Modifier from composeModifier).testTag(type.name)
    Box(
        modifier = modifier,
        contentAlignment = contentAlignment,
        propagateMinConstraints = propagateMinConstraints,
    ) {
        children?.forEach {
            it.ToCompose()
        }
    }
}

@Composable
fun ComposeNode.ToText() {
    val props = properties as? NodeProperties.TextProps ?: return
    val text = props.text
    val modifier = (Modifier from composeModifier).testTag(type.name)
    Text(
        text = text ?: "",
        modifier = modifier,
    )
}

@Composable
fun ComposeNode.ToButton() {
    val props = properties as? NodeProperties.ButtonProps ?: return
    val child = props.child
    val onClickEventName = props.onClickEventName
    val currentBehavior = LocalBehavior.current
    val behavior = currentBehavior[onClickEventName]
    Button(
        onClick = {
            behavior?.invoke()
        },
        modifier = (Modifier from composeModifier).testTag(type.name),
    ) {
        child?.ToCompose()
    }
}

@Composable
fun ComposeNode.ToImage() {
    val props = properties as? NodeProperties.ImageProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)

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
            val resource = drawableResources[props.resourceName]

            if (resource != null) {
                Image(
                    painter = painterResource(resource),
                    contentDescription = props.contentDescription,
                    modifier = modifier
                )
            } else {
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
    val props = properties as? NodeProperties.TextFieldProps ?: return
    val valueStateHostName = props.valueStateHostName
    val modifier = (Modifier from composeModifier).testTag(type.name)

    if (valueStateHostName == null) {
        println("Warning: TextField node has no valueStateHostName. The component will not render.")
        return
    }

    val currentStateHost = LocalStateHost.current
    val rawStateHost = currentStateHost[valueStateHostName]

    if (rawStateHost == null) {
        println("Warning: No StateHost registered with name \"$valueStateHostName\". Expected StateHost<String>.")
        return
    }

    @Suppress("UNCHECKED_CAST")
    val stateHost = rawStateHost as? StateHost<String>
    if (stateHost == null) {
        println("Warning: StateHost \"$valueStateHostName\" is not of type StateHost<String>. Check the registered type.")
        return
    }

    val value = try {
        stateHost.state as? String
    } catch (e: ClassCastException) {
        println("Warning: StateHost \"$valueStateHostName\" is not of type StateHost<String>. Check the registered type.")
        null
    } ?: return

    TextField(
        value = value,
        onValueChange = { newValue ->
            stateHost.onStateChange(newValue)
        },
        modifier = modifier
    )
}

@Composable
fun ComposeNode.ToLazyColumn() {
    val props = properties as? NodeProperties.ColumnProps ?: return
    val children = props.children
    val verticalArrangement = props.verticalArrangement
        ?.toVerticalArrangement()
        ?: Arrangement.Top
    val horizontalAlignment = props.horizontalAlignment
        ?.toHorizontalsAlignment()
        ?: Alignment.Start
    val modifier = (Modifier from composeModifier).testTag(type.name)
    LazyColumn(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
    ) {
        children?.let { items(it) { child -> child.ToCompose() } }
    }
}

@Composable
fun ComposeNode.ToLazyRow() {
    val props = properties as? NodeProperties.RowProps ?: return
    val children = props.children
    val horizontalArrangement = props.horizontalArrangement
        ?.toHorizontalArrangement()
        ?: Arrangement.Start
    val verticalAlignment = props.verticalAlignment
        ?.toVerticalAlignment()
        ?: Alignment.Top
    val modifier = (Modifier from composeModifier).testTag(type.name)
    LazyRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
    ) {
        children?.let { items(it) { child -> child.ToCompose() } }
    }
}

@Composable
fun ComposeNode.ToScaffold() {
    val props = properties as? NodeProperties.ScaffoldProps ?: return
    val child = props.child
    val modifier = (Modifier from composeModifier).testTag(type.name)
    Scaffold(
        modifier = modifier,
    ) {
        child?.ToCompose()
    }
}

@Composable
fun ComposeNode.ToCard() {
    val props = properties as? NodeProperties.CardProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val elevation = (props.elevation ?: 1).dp
    val shape = RoundedCornerShape((props.cornerRadius ?: 0).dp)
    Card(
        modifier = modifier,
        elevation = elevation,
        shape = shape,
    ) {
        props.child?.ToCompose()
    }
}

@Composable
fun ComposeNode.ToAlertDialog() {
    val props = properties as? NodeProperties.AlertDialogProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val currentStateHost = LocalStateHost.current
    val visibilityStateHost = props.visibilityStateHostName?.let { name ->
        val rawStateHost = currentStateHost[name]
        if (rawStateHost == null) {
            println("Warning: No StateHost registered with name \"$name\". Expected StateHost<Boolean>. Dialog will default to visible.")
            return@let null
        }
        @Suppress("UNCHECKED_CAST")
        val typed = rawStateHost as? StateHost<Boolean>
        if (typed == null) {
            println("Warning: StateHost \"$name\" is not of type StateHost<Boolean>. Check the registered type. Dialog will default to visible.")
            return@let null
        }
        // Verify actual type at runtime (type erasure makes the cast above always succeed)
        try {
            typed.state as? Boolean ?: run {
                println("Warning: StateHost \"$name\" is not of type StateHost<Boolean>. Check the registered type. Dialog will default to visible.")
                return@let null
            }
            typed
        } catch (e: ClassCastException) {
            println("Warning: StateHost \"$name\" is not of type StateHost<Boolean>. Check the registered type. Dialog will default to visible.")
            null
        }
    }

    val isVisible = visibilityStateHost?.state ?: true
    if (!isVisible) return

    val onClickEventName = props.onDismissRequestEventName
    val currentBehavior = LocalBehavior.current
    val behavior = currentBehavior[onClickEventName]

    val backgroundColor = props.backgroundColor?.let { Color(it) }
        ?: MaterialTheme.colors.surface
    val contentColor = props.contentColor?.let { Color(it) }
        ?: contentColorFor(backgroundColor)

    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            visibilityStateHost?.onStateChange(false)
            behavior?.invoke()
        },
        title = {
            props.title?.ToCompose()
        },
        text = {
            props.text?.ToCompose()
        },
        confirmButton = {
            props.confirmButton?.ToCompose()
        },
        dismissButton = {
            props.dismissButton?.ToCompose()
        },
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        // TODO: Support DialogProperties
        properties = DialogProperties(),
        // TODO: Support Shape
        shape = MaterialTheme.shapes.medium,
    )
}

@Composable
fun ComposeNode.ToTopAppBar() {
    val props = properties as? NodeProperties.TopAppBarProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val backgroundColor = props.backgroundColor?.let { Color(it) }
        ?: MaterialTheme.colors.primarySurface
    val contentColor = props.contentColor?.let { Color(it) }
        ?: contentColorFor(backgroundColor)
    TopAppBar(
        modifier = modifier,
        title = { props.title?.ToCompose() },
        navigationIcon = props.navigationIcon?.let { nav -> { nav.ToCompose() } },
        actions = {
            props.actions?.forEach { action -> action.ToCompose() }
        },
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        // TODO: Support elevation
        elevation = AppBarDefaults.TopAppBarElevation
    )
}

@Composable
fun ComposeNode.ToCustom() {
    val customRenderers = LocalCustomRenderers.current
    val customProps = properties as? NodeProperties.CustomProps
    customProps?.customType?.let { customType ->
        customRenderers[customType]?.invoke(this)
    }
}