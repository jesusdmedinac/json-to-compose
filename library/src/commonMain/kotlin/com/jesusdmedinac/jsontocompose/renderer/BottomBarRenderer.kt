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
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

@Composable
fun ComposeNode.ToBottomBar() {
    val props = properties as? NodeProperties.BottomBarProps ?: return
    val children = props.children ?: return
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
        elevation = BottomNavigationDefaults.Elevation,
    ) {
        children.forEach { child ->
            if (child.type == ComposeType.BottomNavigationItem) {
                val itemProps = child.properties as? NodeProperties.BottomNavigationItemProps
                    ?: return@forEach
                BottomNavigationItem(
                    selected = itemProps.selected ?: false,
                    onClick = {
                        itemProps.onClickEventName?.let { behaviors[it]?.invoke() }
                    },
                    label = itemProps.label?.let { label -> { label.ToCompose() } },
                    icon = { itemProps.icon?.ToCompose() ?: Text("") },
                )
            } else {
                child.ToCompose()
            }
        }
    }
}

@Composable
fun ComposeNode.ToBottomNavigationItem() {
    println("Warning: BottomNavigationItem must be used inside a BottomBar. It will not render standalone.")
}
