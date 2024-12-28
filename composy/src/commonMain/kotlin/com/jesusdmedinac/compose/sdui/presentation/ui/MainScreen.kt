package com.jesusdmedinac.compose.sdui.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import com.jesusdmedinac.compose.sdui.Platform
import com.jesusdmedinac.compose.sdui.getPlatform
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeTreeScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.EditNodeScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.EditNodeSideEffect
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.MainScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.MainScreenSideEffect
import com.jesusdmedinac.composy.composy.generated.resources.Res
import com.jesusdmedinac.composy.composy.generated.resources.ic_menu
import io.github.kotlin.fibonacci.ComposeNode
import io.github.kotlin.fibonacci.ToCompose
import io.github.vinceglb.filekit.compose.rememberFileSaverLauncher
import org.jetbrains.compose.resources.painterResource

data object MainScreen : Screen {
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

        val composeTreeScreenModel = koinScreenModel<ComposeTreeScreenModel>()
        val composeEditorState by composeTreeScreenModel.state.collectAsState()
        val composeNodeRoot = composeEditorState.composeNodeRoot
        val selectedComposeNode = composeEditorState.selectedComposeNode

        val editNodeScreenModel = koinScreenModel<EditNodeScreenModel>()
        val editNodeState by editNodeScreenModel.state.collectAsState()
        val selectedComposeNodeOnEditor = editNodeState.selectedComposeNode
        val editNodeSideEffect by editNodeScreenModel.sideEffect.collectAsState()

        LaunchedEffect(selectedComposeNode) {
            screenModel.onDisplayRightPanelChange(isRightPanelDisplayed = selectedComposeNode != null)
            editNodeScreenModel.onComposeNodeSelected(selectedComposeNode)
        }

        LaunchedEffect(selectedComposeNodeOnEditor) {
            screenModel.onDisplayRightPanelChange(isRightPanelDisplayed = selectedComposeNodeOnEditor != null)
            composeTreeScreenModel.onComposeNodeSelected(selectedComposeNodeOnEditor)
        }

        LaunchedEffect(editNodeSideEffect) {
            when (val sideEffect = editNodeSideEffect) {
                EditNodeSideEffect.Idle -> Unit
                is EditNodeSideEffect.SaveNode -> {
                    composeTreeScreenModel.saveNode(sideEffect.composeNode)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "Composy",
                        color = Color.White
                    )
                },
                actions = {
                    // TODO Ignore until desktop app is deployed
                    if (false && getPlatform() == Platform.Wasm) {
                        TextButton(
                            onClick = {
                                screenModel.onDownloadDesktopClick()
                            },
                            modifier = Modifier
                                .pointerHoverIcon(
                                    icon = PointerIcon.Hand
                                )
                        ) {
                            Text(
                                text = "Download Desktop App",
                                color = Color.White
                            )
                        }
                    }
                    TextButton(
                        onClick = {
                            screenModel.exportAsJSONClick(composeNodeRoot)
                        },
                        modifier = Modifier
                            .pointerHoverIcon(
                                icon = PointerIcon.Hand
                            )
                    ) {
                        Text(
                            text = "Export as JSON",
                            color = Color.White
                        )
                    }
                },
                backgroundColor = Color(0xFF1E1E1E),
                contentColor = Color.White
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
                    ComposeNodeTree(
                        composeNodeRoot,
                        composeTreeScreenModel,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF2C2C2C))
                    )
                },
                rightPanelContent = {
                    ComposeNodeEditor(
                        editNodeState,
                        editNodeScreenModel
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
                        .background(Color(0xFF1E1E1E))
                )
            }
        }
    }
}

@Composable
fun ComposePreview(
    composeNodeRoot: ComposeNode,
    onLeftPanelButtonClick: () -> Unit,
    onRightPanelButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row {
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
                    tint = Color.White
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
                    tint = Color.White
                )
            }
        }
        composeNodeRoot.ToCompose(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        )
    }
}