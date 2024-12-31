package com.jesusdmedinac.compose.sdui.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeTreeScreenModel
import com.jesusdmedinac.jsontocompose.ComposeNode


@Composable
fun ComposeNodeTree(
    composeNodeRoot: ComposeNode,
    screenModel: ComposeTreeScreenModel,
    modifier: Modifier = Modifier,
) {
    val state by screenModel.state.collectAsState()
    composeNodeRoot.ToComposeTree(
        modifier = modifier,
        state = state,
        behavior = screenModel
    )
}