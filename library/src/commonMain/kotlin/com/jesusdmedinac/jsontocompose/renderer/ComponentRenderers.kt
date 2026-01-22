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
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
    val modifier = Modifier from composeModifier
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
    val modifier = Modifier from composeModifier
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
    val modifier = Modifier from composeModifier
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
    val modifier = Modifier from composeModifier
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
            behavior?.onClick()
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
    val onTextChangeEventName = props.onTextChangeEventName
    val modifier = Modifier from composeModifier

    val currentStateHost = LocalStateHost.current
    val stateHost = currentStateHost[onTextChangeEventName] as? StateHost<String> ?: return
    val value = stateHost.state as? String ?: return
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
    val modifier = Modifier from composeModifier
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
    val modifier = Modifier from composeModifier
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
    val modifier = Modifier from composeModifier
    Scaffold(
        modifier = modifier,
    ) {
        child?.ToCompose()
    }
}

@Composable
fun ComposeNode.ToCustom() {
    val customRenderers = LocalCustomRenderers.current
    val customProps = properties as? NodeProperties.CustomProps
    customProps?.customType?.let { customType ->
        customRenderers[customType]?.invoke(this)
    }
}