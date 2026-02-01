package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state.StateHost
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

@Composable
fun ComposeNode.ToCheckbox() {
    val props = properties as? NodeProperties.CheckboxProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val behaviors = LocalBehavior.current

    val checkedStateHostName = props.checkedStateHostName
    if (checkedStateHostName == null) {
        println("Warning: Checkbox node has no checkedStateHostName. The component will not render.")
        return
    }

    val enabledStateHostName = props.enabledStateHostName
    if (enabledStateHostName == null) {
        println("Warning: Checkbox node has no enabledStateHostName. The component will not render.")
        return
    }

    val currentStateHost = LocalStateHost.current
    val checkedStateHost = currentStateHost[checkedStateHostName]
    if (checkedStateHost == null) {
        println("Warning: No StateHost registered with name \"$checkedStateHostName\". Expected StateHost<Boolean>.")
        return
    }

    val enabledStateHost = currentStateHost[enabledStateHostName]
    if (enabledStateHost == null) {
        println("Warning: No StateHost registered with name \"$enabledStateHostName\". Expected StateHost<Boolean>.")
        return
    }

    @Suppress("UNCHECKED_CAST")
    val typedCheckedStateHost = checkedStateHost as? StateHost<Boolean>
    if (typedCheckedStateHost == null) {
        println("Warning: StateHost \"$checkedStateHostName\" is not of type StateHost<Boolean>. Check the registered type.")
        return
    }

    @Suppress("UNCHECKED_CAST")
    val typedEnabledStateHost = enabledStateHost as? StateHost<Boolean>
    if (typedEnabledStateHost == null) {
        println("Warning: StateHost \"$enabledStateHostName\" is not of type StateHost<Boolean>. Check the registered type.")
        return
    }

    val checked = runCatching { typedCheckedStateHost.state }
        .getOrElse {
            println("Warning: StateHost \"$checkedStateHostName\" is not of type StateHost<Boolean>. Check the registered type.")
            false
        }

    val enabled = runCatching { typedEnabledStateHost.state }
        .getOrElse {
            println("Warning: StateHost \"$enabledStateHostName\" is not of type StateHost<Boolean>. Check the registered type.")
            true
        }

    Checkbox(
        checked = checked,
        onCheckedChange = { newValue ->
            typedCheckedStateHost.onStateChange(newValue)
            props.onCheckedChangeEventName?.let { behaviors[it]?.invoke() }
        },
        modifier = modifier,
        enabled = enabled,
    )
}
