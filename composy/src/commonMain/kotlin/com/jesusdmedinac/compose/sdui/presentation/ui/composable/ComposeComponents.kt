package com.jesusdmedinac.compose.sdui.presentation.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeComponentsScreenModel
import com.jesusdmedinac.jsontocompose.ComposeType

@Composable
fun ComposeComponents(
    screenModel: ComposeComponentsScreenModel,
    modifier: Modifier = Modifier
) {
    val state by screenModel.state.collectAsState()
    var keyword by remember { mutableStateOf(TextFieldValue("")) }
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = keyword,
            onValueChange = {
                keyword = it
                screenModel.onKeywordChanged(it.text)
            },
            label = {
                Text("Find a component")
            },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Find a component"
                )
            }
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(ComposeType.entries.filter {
                it.name.contains(state.keyword)
            }) { type ->
                ComposeComponent(
                    type,
                    onClick = {
                        screenModel.onComposeTypeClick(type)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ComposeComponent(
    type: ComposeType,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isHovered by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            )
            .hoverable(
                interactionSource = interactionSource,
                enabled = true,
            )
            .pointerHoverIcon(
                icon = PointerIcon.Hand
            )
            .onPointerEvent(
                PointerEventType.Enter,
                onEvent = {
                    isHovered = true
                }
            )
            .onPointerEvent(
                PointerEventType.Exit,
                onEvent = {
                    isHovered = false
                }
            )
            .then(
                if (isHovered) {
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                } else {
                    Modifier
                }
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = type.name,
            color = when {
                isHovered -> MaterialTheme.colorScheme.background
                else -> MaterialTheme.colorScheme.onBackground
            },
        )
    }
}
