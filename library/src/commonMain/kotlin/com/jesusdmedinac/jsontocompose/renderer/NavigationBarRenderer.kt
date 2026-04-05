package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.LocalRowScope
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue

@Composable
fun ComposeNode.ToNavigationBar() {
    val props = properties as? NodeProperties.NavigationBarProps ?: return
    val children = props.children ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val containerColor = props.containerColor.toColor(NavigationBarDefaults.containerColor)
    val contentColor = props.contentColor.toColor(contentColorFor(containerColor))

    NavigationBar(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
    ) {
        CompositionLocalProvider(LocalRowScope provides this) {
            children.forEach { child -> child.ToCompose() }
        }
    }
}

@Composable
fun ComposeNode.ToNavigationBarItem() {
    val props = properties as? NodeProperties.NavigationBarItemProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val rowScope = LocalRowScope.current ?: return

    val (selected, _) = resolveStateHostValue(
        stateHostName = props.selectedStateHostName,
        inlineValue = props.selected,
        defaultValue = false,
    )

    val (enabled, _) = resolveStateHostValue(
        stateHostName = props.enabledStateHostName,
        inlineValue = props.enabled,
        defaultValue = true,
    )

    val (alwaysShowLabel, _) = resolveStateHostValue(
        stateHostName = props.alwaysShowLabelStateHostName,
        inlineValue = props.alwaysShowLabel,
        defaultValue = true,
    )

    val behavior = LocalBehavior.current
    val onClick = {
        props.onClickEventName?.let { eventName -> behavior[eventName]?.invoke() }
    }

    with(rowScope) {
        NavigationBarItem(
            modifier = modifier,
            selected = selected,
            onClick = { onClick() },
            icon = { props.icon?.ToCompose() ?: androidx.compose.material3.Text("") },
            label = props.label?.let { labelNode -> { labelNode.ToCompose() } },
            enabled = enabled,
            alwaysShowLabel = alwaysShowLabel,
        )
    }
}
