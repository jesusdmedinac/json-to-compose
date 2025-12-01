package com.jesusdmedinac.compose.sdui.presentation.ui.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SplitColumn(
    modifier: Modifier = Modifier,
    top: @Composable ColumnScope.(Modifier) -> Unit,
    bottom: @Composable ColumnScope.(Modifier) -> Unit
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
private fun ColumnScope.Top(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.(Modifier) -> Unit
) {
    content(modifier)
}

@Composable
private fun ColumnScope.Bottom(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.(Modifier) -> Unit
) {
    content(modifier)
}