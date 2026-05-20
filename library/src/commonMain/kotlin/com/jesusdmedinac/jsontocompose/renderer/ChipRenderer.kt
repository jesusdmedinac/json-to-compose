package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeNode.ToAssistChip() {
    val props = properties as? NodeProperties.ChipProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val (enabled, _) = resolveStateHostValue(
        stateHostName = props.enabledStateHostName,
        inlineValue = props.enabled,
        defaultValue = true,
    )
    val behavior = LocalBehavior.current[props.onClickEventName]
    AssistChip(
        onClick = { behavior?.invoke() },
        label = { props.label?.ToCompose() },
        modifier = modifier,
        enabled = enabled,
        leadingIcon = props.leadingIcon?.let { { it.ToCompose() } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeNode.ToSuggestionChip() {
    val props = properties as? NodeProperties.ChipProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val (enabled, _) = resolveStateHostValue(
        stateHostName = props.enabledStateHostName,
        inlineValue = props.enabled,
        defaultValue = true,
    )
    val behavior = LocalBehavior.current[props.onClickEventName]
    SuggestionChip(
        onClick = { behavior?.invoke() },
        label = { props.label?.ToCompose() },
        modifier = modifier,
        enabled = enabled,
        icon = props.leadingIcon?.let { { it.ToCompose() } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeNode.ToFilterChip() {
    val props = properties as? NodeProperties.FilterChipProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
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
    val behavior = LocalBehavior.current[props.onClickEventName]
    FilterChip(
        selected = selected,
        onClick = {
            val nextSelected = !selected
            selectedStateHost?.onStateChange(nextSelected)
            behavior?.invoke()
        },
        label = { props.label?.ToCompose() },
        modifier = modifier,
        enabled = enabled,
        leadingIcon = props.leadingIcon?.let { { it.ToCompose() } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeNode.ToInputChip() {
    val props = properties as? NodeProperties.InputChipProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
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
    val behavior = LocalBehavior.current[props.onClickEventName]
    InputChip(
        selected = selected,
        onClick = {
            val nextSelected = !selected
            selectedStateHost?.onStateChange(nextSelected)
            behavior?.invoke()
        },
        label = { props.label?.ToCompose() },
        modifier = modifier,
        enabled = enabled,
        leadingIcon = props.leadingIcon?.let { { it.ToCompose() } },
        trailingIcon = props.trailingIcon?.let { { it.ToCompose() } }
    )
}
