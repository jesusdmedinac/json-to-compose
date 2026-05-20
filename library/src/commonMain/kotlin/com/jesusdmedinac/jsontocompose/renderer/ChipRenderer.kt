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
import com.jesusdmedinac.jsontocompose.state.StateHost
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue

@Composable
private fun resolveEnabled(
    enabledStateHostName: String?,
    enabledInline: Boolean?
): Boolean {
    val (enabled, _) = resolveStateHostValue(
        stateHostName = enabledStateHostName,
        inlineValue = enabledInline,
        defaultValue = true,
    )
    return enabled
}

@Composable
private fun resolveOnClick(
    onClickEventName: String?
): () -> Unit {
    val behavior = LocalBehavior.current[onClickEventName]
    return { behavior?.invoke() }
}

@Composable
private fun resolveSelected(
    selectedStateHostName: String?,
    selectedInline: Boolean?
) = resolveStateHostValue(
    stateHostName = selectedStateHostName,
    inlineValue = selectedInline,
    defaultValue = false,
)

@Composable
private fun resolveSelectableOnClick(
    selected: Boolean,
    selectedStateHost: StateHost<Boolean>?,
    onClickEventName: String?
): () -> Unit {
    val behavior = LocalBehavior.current[onClickEventName]
    return {
        val nextSelected = !selected
        selectedStateHost?.onStateChange(nextSelected)
        behavior?.invoke()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeNode.ToAssistChip() {
    val props = properties as? NodeProperties.ChipProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val enabled = resolveEnabled(props.enabledStateHostName, props.enabled)
    val onClick = resolveOnClick(props.onClickEventName)

    AssistChip(
        onClick = onClick,
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
    val enabled = resolveEnabled(props.enabledStateHostName, props.enabled)
    val onClick = resolveOnClick(props.onClickEventName)

    SuggestionChip(
        onClick = onClick,
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
    val (selected, selectedStateHost) = resolveSelected(props.selectedStateHostName, props.selected)
    val enabled = resolveEnabled(props.enabledStateHostName, props.enabled)
    val onClick = resolveSelectableOnClick(selected, selectedStateHost, props.onClickEventName)

    FilterChip(
        selected = selected,
        onClick = onClick,
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
    val (selected, selectedStateHost) = resolveSelected(props.selectedStateHostName, props.selected)
    val enabled = resolveEnabled(props.enabledStateHostName, props.enabled)
    val onClick = resolveSelectableOnClick(selected, selectedStateHost, props.onClickEventName)

    InputChip(
        selected = selected,
        onClick = onClick,
        label = { props.label?.ToCompose() },
        modifier = modifier,
        enabled = enabled,
        leadingIcon = props.leadingIcon?.let { { it.ToCompose() } },
        trailingIcon = props.trailingIcon?.let { { it.ToCompose() } }
    )
}
