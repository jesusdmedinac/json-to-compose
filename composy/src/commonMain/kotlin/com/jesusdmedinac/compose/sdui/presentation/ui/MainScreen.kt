package com.jesusdmedinac.compose.sdui.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import com.jesusdmedinac.compose.sdui.auth.presentation.screenmodel.AuthBehavior
import com.jesusdmedinac.compose.sdui.auth.presentation.screenmodel.AuthScreenState
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.EditorIntent
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.EditorScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.EditorState
import com.jesusdmedinac.compose.sdui.presentation.ui.composable.ComposeComponents
import com.jesusdmedinac.compose.sdui.presentation.ui.composable.ComposeNodeEditor
import com.jesusdmedinac.compose.sdui.presentation.ui.composable.ComposeNodeTree
import com.jesusdmedinac.compose.sdui.presentation.ui.composable.ComposePreview
import com.jesusdmedinac.compose.sdui.presentation.ui.layouts.SplitColumn
import com.jesusdmedinac.compose.sdui.presentation.ui.layouts.WindowWithPanels
import io.github.vinceglb.filekit.compose.rememberFileSaverLauncher
import kotlinx.serialization.json.Json

data object MainScreen : Screen {
    @Composable
    override fun Content() {
        val editorScreenModel = koinScreenModel<EditorScreenModel>()
        val editorState by editorScreenModel.state.collectAsState()
        val selectedNode = editorState.selectedNode

        val launcher = rememberFileSaverLauncher { file -> }

        var showDeleteDialog by remember { mutableStateOf(false) }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Node") },
                text = { Text("Are you sure you want to delete this node? All its children will also be deleted.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (selectedNode != null) {
                                editorScreenModel.onIntent(EditorIntent.DeleteNode(selectedNode.id))
                            }
                            showDeleteDialog = false
                        }
                    ) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) { focusRequester.requestFocus() }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)
                .focusable()
                .onKeyEvent { event ->
                    if (event.type == KeyEventType.KeyUp && 
                        (event.key == Key.Delete || event.key == Key.Backspace)) {
                        
                        if (selectedNode != null && editorState.rootNode.id != selectedNode.id) {
                            if (selectedNode.children().isNotEmpty()) {
                                showDeleteDialog = true
                            } else {
                                editorScreenModel.onIntent(EditorIntent.DeleteNode(selectedNode.id))
                            }
                            true
                        } else false
                    } else false
                }
        ) {
            MainScreenLayout(
                editorState = editorState,
                onExportAsJSON = {
                    launcher.launch(
                        baseName = "composeAsJSON",
                        extension = "json",
                        initialDirectory = "",
                        bytes = Json.encodeToString(editorState.rootNode).encodeToByteArray()
                    )
                },
                isLeftPanelDisplayed = editorState.isLeftPanelDisplayed,
                isRightPanelDisplayed = editorState.isRightPanelDisplayed,
                onLeftPanelClosed = { editorScreenModel.onIntent(EditorIntent.SetLeftPanelDisplayed(false)) },
                onRightPanelClosed = { editorScreenModel.onIntent(EditorIntent.SetRightPanelDisplayed(false)) }
            )
        }
    }
}

@Composable
private fun MainScreenLayout(
    editorState: EditorState,
    onExportAsJSON: () -> Unit,
    isLeftPanelDisplayed: Boolean,
    isRightPanelDisplayed: Boolean,
    onLeftPanelClosed: () -> Unit,
    onRightPanelClosed: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        MainScreenTopAppBar(
            onExportAsJSON = onExportAsJSON,
        )
        WindowWithPanels(
            isLeftPanelDisplayed = isLeftPanelDisplayed,
            onLeftPanelClosed = onLeftPanelClosed,
            isRightPanelDisplayed = isRightPanelDisplayed,
            onRightPanelClosed = onRightPanelClosed,
            leftPanelContent = { LeftPanelContent() },
            rightPanelContent = { RightPanelContent() },
            modifier = Modifier.fillMaxSize()
        ) {
            MainPanelContent()
        }
    }
}

@Composable
private fun LeftPanelContent() {
    SplitColumn(
        modifier = Modifier.fillMaxSize(),
        top = { modifier ->
            ComposeComponents(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(8.dp)
            )
        },
        bottom = { modifier ->
            ComposeNodeTree(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(8.dp)
            )
        }
    )
}

@Composable
private fun RightPanelContent() {
    ComposeNodeEditor(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(8.dp)
    )
}

@Composable
private fun MainPanelContent() {
    ComposePreview(
        modifier = Modifier.fillMaxSize()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenTopAppBar(
    onExportAsJSON: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = "Composy") },
        actions = {
            Button(
                onClick = onExportAsJSON,
                modifier = Modifier.pointerHoverIcon(icon = PointerIcon.Hand)
            ) {
                Text(text = "Export as JSON")
            }
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
    authScreenState: AuthScreenState,
    authBehavior: AuthBehavior,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier) {
        OutlinedIconButton(onClick = { expanded = true }) {
            Text(
                text = when (authScreenState) {
                    is AuthScreenState.Authenticated -> authScreenState.user?.email?.firstOrNull().toString()
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
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(onClick = { authBehavior.logout() }) {
                Text(text = "Logout")
            }
        }
    }
}
