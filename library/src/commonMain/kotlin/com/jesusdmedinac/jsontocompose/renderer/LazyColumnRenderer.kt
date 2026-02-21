package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

@Composable
fun ComposeNode.ToLazyColumn() {
    val props = properties as? NodeProperties.ColumnProps ?: return
    val children = props.children
    val verticalArrangement = props.verticalArrangement
        ?.toVerticalArrangement()
        ?: Arrangement.Top
    val horizontalAlignment = props.horizontalAlignment
        ?.toHorizontalsAlignment()
        ?: Alignment.Start
    val modifier = (Modifier from composeModifier).testTag(type.name)
    LazyColumn(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
    ) {
        children?.let { items(it) { child -> child.ToCompose() } }
    }
}
