package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
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
fun ComposeNode.ToLazyRow() {
    val props = properties as? NodeProperties.RowProps ?: return
    val children = props.children
    val horizontalArrangement = props.horizontalArrangement
        ?.toHorizontalArrangement()
        ?: Arrangement.Start
    val verticalAlignment = props.verticalAlignment
        ?.toVerticalAlignment()
        ?: Alignment.Top
    val modifier = (Modifier from composeModifier).testTag(type.name)
    LazyRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
    ) {
        children?.let { items(it) { child -> child.ToCompose() } }
    }
}
