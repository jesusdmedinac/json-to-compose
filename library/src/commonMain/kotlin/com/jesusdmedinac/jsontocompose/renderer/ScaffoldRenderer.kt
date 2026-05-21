package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import com.jesusdmedinac.jsontocompose.state.StateHost

@Composable
fun ComposeNode.ToScaffold() {
    val props = properties as? NodeProperties.ScaffoldProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val containerColor = props.containerColor?.toColor()

    // Resolve or create a SnackbarHostState shared via StateHosts
    val stateHosts = LocalStateHost.current
    val snackbarHostState = remember(props.snackbarHostStateHostName, stateHosts) {
        val hostName = props.snackbarHostStateHostName ?: "snackbarState"
        val stateHost = stateHosts[hostName]
        val existing = stateHost?.state as? SnackbarHostState
        if (existing != null) {
            existing
        } else {
            val newHostState = SnackbarHostState()
            if (stateHost != null) {
                @Suppress("UNCHECKED_CAST")
                (stateHost as? StateHost<Any>)?.onStateChange(newHostState)
            }
            newHostState
        }
    }

    val fabPosition = when (props.floatingActionButtonPosition?.lowercase()) {
        "center" -> FabPosition.Center
        else -> FabPosition.End
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            props.topBar?.ToCompose()
        },
        bottomBar = {
            props.bottomBar?.ToCompose()
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            props.floatingActionButton?.ToCompose()
        },
        floatingActionButtonPosition = fabPosition,
        containerColor = containerColor ?: androidx.compose.material3.MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            props.child?.ToCompose()
        }
    }
}
