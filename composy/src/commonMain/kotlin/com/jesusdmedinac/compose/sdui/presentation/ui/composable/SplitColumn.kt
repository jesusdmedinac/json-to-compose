package com.jesusdmedinac.compose.sdui.presentation.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SplitColumn(
    modifier: Modifier = Modifier,
    top: @Composable (Modifier) -> Unit,
    bottom: @Composable (Modifier) -> Unit
) {
    Column(
        modifier = modifier,
    ) {
        Top(
            modifier = modifier
                .weight(1f),
            content = top,
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onSurface
        )
        Bottom(
            modifier = modifier
                .weight(1f),
            content = bottom,
        )
    }
}

@Composable
private fun Top(
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit
) {
    content(modifier)
}

@Composable
private fun Bottom(
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit
) {
    content(modifier)
}