package com.jesusdmedinac.compose.sdui.presentation.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Monitor
import com.composables.icons.lucide.Smartphone
import com.composables.icons.lucide.Tablet
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeTreeState
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.DeviceType
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.MainScreenBehavior
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.MainScreenState
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.Orientation
import com.jesusdmedinac.composy.composy.generated.resources.Res
import com.jesusdmedinac.composy.composy.generated.resources.ic_menu
import org.jetbrains.compose.resources.painterResource

@Composable
fun ComposePreview(
    composeTreeState: ComposeTreeState,
    mainScreenState: MainScreenState,
    mainScreenBehavior: MainScreenBehavior,
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
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
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
                    .horizontalScroll(stateHorizontal),
                contentAlignment = Alignment.Center,
            ) {
                DeviceLayer(
                    composeTreeState,
                    mainScreenState,
                    modifier = modifier.size(1048.dp)
                )
            }
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(8.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(2.dp)
            ) {
                IconTabBar(
                    selectedIndex = when (mainScreenState.orientation) {
                        Orientation.Portrait -> 0
                        Orientation.Landscape -> 1
                    },
                    options = listOf(
                        SelectionOption(
                            onClick = {
                                mainScreenBehavior.onPortraitClick()
                            },
                            icon = {
                                Icon(
                                    imageVector = Lucide.Smartphone,
                                    contentDescription = "Portrait",
                                )
                            }
                        ),
                        SelectionOption(
                            onClick = {
                                mainScreenBehavior.onLandscapeClick()
                            },
                            icon = {
                                Icon(
                                    imageVector = Lucide.Smartphone,
                                    contentDescription = "Horizontal",
                                    modifier = Modifier.rotate(90f),
                                )
                            }
                        )
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                IconTabBar(
                    selectedIndex = when (mainScreenState.deviceType) {
                        DeviceType.Smartphone -> 0
                        DeviceType.Tablet -> 1
                        DeviceType.Desktop -> 2
                    },
                    options = listOf(
                        SelectionOption(
                            onClick = {
                                mainScreenBehavior.onSmartphoneClick()
                            },
                            icon = {
                                Icon(
                                    imageVector = Lucide.Smartphone,
                                    contentDescription = "Smartphone",
                                )
                            }
                        ),
                        SelectionOption(
                            onClick = {
                                mainScreenBehavior.onTabletClick()
                            },
                            icon = {
                                Icon(
                                    imageVector = Lucide.Tablet,
                                    contentDescription = "Tablet",
                                )
                            }
                        ),
                        SelectionOption(
                            onClick = {
                                mainScreenBehavior.onDesktopClick()
                            },
                            icon = {
                                Icon(
                                    imageVector = Lucide.Monitor,
                                    contentDescription = "Desktop",
                                )
                            }
                        )
                    )
                )
            }
        }
    }
}