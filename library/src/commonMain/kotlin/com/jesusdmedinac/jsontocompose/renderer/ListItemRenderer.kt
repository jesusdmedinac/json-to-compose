package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

@Composable
fun ComposeNode.ToListItem() {
    val props = properties as? NodeProperties.ListItemProps ?: return
    val onClickEventName = props.onClickEventName
    val currentBehavior = LocalBehavior.current
    val behavior = currentBehavior[onClickEventName]

    val baseModifier = Modifier from composeModifier
    val modifier = if (onClickEventName != null) {
        baseModifier
            .clickable {
                if (behavior != null) {
                    behavior.invoke()
                } else {
                    println("Warning: Behavior for event \"$onClickEventName\" not found in LocalBehavior.")
                }
            }
    } else {
        baseModifier
    }.testTag(type.name)

    ListItem(
        headlineContent = {
            props.headlineContent?.ToCompose()
        },
        modifier = modifier,
        supportingContent = props.supportingContent?.let {
            { it.ToCompose() }
        },
        overlineContent = props.overlineContent?.let {
            { it.ToCompose() }
        },
        leadingContent = props.leadingContent?.let {
            { it.ToCompose() }
        },
        trailingContent = props.trailingContent?.let {
            { it.ToCompose() }
        }
    )
}
