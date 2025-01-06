package com.jesusdmedinac.compose.sdui.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import com.jesusdmedinac.compose.sdui.Platform
import com.jesusdmedinac.compose.sdui.getPlatform
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeComponentsScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeComponentsSideEffect
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeTreeScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.EditNodeScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.MainScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.MainScreenSideEffect
import com.jesusdmedinac.composy.composy.generated.resources.Res
import com.jesusdmedinac.composy.composy.generated.resources.ic_menu
import com.jesusdmedinac.jsontocompose.ComposeModifier
import com.jesusdmedinac.jsontocompose.ComposeNode
import com.jesusdmedinac.jsontocompose.ComposeType
import com.jesusdmedinac.jsontocompose.ToCompose
import io.github.vinceglb.filekit.compose.rememberFileSaverLauncher
import org.jetbrains.compose.resources.painterResource

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
        val composeEditorState by composeTreeScreenModel.state.collectAsState()
        val composeNodeRoot = composeEditorState.composeNodeRoot
        val selectedComposeNode = composeEditorState.selectedComposeNode

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
                                screenModel.onDownloadDesktopClick()
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
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                )
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.extraLarge.copy(bottomStart = CornerSize(0.0.dp), bottomEnd = CornerSize(0.0.dp)))
                .background(MaterialTheme.colorScheme.background)
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
                .clip(MaterialTheme.shapes.extraLarge.copy(topStart = CornerSize(0.0.dp), topEnd = CornerSize(0.0.dp)))
                .background(MaterialTheme.colorScheme.background)
                .padding(8.dp)
                .fillMaxSize()
        ) {
            composeNodeRoot.ToCompose()
        }
    }
}