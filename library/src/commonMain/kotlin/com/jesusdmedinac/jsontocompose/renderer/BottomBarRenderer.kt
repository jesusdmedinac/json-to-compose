package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationDefaults
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

@Composable
fun ComposeNode.ToBottomBar() {
    val props = properties as? NodeProperties.BottomBarProps ?: return
    val items = props.items ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val backgroundColor = props.backgroundColor?.let { Color(it) }
        ?: MaterialTheme.colors.primarySurface
    val contentColor = props.contentColor?.let { Color(it) }
        ?: contentColorFor(backgroundColor)
    val behaviors = LocalBehavior.current

    BottomNavigation(
        modifier = modifier,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        // TODO: Support elevation
        elevation = BottomNavigationDefaults.Elevation,
    ) {
        items.forEachIndexed { index, item ->
            BottomNavigationItem(
                selected = index == (props.selectedIndex ?: 0),
                onClick = {
                    item.eventName?.let { behaviors[it]?.invoke() }
                },
                label = { Text(item.label ?: "") },
                icon = { Text(item.iconName ?: "") },
            )
        }
    }
}
