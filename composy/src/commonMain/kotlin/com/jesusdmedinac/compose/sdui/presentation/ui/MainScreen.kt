package com.jesusdmedinac.compose.sdui.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Monitor
import com.composables.icons.lucide.Smartphone
import com.composables.icons.lucide.Tablet
import com.jesusdmedinac.compose.sdui.Platform
import com.jesusdmedinac.compose.sdui.getPlatform
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeComponentsScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeComponentsSideEffect
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeTreeScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeTreeState
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.DeviceType
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.EditNodeScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.MainScreenBehavior
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.MainScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.MainScreenSideEffect
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.MainScreenState
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.Orientation
import com.jesusdmedinac.compose.sdui.presentation.ui.composable.IconTabBar
import com.jesusdmedinac.compose.sdui.presentation.ui.composable.SelectionOption
import com.jesusdmedinac.jsontocompose.ComposeNode
import com.jesusdmedinac.jsontocompose.ComposeType
import io.github.vinceglb.filekit.compose.rememberFileSaverLauncher

data object MainScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<MainScreenModel>()
        val state by screenModel.state.collectAsState()
        val isLeftPanelDisplayed = state.isLeftPanelDisplayed
        val isRightPanelDisplayed = state.isRightPanelDisplayed
        val mainScreenSideEffect by screenModel.sideEffect.collectAsState()

        val launcher = rememberFileSaverLauncher { file -> }
        LaunchedEffect(mainScreenSideEffect) {
            when (val sideEffect = mainScreenSideEffect) {
                MainScreenSideEffect.Idle -> Unit
                is MainScreenSideEffect.ExportAsJSON -> {
                    launcher.launch(
                        baseName = sideEffect.baseName,
                        extension = sideEffect.extension,
                        initialDirectory = sideEffect.initialDirectory,
                        bytes = sideEffect.content.encodeToByteArray()
                    )
                }
            }
        }

        val composeComponentsScreenModel = koinScreenModel<ComposeComponentsScreenModel>()
        val composeComponentsSideEffect by composeComponentsScreenModel.sideEffect.collectAsState()

        val composeTreeScreenModel = koinScreenModel<ComposeTreeScreenModel>()
        val composeTreeState by composeTreeScreenModel.state.collectAsState()
        val composeNodeRoot = composeTreeState.composeNodeRoot
        val selectedComposeNode = composeTreeState.selectedComposeNode

        val editNodeScreenModel = koinScreenModel<EditNodeScreenModel>()
        val editNodeState by editNodeScreenModel.state.collectAsState()
        val selectedComposeNodeOnEditor = editNodeState.selectedComposeNode

        LaunchedEffect(selectedComposeNode) {
            screenModel.onDisplayRightPanelChange(isRightPanelDisplayed = selectedComposeNode != null)
            editNodeScreenModel.onComposeNodeSelected(selectedComposeNode)
        }

        LaunchedEffect(selectedComposeNodeOnEditor) {
            screenModel.onDisplayRightPanelChange(isRightPanelDisplayed = selectedComposeNodeOnEditor != null)
            composeTreeScreenModel.onComposeNodeSelected(selectedComposeNodeOnEditor)
        }

        LaunchedEffect(editNodeState) {
            editNodeState.editingComposeNode?.let { composeTreeScreenModel.saveNode(it) }
        }

        LaunchedEffect(composeNodeRoot) {
        }

        LaunchedEffect(composeComponentsSideEffect) {
            when (val sideEffect = composeComponentsSideEffect) {
                ComposeComponentsSideEffect.Idle -> Unit
                is ComposeComponentsSideEffect.ComposeTypeClicked -> {
                    when (selectedComposeNode?.type) {
                        ComposeType.Column, ComposeType.Row, ComposeType.Box -> {
                            composeTreeScreenModel.onAddNewNodeToChildren(
                                ComposeNode(
                                    type = sideEffect.type,
                                    parent = selectedComposeNode,
                                )
                            )
                        }

                        ComposeType.Button -> {
                            composeTreeScreenModel.onAddNewNodeAsChild(
                                ComposeNode(
                                    type = sideEffect.type,
                                    parent = selectedComposeNode,
                                )
                            )
                        }

                        else -> Unit
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            MainScreenTopAppBar(
                composeTreeState,
                state,
                screenModel
            )
            WindowWithPanels(
                isLeftPanelDisplayed,
                onLeftPanelClosed = {
                    screenModel.onDisplayLeftPanelChange(false)
                },
                isRightPanelDisplayed,
                onRightPanelClosed = {
                    screenModel.onDisplayRightPanelChange(false)
                },
                leftPanelContent = {
                    SplitColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        top = { modifier ->
                            ComposeComponents(
                                screenModel = composeComponentsScreenModel,
                                modifier = modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .background(MaterialTheme.colorScheme.surfaceContainer)
                                    .padding(8.dp)
                            )
                        },
                        bottom = { modifier ->
                            ComposeNodeTree(
                                composeNodeRoot,
                                composeTreeScreenModel,
                                modifier = modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .background(MaterialTheme.colorScheme.surfaceContainer)
                                    .padding(8.dp)
                            )
                        }
                    )
                },
                rightPanelContent = {
                    ComposeNodeEditor(
                        editNodeState,
                        editNodeScreenModel,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .padding(8.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
            ) {
                ComposePreview(
                    composeNodeRoot,
                    onLeftPanelButtonClick = {
                        screenModel.onDisplayLeftPanelChange(!isLeftPanelDisplayed)
                    },
                    onRightPanelButtonClick = {
                        screenModel.onDisplayRightPanelChange(!isRightPanelDisplayed)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenTopAppBar(
    composeTreeState: ComposeTreeState,
    mainScreenState: MainScreenState,
    mainScreenBehavior: MainScreenBehavior,
) {
    TopAppBar(
        title = {
            Text(
                text = "Composy",
            )
        },
        actions = {
            // TODO Ignore until desktop app is deployed
            if (false && getPlatform() == Platform.Wasm) {
                TextButton(
                    onClick = {
                        mainScreenBehavior.onDownloadDesktopClick()
                    },
                    modifier = Modifier
                        .pointerHoverIcon(
                            icon = PointerIcon.Hand
                        )
                ) {
                    Text(
                        text = "Download Desktop App",
                    )
                }
            }
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
            Button(
                onClick = {
                    mainScreenBehavior.exportAsJSONClick(composeTreeState.composeNodeRoot)
                },
                modifier = Modifier
                    .pointerHoverIcon(
                        icon = PointerIcon.Hand
                    )
            ) {
                Text(
                    text = "Export as JSON",
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface,
        )
    )
}