package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue

@Composable
fun ComposeNode.ToModalNavigationDrawer() {
    val props = properties as? NodeProperties.NavigationDrawerProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)

    val openState = resolveStateHostValue<Boolean>(
        stateHostName = props.isOpenStateHostName,
        inlineValue = props.isOpen,
        defaultValue = false,
    )
    val isOpen = openState.value
    val openStateHost = openState.stateHost

    val drawerState = rememberDrawerState(
        initialValue = if (isOpen) DrawerValue.Open else DrawerValue.Closed
    )

    val behavior = LocalBehavior.current
    val onDismiss = {
        val eventName = props.onDismissRequestEventName
        if (eventName != null) {
            val b = behavior[eventName]
            if (b != null) {
                b()
            }
        }
    }

    LaunchedEffect(isOpen) {
        if (isOpen) drawerState.open() else drawerState.close()
    }

    LaunchedEffect(drawerState.currentValue) {
        val isDrawerOpen = drawerState.currentValue == DrawerValue.Open
        if (isOpen != isDrawerOpen) {
            openStateHost?.onStateChange(isDrawerOpen)
            if (!isDrawerOpen) {
                onDismiss()
            }
        }
    }

    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                props.drawerContent?.forEach { it.ToCompose() }
            }
        },
        gesturesEnabled = true,
        content = {
            props.child?.ToCompose()
        }
    )
}

@Composable
fun ComposeNode.ToNavigationDrawerItem() {
    val props = properties as? NodeProperties.NavigationDrawerItemProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)

    val (selected, _) = resolveStateHostValue(
        stateHostName = props.selectedStateHostName,
        inlineValue = props.selected,
        defaultValue = false,
    )

    val behavior = LocalBehavior.current
    val onClick = {
        val eventName = props.onClickEventName
        if (eventName != null) {
            val b = behavior[eventName]
            if (b != null) {
                b()
            }
        }
    }

    NavigationDrawerItem(
        modifier = modifier,
        label = { props.label?.ToCompose() },
        selected = selected,
        onClick = { onClick() },
        icon = props.icon?.let { iconNode -> { iconNode.ToCompose() } },
        badge = props.badge?.let { badgeNode -> { badgeNode.ToCompose() } }
    )
}
