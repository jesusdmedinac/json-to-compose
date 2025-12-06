package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.LocalDrawableResources
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state.StateHost
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import org.jetbrains.compose.resources.painterResource

@Composable
fun ComposeNode.ToColumn() {
    val props = properties as? NodeProperties.LayoutProps ?: return
    val children = props.children
    val modifier = Modifier from composeModifier
    Column(
        modifier = modifier,
    ) {
        children?.forEach {
            it.ToCompose()
        }
    }
}

@Composable
fun ComposeNode.ToRow() {
    val props = properties as? NodeProperties.LayoutProps ?: return
    val children = props.children
    val modifier = Modifier from composeModifier
    Row(
        modifier = modifier,
    ) {
        children?.forEach {
            it.ToCompose()
        }
    }
}

@Composable
fun ComposeNode.ToBox() {
    val props = properties as? NodeProperties.LayoutProps ?: return
    val children = props.children
    val modifier = Modifier from composeModifier
    Box(
        modifier = modifier,
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
    val props = properties as? NodeProperties.LayoutProps ?: return
    val children = props.children
    val modifier = Modifier from composeModifier

    LazyColumn(
        modifier = modifier,
    ) {
        children?.let { items(it) { child -> child.ToCompose() } }
    }
}

@Composable
fun ComposeNode.ToLazyRow() {
    val props = properties as? NodeProperties.LayoutProps ?: return
    val children = props.children
    val modifier = Modifier from composeModifier

    LazyRow(
        modifier = modifier,
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
