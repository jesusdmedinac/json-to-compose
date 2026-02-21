package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.modifier.from

@Composable
fun ComposeNode.ToSpacer() {
    val modifier = (Modifier from composeModifier).testTag(type.name)

    Spacer(
        modifier = modifier,
    )
}