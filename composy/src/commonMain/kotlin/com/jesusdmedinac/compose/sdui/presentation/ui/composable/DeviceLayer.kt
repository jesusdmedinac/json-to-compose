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
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.Orientation
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
                    when (mainScreenState.deviceType to mainScreenState.orientation) {
                        DeviceType.Smartphone to Orientation.Portrait -> this
                            .width(412.dp)
                            .height(917.dp)
                        DeviceType.Smartphone to Orientation.Landscape -> this
                            .width(917.dp)
                            .height(412.dp)
                        DeviceType.Tablet to Orientation.Portrait -> this
                            .width(800.dp)
                            .height(1280.dp)
                        DeviceType.Tablet to Orientation.Landscape -> this
                            .width(1280.dp)
                            .height(800.dp)
                        DeviceType.Desktop to Orientation.Portrait -> this
                            .width(1024.dp)
                            .height(1440.dp)
                        DeviceType.Desktop to Orientation.Landscape -> this
                            .width(1440.dp)
                            .height(1024.dp)

                        else -> this
                    }
                }
                .background(MaterialTheme.colorScheme.background),
        ) {
            composeTreeState.composeNodeRoot.ToCompose()
        }
    }
}