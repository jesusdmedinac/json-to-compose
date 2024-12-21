package com.jesusdmedinac.compose.sdui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.kotlin.fibonacci.ComposeNode
import io.github.kotlin.fibonacci.ComposeType
import org.jetbrains.compose.ui.tooling.preview.Preview

data class EditNodeScreen(
    val composeNode: ComposeNode
) : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    @Preview
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel: EditNodeScreenModel = koinScreenModel()
        val state by screenModel.state.collectAsState()
        val isComposeTypeMenuExpanded = state.isComposeTypeMenuExpanded
        val editingComposeNode = state.editingComposeNode

        LaunchedEffect(Unit) {
            screenModel.onLoad(composeNode)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1E1E1E))
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = {
                            navigator.popUntilRoot()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    TextButton(
                        onClick = {
                            navigator.popUntilRoot()
                            screenModel.onSaveNodeClick(editingComposeNode!!)
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.White,
                            disabledContentColor = Color.Gray,
                        ),
                        enabled = composeNode != editingComposeNode,
                    ) {
                        Text(
                            "Save",
                        )
                    }
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = isComposeTypeMenuExpanded,
                        onExpandedChange = { screenModel.onComposeTypeMenuExpandedChange(true) },
                    ) {
                        OutlinedTextField(
                            readOnly = true,
                            value = editingComposeNode?.type?.name ?: "",
                            onValueChange = {},
                            label = { Text("Compose type", color = Color.White) },
                            trailingIcon = {
                                Icon(
                                    Icons.Filled.ArrowDropDown,
                                    "Trailing icon for exposed dropdown menu",
                                    Modifier.rotate(if (isComposeTypeMenuExpanded) 180f else 360f),
                                    tint = Color.White
                                )
                            },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White,
                                textColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = isComposeTypeMenuExpanded,
                            onDismissRequest = { screenModel.onComposeTypeMenuExpandedChange(false) }
                        ) {
                            ComposeType.entries.forEach { type ->
                                DropdownMenuItem(
                                    onClick = {
                                        screenModel.onComposeTypeSelected(type)
                                    }
                                ) {
                                    Text(text = type.name)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}