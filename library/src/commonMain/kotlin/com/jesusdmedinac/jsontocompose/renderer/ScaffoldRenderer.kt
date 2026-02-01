package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

@Composable
fun ComposeNode.ToScaffold() {
    val props = properties as? NodeProperties.ScaffoldProps ?: return
    val child = props.child
    val modifier = (Modifier from composeModifier).testTag(type.name)
    Scaffold(
        modifier = modifier,
        topBar = {
            props.topBar?.ToCompose()
        },
        bottomBar = {
            props.bottomBar?.ToCompose()
        },
    ) {
        child?.ToCompose()
    }
}
