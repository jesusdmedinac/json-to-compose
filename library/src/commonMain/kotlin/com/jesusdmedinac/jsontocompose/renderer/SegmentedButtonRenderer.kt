package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.*
import androidx.compose.material3.MultiChoiceSegmentedButtonRowScope
import androidx.compose.material3.SingleChoiceSegmentedButtonRowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import com.jesusdmedinac.jsontocompose.*
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeNode.ToSegmentedButton() {
    val props = properties as? NodeProperties.SegmentedButtonProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val behaviors = LocalBehavior.current
    val index = LocalSegmentedButtonIndex.current
    val count = LocalSegmentedButtonCount.current
    val rowScope = LocalSegmentedButtonRowScope.current

    val (selected, selectedStateHost) = resolveStateHostValue(
        stateHostName = props.selectedStateHostName,
        inlineValue = props.selected,
        defaultValue = false,
    )
    
    val (enabled, _) = resolveStateHostValue(
        stateHostName = props.enabledStateHostName,
        inlineValue = props.enabled,
        defaultValue = true,
    )

    val onClick = {
        selectedStateHost?.onStateChange(!selected)
        val eventName = props.onClickEventName
        if (eventName != null) {
            val behavior = behaviors[eventName]
            if (behavior != null) {
                behavior.invoke()
            } else {
                println("Warning: Behavior for event \"$eventName\" not found in LocalBehavior.")
            }
        }
    }

    val shape = SegmentedButtonDefaults.itemShape(index = index, count = count)
    val icon: @Composable () -> Unit = {
        if (selected && props.icon == null) {
            SegmentedButtonDefaults.Icon(selected)
        } else {
            props.icon?.ToCompose()
        }
    }
    val label: @Composable () -> Unit = {
        props.label?.ToCompose()
    }

    when (rowScope) {
        is SingleChoiceSegmentedButtonRowScope -> {
            rowScope.SegmentedButton(
                selected = selected,
                onClick = onClick,
                shape = shape,
                modifier = modifier.semantics {
                    this.selected = selected
                },
                enabled = enabled,
                icon = icon,
                label = label
            )
        }

        is MultiChoiceSegmentedButtonRowScope -> {
            rowScope.SegmentedButton(
                checked = selected,
                onCheckedChange = { onClick() },
                shape = shape,
                modifier = modifier.semantics {
                    this.selected = selected
                },
                enabled = enabled,
                icon = icon,
                label = label
            )
        }

        else -> {
            // Fallback for orphan SegmentedButtons
            OutlinedButton(
                onClick = onClick,
                shape = shape,
                modifier = modifier.semantics {
                    this.selected = selected
                },
                enabled = enabled,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (selected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
                    contentColor = if (selected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline
                )
            ) {
                icon()
                label()
            }
        }
    }
}
