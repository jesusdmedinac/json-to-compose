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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.composables.icons.lucide.Box
import com.composables.icons.lucide.CaseSensitive
import com.composables.icons.lucide.Circle
import com.composables.icons.lucide.CirclePlus
import com.composables.icons.lucide.Columns3
import com.composables.icons.lucide.Columns4
import com.composables.icons.lucide.GalleryVertical
import com.composables.icons.lucide.Image
import com.composables.icons.lucide.Layers
import com.composables.icons.lucide.LayoutDashboard
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MessageSquare
import com.composables.icons.lucide.MousePointerClick
import com.composables.icons.lucide.PanelBottom
import com.composables.icons.lucide.PanelTop
import com.composables.icons.lucide.Plus
import com.composables.icons.lucide.Puzzle
import com.composables.icons.lucide.RectangleHorizontal
import com.composables.icons.lucide.Rows3
import com.composables.icons.lucide.Rows4
import com.composables.icons.lucide.Search
import com.composables.icons.lucide.SeparatorHorizontal
import com.composables.icons.lucide.Smile
import com.composables.icons.lucide.Square
import com.composables.icons.lucide.SquareCheck
import com.composables.icons.lucide.TextCursor
import com.composables.icons.lucide.ToggleRight
import com.composables.icons.lucide.WholeWord
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeComponentsScreenModel
import com.jesusdmedinac.jsontocompose.model.ComposeType

@Composable
fun ComposeComponents(
    modifier: Modifier = Modifier
) {
    val navigator = LocalNavigator.currentOrThrow
    val composeComponentsScreenModel: ComposeComponentsScreenModel =
        navigator.koinNavigatorScreenModel()
    val state by composeComponentsScreenModel.state.collectAsState()
    var keyword by remember { mutableStateOf(TextFieldValue("")) }
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = keyword,
            onValueChange = {
                keyword = it
                composeComponentsScreenModel.onKeywordChanged(it.text)
            },
            label = {
                Text("Find a component")
            },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Lucide.Search,
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
                        composeComponentsScreenModel.onComposeTypeClick(type)
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
        Icon(
            imageVector = when (type) {
                ComposeType.Column -> Lucide.Rows3
                ComposeType.Row -> Lucide.Columns3
                ComposeType.Box -> Lucide.Box
                ComposeType.Text -> Lucide.CaseSensitive
                ComposeType.Button -> Lucide.MousePointerClick
                ComposeType.Image -> Lucide.Image
                ComposeType.TextField -> Lucide.TextCursor
                ComposeType.LazyColumn -> Lucide.Rows4
                ComposeType.LazyRow -> Lucide.Columns4
                ComposeType.Scaffold -> Lucide.LayoutDashboard
                ComposeType.Card -> Lucide.GalleryVertical
                ComposeType.AlertDialog -> Lucide.MessageSquare
                ComposeType.Custom -> Lucide.Puzzle
                ComposeType.Spacer -> Lucide.SeparatorHorizontal
                ComposeType.TopAppBar -> Lucide.PanelTop
                ComposeType.BottomBar -> Lucide.PanelBottom
                ComposeType.BottomNavigationItem -> Lucide.Square
                ComposeType.Switch -> Lucide.ToggleRight
                ComposeType.Checkbox -> Lucide.SquareCheck
                ComposeType.Icon -> Lucide.Smile
                ComposeType.OutlinedButton -> Lucide.RectangleHorizontal
                ComposeType.TextButton -> Lucide.WholeWord
                ComposeType.ElevatedButton -> Lucide.Layers
                ComposeType.FilledTonalButton -> Lucide.RectangleHorizontal
                ComposeType.IconButton -> Lucide.Circle
                ComposeType.FloatingActionButton -> Lucide.CirclePlus
                ComposeType.ExtendedFloatingActionButton -> Lucide.Plus
            },
            contentDescription = type.name
        )
        Spacer(
            modifier = Modifier.width(8.dp)
        )
        Text(
            text = type.name,
            color = when {
                isHovered -> MaterialTheme.colorScheme.onBackground
                else -> MaterialTheme.colorScheme.onSurface
            },
        )
    }
}
