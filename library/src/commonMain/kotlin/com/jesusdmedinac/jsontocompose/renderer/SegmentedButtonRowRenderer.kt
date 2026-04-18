package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.LocalSegmentedButtonCount
import com.jesusdmedinac.jsontocompose.LocalSegmentedButtonIndex
import com.jesusdmedinac.jsontocompose.LocalSegmentedButtonRowScope
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

@Composable
fun ComposeNode.ToSingleChoiceSegmentedButtonRow() {
    SegmentedButtonRowContent()
}

@Composable
fun ComposeNode.ToMultiChoiceSegmentedButtonRow() {
    SegmentedButtonRowContent()
}

@Composable
private fun ComposeNode.SegmentedButtonRowContent() {
    val props = properties as? NodeProperties.SegmentedButtonRowProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val children = props.children ?: emptyList()

    Row(
        modifier = modifier
    ) {
        CompositionLocalProvider(
            LocalSegmentedButtonRowScope provides this,
            LocalSegmentedButtonCount provides children.size
        ) {
            children.forEachIndexed { index, child ->
                CompositionLocalProvider(LocalSegmentedButtonIndex provides index) {
                    child.ToCompose()
                }
            }
        }
    }
}
