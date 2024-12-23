package com.jesusdmedinac.compose.sdui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import io.github.kotlin.fibonacci.ComposeNode
import io.github.kotlin.fibonacci.ComposeType
import io.github.kotlin.fibonacci.ToCompose
import json_to_compose.composy.generated.resources.Res
import json_to_compose.composy.generated.resources.ic_menu
import org.jetbrains.compose.resources.painterResource

data object MainScreen : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<MainScreenModel>()
        val state by screenModel.state.collectAsState()
        val isLeftPanelDisplayed = state.isLeftPanelDisplayed
        val isRightPanelDisplayed = state.isRightPanelDisplayed

        val composeEditorScreenModel = koinScreenModel<ComposeEditorScreenModel>()
        val composeEditorState by composeEditorScreenModel.state.collectAsState()
        val composeNodeRoot = composeEditorState.composeNodeRoot
        val composeEditorSideEffect by composeEditorScreenModel.sideEffect.collectAsState()

        val editNodeScreenModel = koinScreenModel<EditNodeScreenModel>()
        val editNodeState by editNodeScreenModel.state.collectAsState()
        val editNodeSideEffect by editNodeScreenModel.sideEffect.collectAsState()

        LaunchedEffect(composeEditorSideEffect) {
            when (val sideEffect = composeEditorSideEffect) {
                ComposeEditorSideEffect.Idle -> Unit
                is ComposeEditorSideEffect.DisplayEditNodeDialog -> {
                    editNodeScreenModel.onComposeNodeSelected(sideEffect.composeNode)
                }
            }
        }

        LaunchedEffect(editNodeSideEffect) {
            when (val sideEffect = editNodeSideEffect) {
                EditNodeSideEffect.Idle -> Unit
                is EditNodeSideEffect.SaveNode -> {
                    composeEditorScreenModel.saveNode(sideEffect.composeNode)
                }
            }
        }

        MaterialTheme {
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
                        composeEditorScreenModel,
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
                    onMenuClick = {
                        screenModel.onDisplayLeftPanelChange(!isLeftPanelDisplayed)
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
    onMenuClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        IconButton(onClick = {
            onMenuClick()
        }) {
            Icon(
                painterResource(Res.drawable.ic_menu),
                contentDescription = null,
                tint = Color.White
            )
        }
        composeNodeRoot.ToCompose(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        )
    }
}