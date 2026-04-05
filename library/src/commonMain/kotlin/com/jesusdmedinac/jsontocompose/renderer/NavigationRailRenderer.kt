package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue

@Composable
fun ComposeNode.ToNavigationRail() {
    val props = properties as? NodeProperties.NavigationRailProps ?: return
    val children = props.children ?: emptyList()
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val containerColor = props.containerColor.toColor(MaterialTheme.colorScheme.surface)
    val contentColor = props.contentColor.toColor(contentColorFor(containerColor))

    NavigationRail(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        header = props.header?.let { headerNode -> { headerNode.ToCompose() } }
    ) {
        children.forEach { child -> child.ToCompose() }
    }
}

@Composable
fun ComposeNode.ToNavigationRailItem() {
    val props = properties as? NodeProperties.NavigationRailItemProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)

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
        props.onClickEventName?.let { behavior[it]?.invoke() }
    }

    NavigationRailItem(
        modifier = modifier,
        selected = selected,
        onClick = { onClick() },
        icon = { props.icon?.ToCompose() },
        label = props.label?.let { labelNode -> { labelNode.ToCompose() } },
        enabled = enabled,
        alwaysShowLabel = alwaysShowLabel,
    )
}
