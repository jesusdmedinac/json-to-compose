package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state.StateHost
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

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
