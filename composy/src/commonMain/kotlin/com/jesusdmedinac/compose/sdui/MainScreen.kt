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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
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
import io.github.kotlin.fibonacci.ComposeType
import io.github.kotlin.fibonacci.ToCompose
import json_to_compose.composy.generated.resources.Res
import json_to_compose.composy.generated.resources.ic_menu
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterialApi::class)
data object MainScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<MainScreenModel>()
        val state by screenModel.state.collectAsState()
        val isLeftPanelDisplayed = state.isLeftPanelDisplayed

        var composeNodeChildren by remember { mutableStateOf(emptyList<ComposeNode>()) }

        var addNewNodeMenuDisplayed by remember { mutableStateOf(false) }
        var addNewNodeLayoutMenuDisplayed by remember { mutableStateOf(false) }

        var addNewSubNodeMenuDisplayed by remember { mutableStateOf(false) }
        var addNewSubNodeLayoutMenuDisplayed by remember { mutableStateOf(false) }

        val composeNodeRoot = ComposeNode(
            ComposeType.Layout.Column,
            children = composeNodeChildren
        )

        fun addNewNode(composeNode: ComposeNode) {
            addNewNodeMenuDisplayed = false
            addNewNodeLayoutMenuDisplayed = false
            composeNodeChildren = composeNodeChildren + composeNode
        }
        fun addNewSubNode(composeNode: ComposeNode, composeChild: ComposeNode) {
            addNewSubNodeMenuDisplayed = false
            addNewSubNodeLayoutMenuDisplayed = false
            composeNodeChildren = composeNodeChildren.map {
                if (it == composeNode) {
                    it.copy(children = it.children?.plus(composeChild))
                } else {
                    it
                }
            }
        }
        MaterialTheme {
            val animatedLeftSideOffsetDp by animateDpAsState(if (isLeftPanelDisplayed) 0.dp else (-256).dp)
            val animatedLeftSideSpacerDp by animateDpAsState(if (isLeftPanelDisplayed) 256.dp else 0.dp)
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(256.dp)
                        .offset(x = animatedLeftSideOffsetDp)
                        .background(Color(0xFF2C2C2C))
                ) {
                    composeNodeRoot.children?.let {
                        items(it) { node ->
                            ListItem(
                                overlineText = {
                                    Text(node.type::class.simpleName ?: "", color = Color.White)
                                },
                                text = { Text(node.text ?: "", color = Color.White) },
                                secondaryText = {
                                    if (node.type is ComposeType.Layout) {
                                        Box {
                                            TextButton(
                                                onClick = {
                                                    addNewSubNodeMenuDisplayed = true
                                                },
                                                colors = ButtonDefaults.textButtonColors(
                                                    contentColor = Color.White
                                                )
                                            ) {
                                                Icon(Icons.Default.Add, contentDescription = null)
                                                Text("Agregar sub nodo")
                                            }
                                            DropdownMenu(
                                                expanded = addNewSubNodeMenuDisplayed,
                                                onDismissRequest = {
                                                    addNewSubNodeMenuDisplayed = false
                                                },
                                            ) {
                                                DropdownMenuItem(onClick = {
                                                    addNewSubNodeLayoutMenuDisplayed = true
                                                }) {
                                                    Box {
                                                        Text("Layout")
                                                        DropdownMenu(
                                                            expanded = addNewSubNodeLayoutMenuDisplayed,
                                                            onDismissRequest = {
                                                                addNewSubNodeLayoutMenuDisplayed = false
                                                            }
                                                        ) {
                                                            DropdownMenuItem(onClick = {
                                                                addNewSubNode(
                                                                    node,
                                                                    ComposeNode(
                                                                        ComposeType.Layout.Column
                                                                    )
                                                                )
                                                            }) {
                                                                Text("Column")
                                                            }
                                                            DropdownMenuItem(onClick = {
                                                                addNewSubNode(
                                                                    node,
                                                                    ComposeNode(
                                                                        ComposeType.Layout.Row
                                                                    )
                                                                )
                                                            }) {
                                                                Text("Row")
                                                            }
                                                            DropdownMenuItem(onClick = {
                                                                addNewSubNode(
                                                                    node,
                                                                    ComposeNode(
                                                                        ComposeType.Layout.Box
                                                                    )
                                                                )
                                                            }) {
                                                                Text("Box")
                                                            }
                                                        }
                                                    }
                                                }
                                                DropdownMenuItem(onClick = {
                                                    addNewSubNode(
                                                        node,
                                                        ComposeNode(
                                                            ComposeType.Text,
                                                            text = "Text"
                                                        )
                                                    )
                                                }) {
                                                    Text("Text")
                                                }
                                                DropdownMenuItem(onClick = {
                                                    addNewSubNode(
                                                        node,
                                                        ComposeNode(
                                                            ComposeType.Button,
                                                            child = ComposeNode(
                                                                ComposeType.Text,
                                                                text = "Button"
                                                            )
                                                        )
                                                    )
                                                }) {
                                                    Text("Button")
                                                }
                                            }
                                        }
                                    }
                                },
                                icon = {
                                    IconButton(onClick = {}) {
                                        Icon(
                                            Icons.Default.Edit,
                                            contentDescription = null,
                                            tint = Color.White,
                                        )
                                    }
                                },
                                trailing = {
                                    IconButton(onClick = {
                                        composeNodeChildren =
                                            composeNodeChildren.filter { child -> child != node }
                                    }) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = null,
                                            tint = Color.White,
                                        )
                                    }
                                },
                            )
                        }
                        item {
                            Box {
                                TextButton(
                                    onClick = {
                                        addNewNodeMenuDisplayed = true
                                    },
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = Color.White
                                    )
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null)
                                    Text("Agregar nodo")
                                }
                                DropdownMenu(
                                    expanded = addNewNodeMenuDisplayed,
                                    onDismissRequest = {
                                        addNewNodeMenuDisplayed = false
                                    }
                                ) {
                                    DropdownMenuItem(onClick = {
                                        addNewNodeLayoutMenuDisplayed = true
                                    }) {
                                        Box {
                                            Text("Layout")
                                            DropdownMenu(
                                                expanded = addNewNodeLayoutMenuDisplayed,
                                                onDismissRequest = {
                                                    addNewNodeLayoutMenuDisplayed = false
                                                }
                                            ) {
                                                DropdownMenuItem(onClick = {
                                                    addNewNode(
                                                        ComposeNode(
                                                            ComposeType.Layout.Column
                                                        )
                                                    )
                                                }) {
                                                    Text("Column")
                                                }
                                                DropdownMenuItem(onClick = {
                                                    addNewNode(
                                                        ComposeNode(
                                                            ComposeType.Layout.Row
                                                        )
                                                    )
                                                }) {
                                                    Text("Row")
                                                }
                                                DropdownMenuItem(onClick = {
                                                    addNewNode(
                                                        ComposeNode(
                                                            ComposeType.Layout.Box
                                                        )
                                                    )
                                                }) {
                                                    Text("Box")
                                                }
                                            }
                                        }
                                    }
                                    DropdownMenuItem(onClick = {
                                        addNewNode(
                                            ComposeNode(
                                                ComposeType.Text,
                                                text = "Text"
                                            )
                                        )
                                    }) {
                                        Text("Text")
                                    }
                                    DropdownMenuItem(onClick = {
                                        addNewNode(
                                            ComposeNode(
                                                ComposeType.Button,
                                                child = ComposeNode(
                                                    ComposeType.Text,
                                                    text = "Button"
                                                )
                                            )
                                        )
                                    }) {
                                        Text("Button")
                                    }
                                }
                            }
                        }
                    }
                }
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