package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.ComposeType.BottomBar
import com.jesusdmedinac.jsontocompose.model.ComposeType.Box
import com.jesusdmedinac.jsontocompose.model.ComposeType.Button
import com.jesusdmedinac.jsontocompose.model.ComposeType.Card
import com.jesusdmedinac.jsontocompose.model.ComposeType.Column
import com.jesusdmedinac.jsontocompose.model.ComposeType.ElevatedButton
import com.jesusdmedinac.jsontocompose.model.ComposeType.ElevatedCard
import com.jesusdmedinac.jsontocompose.model.ComposeType.FilledTonalButton
import com.jesusdmedinac.jsontocompose.model.ComposeType.FloatingActionButton
import com.jesusdmedinac.jsontocompose.model.ComposeType.FlowColumn
import com.jesusdmedinac.jsontocompose.model.ComposeType.FlowRow
import com.jesusdmedinac.jsontocompose.model.ComposeType.IconButton
import com.jesusdmedinac.jsontocompose.model.ComposeType.LazyColumn
import com.jesusdmedinac.jsontocompose.model.ComposeType.LazyRow
import com.jesusdmedinac.jsontocompose.model.ComposeType.OutlinedButton
import com.jesusdmedinac.jsontocompose.model.ComposeType.OutlinedCard
import com.jesusdmedinac.jsontocompose.model.ComposeType.Row
import com.jesusdmedinac.jsontocompose.model.ComposeType.Scaffold
import com.jesusdmedinac.jsontocompose.model.ComposeType.ScrollableTabRow
import com.jesusdmedinac.jsontocompose.model.ComposeType.Surface
import com.jesusdmedinac.jsontocompose.model.ComposeType.Tab
import com.jesusdmedinac.jsontocompose.model.ComposeType.TabRow
import com.jesusdmedinac.jsontocompose.model.ComposeType.TextButton
import com.jesusdmedinac.jsontocompose.model.NodeProperties

/**
 * Creates a default, non-null properties instance for this component type.
 * Optionally preserves existing children when changing a node's type in the editor,
 * otherwise delegates to the core library's [ComposeType.createDefaultProperties].
 */
fun ComposeType.createDefaultProperties(
    preservedChildren: List<ComposeNode>? = null
): NodeProperties = when {
    preservedChildren == null -> createDefaultProperties()
    else -> when (this) {
        Column, LazyColumn -> NodeProperties.ColumnProps(children = preservedChildren)
        Row, LazyRow -> NodeProperties.RowProps(children = preservedChildren)
        Box -> NodeProperties.BoxProps(children = preservedChildren)
        Button, OutlinedButton, TextButton, ElevatedButton, FilledTonalButton, IconButton -> NodeProperties.ButtonProps(child = preservedChildren.firstOrNull())
        FloatingActionButton -> NodeProperties.FabProps(icon = preservedChildren.firstOrNull())
        Scaffold -> NodeProperties.ScaffoldProps(child = preservedChildren.firstOrNull())
        Card, ElevatedCard -> NodeProperties.CardProps(child = preservedChildren.firstOrNull())
        OutlinedCard -> NodeProperties.OutlinedCardProps(child = preservedChildren.firstOrNull())
        TabRow, ScrollableTabRow -> NodeProperties.TabRowProps(children = preservedChildren)
        Tab -> NodeProperties.TabProps(text = preservedChildren.firstOrNull())
        BottomBar -> NodeProperties.BottomBarProps(children = preservedChildren)
        FlowRow -> NodeProperties.FlowRowProps(children = preservedChildren)
        FlowColumn -> NodeProperties.FlowColumnProps(children = preservedChildren)
        Surface -> NodeProperties.SurfaceProps(child = preservedChildren.firstOrNull())
        else -> createDefaultProperties()
    }
}