package com.jesusdmedinac.compose.sdui.presentation.ui.screen

import androidx.compose.foundation.background
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.jesusdmedinac.compose.sdui.presentation.ui.composable.ChatPanel
import com.jesusdmedinac.compose.sdui.presentation.ui.composable.PreviewPanel
import com.jesusdmedinac.compose.sdui.presentation.ui.composable.ProjectGeneratorPanel
import com.jesusdmedinac.compose.sdui.presentation.ui.layouts.WindowWithPanels
import com.jesusdmedinac.composy.composy.generated.resources.Res
import com.jesusdmedinac.composy.composy.generated.resources.ic_menu
import org.jetbrains.compose.resources.painterResource

object ChatScreen : Screen {
    @Composable
    override fun Content() {
        var isLeftPanelDisplayed by remember { mutableStateOf(false) }
        var isRightPanelDisplayed by remember { mutableStateOf(false) }

        ChatScreenLayout(
            isLeftPanelDisplayed = isLeftPanelDisplayed,
            onLeftPanelToggle = { isLeftPanelDisplayed = !isLeftPanelDisplayed },
            isRightPanelDisplayed = isRightPanelDisplayed,
            onRightPanelToggle = { isRightPanelDisplayed = !isRightPanelDisplayed }
        )
    }
}

@Composable
private fun ChatScreenLayout(
    isLeftPanelDisplayed: Boolean,
    onLeftPanelToggle: () -> Unit,
    isRightPanelDisplayed: Boolean,
    onRightPanelToggle: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        ChatScreenTopAppBar()
        WindowWithPanels(
            isLeftPanelDisplayed = isLeftPanelDisplayed,
            onLeftPanelClosed = onLeftPanelToggle,
            isRightPanelDisplayed = isRightPanelDisplayed,
            onRightPanelClosed = onRightPanelToggle,
            leftPanelContent = {
                ProjectGeneratorPanel(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(16.dp)
                )
            },
            rightPanelContent = {
                PreviewPanel(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(16.dp)
                )
            },
            modifier = Modifier.fillMaxSize()
        ) {
            ChatScreenMainContent(
                onLeftPanelToggle = onLeftPanelToggle,
                onRightPanelToggle = onRightPanelToggle
            )
        }
    }
}

@Composable
private fun ChatScreenMainContent(
    onLeftPanelToggle: () -> Unit,
    onRightPanelToggle: () -> Unit
) {
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)) {
        PanelControlHeader(
            onLeftPanelToggle = onLeftPanelToggle,
            onRightPanelToggle = onRightPanelToggle
        )
        ChatPanel(modifier = Modifier.fillMaxSize())
    }
}

@Composable
private fun PanelControlHeader(
    onLeftPanelToggle: () -> Unit,
    onRightPanelToggle: () -> Unit
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
            onClick = onLeftPanelToggle,
            modifier = Modifier.pointerHoverIcon(icon = PointerIcon.Hand)
        ) {
            Icon(
                painterResource(Res.drawable.ic_menu),
                contentDescription = "Open left panel",
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = onRightPanelToggle,
            modifier = Modifier.pointerHoverIcon(icon = PointerIcon.Hand)
        ) {
            Icon(
                painterResource(Res.drawable.ic_menu),
                contentDescription = "Open right panel",
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatScreenTopAppBar() {
    TopAppBar(
        title = {
            Text(text = "Composy")
        },
        actions = {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface,
        )
    )
}