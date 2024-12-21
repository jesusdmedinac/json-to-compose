package com.jesusdmedinac.compose.sdui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import io.github.kotlin.fibonacci.ComposeNode
import io.github.kotlin.fibonacci.ToCompose
import json_to_compose.composy.generated.resources.Res
import json_to_compose.composy.generated.resources.ic_menu
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterialApi::class)
data object MainScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<MainScreenModel>()
        val composeEditorScreenModel = koinScreenModel<ComposeEditorScreenModel>()
        val state by screenModel.state.collectAsState()
        val isLeftPanelDisplayed = state.isLeftPanelDisplayed
        val composeEditorState by composeEditorScreenModel.state.collectAsState()
        val composeNodeRoot = composeEditorState.composeNodeRoot

        LaunchedEffect(composeEditorState) {
            println(composeNodeRoot)
        }

        var addNewNodeMenuDisplayed by remember { mutableStateOf(false) }
        var addNewNodeLayoutMenuDisplayed by remember { mutableStateOf(false) }

        fun addNewNode(composeNode: ComposeNode) {
            addNewNodeMenuDisplayed = false
            addNewNodeLayoutMenuDisplayed = false
            // composeNodeChildren = composeNodeChildren + composeNode
        }
        MaterialTheme {
            val animatedLeftSideOffsetDp by animateDpAsState(if (isLeftPanelDisplayed) 0.dp else (-384).dp)
            val animatedLeftSideSpacerDp by animateDpAsState(if (isLeftPanelDisplayed) 384.dp else 0.dp)
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                ComposeNodeTree(
                    composeNodeRoot,
                    composeEditorScreenModel,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(384.dp)
                        .offset(x = animatedLeftSideOffsetDp)
                        .background(Color(0xFF2C2C2C))
                )
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.width(animatedLeftSideSpacerDp))
                    ComposePreview(
                        composeNodeRoot,
                        onMenuClick = {
                            screenModel.onDisplayLeftPanelClick()
                        },
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(3f)
                            .background(Color(0xFF1E1E1E))
                    )
                }
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