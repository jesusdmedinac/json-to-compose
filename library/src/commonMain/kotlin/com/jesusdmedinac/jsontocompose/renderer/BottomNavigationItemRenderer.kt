package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.LocalRowScope
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

@Composable
fun ComposeNode.ToBottomNavigationItem() {
    val props = properties as? NodeProperties.BottomNavigationItemProps ?: return
    val rowScope = LocalRowScope.current ?: return
    val behaviors = LocalBehavior.current

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

    with(rowScope) {
        BottomNavigationItem(
            selected = selected,
            onClick = {
                props.onClickEventName?.let { behaviors[it]?.invoke() }
            },
            modifier = (Modifier from composeModifier).testTag(type.name),
            enabled = enabled,
            label = props.label?.let { label -> { label.ToCompose() } },
            alwaysShowLabel = alwaysShowLabel,
            icon = { props.icon?.ToCompose() ?: Text("") },
            // TODO: Support interactionSource
            interactionSource = null,
            // TODO: Support selectedContentColor
            selectedContentColor = LocalContentColor.current,
            // TODO: Support unselectedContentColor
            unselectedContentColor =  LocalContentColor.current.copy(alpha = ContentAlpha.medium),
        )
    }
}
