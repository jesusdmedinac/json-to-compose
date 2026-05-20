package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeNode.ToBadgedBox() {
    val props = properties as? NodeProperties.BadgedBoxProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    BadgedBox(
        badge = {
            props.badge?.ToCompose()
        },
        modifier = modifier,
        content = {
            props.child?.ToCompose()
        }
    )
}
