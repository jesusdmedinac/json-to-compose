package com.jesusdmedinac.compose.sdui.presentation.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeTreeState
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.DeviceType
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.MainScreenState
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.DeviceOrientation
import com.jesusdmedinac.jsontocompose.ToCompose

@Composable
fun DeviceLayer(
    composeTreeState: ComposeTreeState,
    mainScreenState: MainScreenState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .padding(8.dp)
                .run {
                    with(mainScreenState.deviceSize) {
                        this@run
                            .width(width.dp)
                            .height(height.dp)
                    }
                }
                .background(MaterialTheme.colorScheme.background),
        ) {
            composeTreeState.composeNodeRoot.ToCompose()
        }
    }
}