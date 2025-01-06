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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.composy.composy.generated.resources.Res
import com.jesusdmedinac.composy.composy.generated.resources.ic_menu
import com.jesusdmedinac.jsontocompose.ComposeNode
import com.jesusdmedinac.jsontocompose.ToCompose
import org.jetbrains.compose.resources.painterResource

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