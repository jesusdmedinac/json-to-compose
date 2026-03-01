package com.jesusdmedinac.compose.sdui.presentation.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Monitor
import com.composables.icons.lucide.Smartphone
import com.composables.icons.lucide.Tablet
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeTreeScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.DeviceOrientation
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.DeviceType
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.MainScreenModel
import com.jesusdmedinac.compose.sdui.presentation.ui.layouts.BoxWithSizeListener
import com.jesusdmedinac.compose.sdui.presentation.ui.layouts.IconTabBar
import com.jesusdmedinac.compose.sdui.presentation.ui.layouts.SelectionOption
import com.jesusdmedinac.composy.composy.generated.resources.Res
import com.jesusdmedinac.composy.composy.generated.resources.ic_menu
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt

@Composable
fun ComposePreview(
    modifier: Modifier = Modifier
) {
    val navigator = LocalNavigator.currentOrThrow
    val mainScreenModel: MainScreenModel = navigator.koinNavigatorScreenModel()
    val mainScreenState by mainScreenModel.state.collectAsState()
    val deviceSize = mainScreenState.deviceSize
    val density = LocalDensity.current
    val halfOfDeviceHeight = (with(density) { deviceSize.height.dp.toPx() / 2 }).roundToInt()
    val halfOfDeviceWidth = (with(density) { deviceSize.width.dp.toPx() / 2 }).roundToInt()
    val deviceType = mainScreenState.deviceType
    val deviceOrientation = mainScreenState.deviceOrientation
    val isLeftPanelDisplayed = mainScreenState.isLeftPanelDisplayed
    val isRightPanelDisplayed = mainScreenState.isRightPanelDisplayed

    val composeTreeScreenModel: ComposeTreeScreenModel = navigator.koinNavigatorScreenModel()
    val composeTreeState by composeTreeScreenModel.state.collectAsState()
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
                    mainScreenModel.onDisplayLeftPanelChange(!isLeftPanelDisplayed)
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
                    mainScreenModel.onDisplayRightPanelChange(!isRightPanelDisplayed)
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
                .background(MaterialTheme.colorScheme.surfaceContainer),
        ) {
            var xOffset by remember { mutableStateOf(0f) }
            var yOffset by remember { mutableStateOf(0f) }
            var layoutSize by remember { mutableStateOf(IntSize.Zero) }
            val halfOfLayoutHeight = layoutSize.height / 2
            val halfOfLayoutWidth = layoutSize.width / 2
            val yOffsetLimit = if (deviceSize.height > layoutSize.height) halfOfLayoutHeight
            else halfOfDeviceHeight
            val xOffsetLimit = if (deviceSize.width > layoutSize.width) halfOfLayoutWidth
            else halfOfDeviceWidth

            BoxWithSizeListener(
                content = {
                    DeviceLayer(
                        modifier = Modifier
                            .graphicsLayer {
                                translationX = xOffset
                                translationY = yOffset
                            }
                    )
                },
                onSizeChanged = { size ->
                    layoutSize = size
                },
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        MaterialTheme.shapes.extraLarge.copy(
                            topStart = CornerSize(0.0.dp),
                            topEnd = CornerSize(0.0.dp)
                        )
                    )
                    .background(MaterialTheme.colorScheme.surfaceDim)
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, _, _ ->
                            val currentYLimit = yOffsetLimit.toFloat()
                            val currentXLimit = xOffsetLimit.toFloat()

                            xOffset = (xOffset + pan.x).coerceIn(
                                minimumValue = -currentXLimit,
                                maximumValue = currentXLimit
                            )

                            yOffset = (yOffset + pan.y).coerceIn(
                                minimumValue = -currentYLimit,
                                maximumValue = currentYLimit
                            )
                        }
                    },
                contentAlignment = Alignment.Center
            )
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(8.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(2.dp)
            ) {
                IconTabBar(
                    selectedIndex = when (deviceOrientation) {
                        DeviceOrientation.Portrait -> 0
                        DeviceOrientation.Landscape -> 1
                    },
                    options = listOf(
                        SelectionOption(
                            onClick = {
                                mainScreenModel.onPortraitClick()
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
                                mainScreenModel.onLandscapeClick()
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
                    selectedIndex = when (deviceType) {
                        DeviceType.Smartphone -> 0
                        DeviceType.Tablet -> 1
                        DeviceType.Desktop -> 2
                    },
                    options = listOf(
                        SelectionOption(
                            onClick = {
                                mainScreenModel.onSmartphoneClick()
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
                                mainScreenModel.onTabletClick()
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
                                mainScreenModel.onDesktopClick()
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