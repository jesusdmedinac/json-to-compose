package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ComposeNode.ToFlowRow() {
    val props = properties as? NodeProperties.FlowRowProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)

    val children = props.children
    val horizontalArrangement = props.horizontalArrangement
        ?.toHorizontalArrangement()
        ?: Arrangement.Start
    val verticalArrangement = props.verticalArrangement
        ?.toVerticalArrangement()
        ?: Arrangement.Top

    FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement
    ) {
        children?.forEach { it.ToCompose() }
    }
}
