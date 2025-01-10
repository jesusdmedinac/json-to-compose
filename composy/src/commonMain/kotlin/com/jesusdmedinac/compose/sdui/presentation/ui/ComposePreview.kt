package com.jesusdmedinac.compose.sdui.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeTreeState
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.DeviceType
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.MainScreenState
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.Orientation
import com.jesusdmedinac.composy.composy.generated.resources.Res
import com.jesusdmedinac.composy.composy.generated.resources.ic_menu
import com.jesusdmedinac.jsontocompose.ComposeNode
import com.jesusdmedinac.jsontocompose.ToCompose
import org.jetbrains.compose.resources.painterResource

@Composable
fun ComposePreview(
    composeTreeState: ComposeTreeState,
    mainScreenState: MainScreenState,
    onLeftPanelButtonClick: () -> Unit,
    onRightPanelButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    MaterialTheme.shapes.extraLarge.copy(
                        bottomStart = CornerSize(0.0.dp),
                        bottomEnd = CornerSize(0.0.dp)
                    )
                )
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(8.dp)
        ) {
            IconButton(
                onClick = {
                    onLeftPanelButtonClick()
                },
                modifier = Modifier
                    .pointerHoverIcon(
                        icon = PointerIcon.Hand
                    )
            ) {
                Icon(
                    painterResource(Res.drawable.ic_menu),
                    contentDescription = "Open left panel",
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    onRightPanelButtonClick()
                },
                modifier = Modifier
                    .pointerHoverIcon(
                        icon = PointerIcon.Hand
                    )
            ) {
                Icon(
                    painterResource(Res.drawable.ic_menu),
                    contentDescription = "Open right panel",
                )
            }
        }
        val stateHorizontal = rememberScrollState()
        val stateVertical = rememberScrollState()
        Box(
            modifier = Modifier
                .clip(
                    MaterialTheme.shapes.extraLarge.copy(
                        topStart = CornerSize(0.0.dp),
                        topEnd = CornerSize(0.0.dp)
                    )
                )
                .background(MaterialTheme.colorScheme.surfaceDim)
                .fillMaxSize()
                .verticalScroll(stateVertical)
                .horizontalScroll(stateHorizontal)
        ) {
            Box(
                modifier = Modifier
                    .size(1048.dp),
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
    }
}