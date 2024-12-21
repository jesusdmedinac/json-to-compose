package com.jesusdmedinac.compose.sdui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import io.github.kotlin.fibonacci.ComposeNode


@Composable
fun ComposeNodeTree(
    composeNodeRoot: ComposeNode,
    screenModel: ComposeEditorScreenModel,
    modifier: Modifier = Modifier,
) {
    val state by screenModel.state.collectAsState()
    composeNodeRoot.ToComposeEditor(
        modifier = modifier,
        state = state,
        behavior = screenModel
    )
}