package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.layout.ColumnScope
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
fun ComposeNode.ToSearchBar() {
    val props = properties as? NodeProperties.SearchBarProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val behaviors = LocalBehavior.current

    val (query, queryStateHost) = resolveStateHostValue(
        stateHostName = props.queryStateHostName,
        inlineValue = props.query,
        defaultValue = "",
    )

    val (active, activeStateHost) = resolveStateHostValue(
        stateHostName = props.activeStateHostName,
        inlineValue = props.active,
        defaultValue = false,
    )

    SearchBar(
        query = query,
        onQueryChange = { newQuery ->
            queryStateHost?.onStateChange(newQuery)
            val eventName = props.onQueryChangeEventName
            if (eventName != null) {
                behaviors[eventName]?.invoke()
            }
        },
        onSearch = { /* Handle search if needed */ },
        active = active,
        onActiveChange = { newActive ->
            activeStateHost?.onStateChange(newActive)
        },
        modifier = modifier,
        placeholder = { props.placeholder?.ToCompose() },
        leadingIcon = { props.leadingIcon?.ToCompose() },
        trailingIcon = { props.trailingIcon?.ToCompose() }
    ) {
        props.children?.forEach { it.ToCompose() }
    }
}
