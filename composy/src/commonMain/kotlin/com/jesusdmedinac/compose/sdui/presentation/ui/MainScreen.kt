package com.jesusdmedinac.compose.sdui.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.jesusdmedinac.compose.sdui.Platform
import com.jesusdmedinac.compose.sdui.auth.presentation.screenmodel.AuthBehavior
import com.jesusdmedinac.compose.sdui.auth.presentation.screenmodel.AuthScreenModel
import com.jesusdmedinac.compose.sdui.auth.presentation.screenmodel.AuthState
import com.jesusdmedinac.compose.sdui.auth.presentation.ui.AuthScreen
import com.jesusdmedinac.compose.sdui.getPlatform
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeComponentsScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeComponentsSideEffect
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeTreeScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeTreeState
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.EditNodeScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.MainScreenBehavior
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.MainScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.MainScreenSideEffect
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.MainScreenState
import com.jesusdmedinac.compose.sdui.presentation.ui.composable.ComposeComponents
import com.jesusdmedinac.compose.sdui.presentation.ui.composable.ComposeNodeEditor
import com.jesusdmedinac.compose.sdui.presentation.ui.composable.ComposeNodeTree
import com.jesusdmedinac.compose.sdui.presentation.ui.composable.ComposePreview
import com.jesusdmedinac.compose.sdui.presentation.ui.composable.SplitColumn
import com.jesusdmedinac.compose.sdui.presentation.ui.composable.WindowWithPanels
import com.jesusdmedinac.jsontocompose.ComposeNode
import com.jesusdmedinac.jsontocompose.ComposeType
import io.github.vinceglb.filekit.compose.rememberFileSaverLauncher

data object MainScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val mainScreenModel = koinScreenModel<MainScreenModel>()
        val mainScreenState by mainScreenModel.state.collectAsState()
        val isLeftPanelDisplayed = mainScreenState.isLeftPanelDisplayed
        val isRightPanelDisplayed = mainScreenState.isRightPanelDisplayed
        val mainScreenSideEffect by mainScreenModel.sideEffect.collectAsState()

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

        val authScreenModel = koinScreenModel<AuthScreenModel>()
        val authState by authScreenModel.state.collectAsState()

        LaunchedEffect(selectedComposeNode) {
            mainScreenModel.onDisplayRightPanelChange(isRightPanelDisplayed = selectedComposeNode != null)
            editNodeScreenModel.onComposeNodeSelected(selectedComposeNode)
        }

        LaunchedEffect(selectedComposeNodeOnEditor) {
            mainScreenModel.onDisplayRightPanelChange(isRightPanelDisplayed = selectedComposeNodeOnEditor != null)
            composeTreeScreenModel.onComposeNodeSelected(selectedComposeNodeOnEditor)
        }

        LaunchedEffect(editNodeState) {
            editNodeState.editingComposeNode?.let { composeTreeScreenModel.saveNode(it) }
        }

        LaunchedEffect(composeNodeRoot) {
        }

        LaunchedEffect(authState) {
            when (authState) {
                is AuthState.Idle -> navigator.replace(AuthScreen)
                else -> Unit
            }
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
                authState,
                mainScreenModel,
                authScreenModel
            )
            WindowWithPanels(
                isLeftPanelDisplayed,
                onLeftPanelClosed = {
                    mainScreenModel.onDisplayLeftPanelChange(false)
                },
                isRightPanelDisplayed,
                onRightPanelClosed = {
                    mainScreenModel.onDisplayRightPanelChange(false)
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
                    composeTreeState,
                    mainScreenState,
                    mainScreenModel,
                    onLeftPanelButtonClick = {
                        mainScreenModel.onDisplayLeftPanelChange(!isLeftPanelDisplayed)
                    },
                    onRightPanelButtonClick = {
                        mainScreenModel.onDisplayRightPanelChange(!isRightPanelDisplayed)
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
    authState: AuthState,
    mainScreenBehavior: MainScreenBehavior,
    authBehavior: AuthBehavior
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
            SessionAction(
                authState = authState,
                authBehavior = authBehavior
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface,
        )
    )
}

@Composable
private fun SessionAction(
    authState: AuthState,
    authBehavior: AuthBehavior,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
    ) {
        OutlinedIconButton(
            onClick = {
                expanded = true
            },
        ) {
            Text(
                text = when (authState) {
                    is AuthState.Authenticated -> authState.user?.email?.firstOrNull().toString()
                        .uppercase()

                    else -> "?"
                },
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            DropdownMenuItem(
                onClick = {
                    authBehavior.logout()
                }
            ) {
                Text(
                    text = "Logout"
                )
            }
        }
    }
}