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
fun ComposeNode.ToTabRow() {
    val props = properties as? NodeProperties.TabRowProps ?: return
    val children = props.children ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)

    val (selectedTabIndex, _) = resolveStateHostValue(
        stateHostName = props.selectedTabIndexStateHostName,
        inlineValue = props.selectedTabIndex,
        defaultValue = 0,
    )

    val containerColor = props.containerColor.toColor(TabRowDefaults.containerColor)
    val contentColor = props.contentColor.toColor(contentColorFor(containerColor))

    TabRow(
        modifier = modifier,
        selectedTabIndex = selectedTabIndex,
        containerColor = containerColor,
        contentColor = contentColor,
    ) {
        children.forEach { child -> child.ToCompose() }
    }
}

@Composable
fun ComposeNode.ToScrollableTabRow() {
    val props = properties as? NodeProperties.TabRowProps ?: return
    val children = props.children ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)

    val (selectedTabIndex, _) = resolveStateHostValue(
        stateHostName = props.selectedTabIndexStateHostName,
        inlineValue = props.selectedTabIndex,
        defaultValue = 0,
    )

    val containerColor = props.containerColor.toColor(TabRowDefaults.containerColor)
    val contentColor = props.contentColor.toColor(contentColorFor(containerColor))

    ScrollableTabRow(
        modifier = modifier,
        selectedTabIndex = selectedTabIndex,
        containerColor = containerColor,
        contentColor = contentColor,
    ) {
        children.forEach { child -> child.ToCompose() }
    }
}

@Composable
fun ComposeNode.ToTab() {
    val props = properties as? NodeProperties.TabProps ?: return
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

    val behavior = LocalBehavior.current
    val onClick = {
        props.onClickEventName?.let { behavior[it]?.invoke() }
    }

    Tab(
        modifier = modifier,
        selected = selected,
        onClick = { onClick() },
        text = props.text?.let { { it.ToCompose() } },
        icon = props.icon?.let { { it.ToCompose() } },
        enabled = enabled
    )
}
