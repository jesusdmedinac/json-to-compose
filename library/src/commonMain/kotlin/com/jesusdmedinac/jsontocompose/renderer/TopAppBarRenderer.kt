package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material.AppBarDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.contentColorFor
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

@Composable
fun ComposeNode.ToTopAppBar() {
    val props = properties as? NodeProperties.TopAppBarProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val backgroundColor = props.backgroundColor?.let { Color(it) }
        ?: MaterialTheme.colors.primarySurface
    val contentColor = props.contentColor?.let { Color(it) }
        ?: contentColorFor(backgroundColor)
    TopAppBar(
        modifier = modifier,
        title = { props.title?.ToCompose() },
        navigationIcon = props.navigationIcon?.let { nav -> { nav.ToCompose() } },
        actions = {
            props.actions?.forEach { action -> action.ToCompose() }
        },
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        // TODO: Support elevation
        elevation = AppBarDefaults.TopAppBarElevation
    )
}
