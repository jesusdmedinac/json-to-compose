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
fun ComposeNode.ToModalNavigationDrawer() {
    val props = properties as? NodeProperties.NavigationDrawerProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)

    val (isOpen, _) = resolveStateHostValue(
        stateHostName = props.isOpenStateHostName,
        inlineValue = props.isOpen,
        defaultValue = false,
    )

    val drawerState = rememberDrawerState(
        initialValue = if (isOpen) DrawerValue.Open else DrawerValue.Closed
    )

    LaunchedEffect(isOpen) {
        if (isOpen) drawerState.open() else drawerState.close()
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
        props.onClickEventName?.let { behavior[it]?.invoke() }
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
