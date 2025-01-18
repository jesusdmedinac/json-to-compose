package com.jesusdmedinac.compose.sdui.presentation.ui.layouts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun IconTabBar(
    selectedIndex: Int = 0,
    options: List<SelectionOption>,
) {
    Surface(
        modifier = Modifier
            .width((options.size * 40).dp)
            .height(40.dp)
            .clip(MaterialTheme.shapes.small),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .width((options.size * 40).dp)
                .height(40.dp)
        ) {
            Surface(
                modifier = Modifier
                    .offset(x = (selectedIndex * 40).dp)
                    .size(40.dp)
                    .clip(MaterialTheme.shapes.small)
                    .align(Alignment.CenterStart)
                ,
                tonalElevation = 8.dp,
                shadowElevation = 8.dp
            ) {
            }
            options.forEachIndexed { index, option ->
                FilledIconButton(
                    onClick = {
                        option.onClick()
                    },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .offset(x = ((index * 40) - 4).dp),
                    shape = MaterialTheme.shapes.small,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = Color.Transparent,
                    ),
                ) {
                    option.icon()
                }
            }
        }
    }
}

class SelectionOption(
    val id: String = "",
    val onClick: () -> Unit = {},
    val icon: @Composable () -> Unit,
)