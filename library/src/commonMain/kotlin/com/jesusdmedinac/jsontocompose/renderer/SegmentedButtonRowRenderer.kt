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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeNode.ToSingleChoiceSegmentedButtonRow() {
    val props = properties as? NodeProperties.SegmentedButtonRowProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val children = props.children ?: emptyList()

    SingleChoiceSegmentedButtonRow(
        modifier = modifier
    ) {
        CompositionLocalProvider(
            LocalSegmentedButtonRowScope provides this
        ) {
            children.forEachIndexed { index, child ->
                CompositionLocalProvider(
                    LocalSegmentedButtonIndex provides index,
                    LocalSegmentedButtonCount provides children.size
                ) {
                    child.ToCompose()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeNode.ToMultiChoiceSegmentedButtonRow() {
    val props = properties as? NodeProperties.SegmentedButtonRowProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val children = props.children ?: emptyList()

    MultiChoiceSegmentedButtonRow(
        modifier = modifier
    ) {
        CompositionLocalProvider(
            LocalSegmentedButtonRowScope provides this
        ) {
            children.forEachIndexed { index, child ->
                CompositionLocalProvider(
                    LocalSegmentedButtonIndex provides index,
                    LocalSegmentedButtonCount provides children.size
                ) {
                    child.ToCompose()
                }
            }
        }
    }
}
