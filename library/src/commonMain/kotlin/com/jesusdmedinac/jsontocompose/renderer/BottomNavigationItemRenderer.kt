package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.LocalRowScope
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state.StateHost
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

@Composable
fun ComposeNode.ToBottomNavigationItem() {
    val props = properties as? NodeProperties.BottomNavigationItemProps ?: return
    val rowScope = LocalRowScope.current ?: return
    val behaviors = LocalBehavior.current

    val selectedStateHostName = props.selectedStateHostName
    if (selectedStateHostName == null) {
        println("Warning: BottomNavigationItem node has no selectedStateHostName. The component will not render.")
        return
    }

    val enabledStateHostName = props.enabledStateHostName
    if (enabledStateHostName == null) {
        println("Warning: BottomNavigationItem node has no enabledStateHostName. The component will not render.")
        return
    }

    val currentStateHost = LocalStateHost.current
    val selectedStateHost = currentStateHost[selectedStateHostName]
    if (selectedStateHost == null) {
        println("Warning: No StateHost registered with name \"$selectedStateHostName\". Expected StateHost<Boolean>.")
        return
    }

    val enabledStateHost = currentStateHost[enabledStateHostName]
    if (enabledStateHost == null) {
        println("Warning: No StateHost registered with name \"$enabledStateHostName\". Expected StateHost<Boolean>.")
        return
    }

    @Suppress("UNCHECKED_CAST")
    val typedSelectedStateHost = selectedStateHost as? StateHost<Boolean>
    if (typedSelectedStateHost == null) {
        println("Warning: StateHost \"$selectedStateHostName\" is not of type StateHost<Boolean>. Check the registered type.")
        return
    }

    @Suppress("UNCHECKED_CAST")
    val typedEnabledStateHost = enabledStateHost as? StateHost<Boolean>
    if (typedEnabledStateHost == null) {
        println("Warning: StateHost \"$enabledStateHostName\" is not of type StateHost<Boolean>. Check the registered type.")
        return
    }

    val selected = runCatching { typedSelectedStateHost.state }
        .getOrElse {
            println("Warning: StateHost \"$selectedStateHostName\" is not of type StateHost<Boolean>. Check the registered type.")
            false
        }

    val enabled = runCatching { typedEnabledStateHost.state }
        .getOrElse {
            println("Warning: StateHost \"$enabledStateHostName\" is not of type StateHost<Boolean>. Check the registered type.")
            true
        }

    with(rowScope) {
        BottomNavigationItem(
            selected = selected,
            onClick = {
                props.onClickEventName?.let { behaviors[it]?.invoke() }
            },
            modifier = (Modifier from composeModifier).testTag(type.name),
            enabled = enabled,
            label = props.label?.let { label -> { label.ToCompose() } },
            alwaysShowLabel = props.alwaysShowLabel ?: true,
            icon = { props.icon?.ToCompose() ?: Text("") },
            // TODO: Support interactionSource
            // TODO: Support selectedContentColor
            // TODO: Support unselectedContentColor
        )
    }
}
