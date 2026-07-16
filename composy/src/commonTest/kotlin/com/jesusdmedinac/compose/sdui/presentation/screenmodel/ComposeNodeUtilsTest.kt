package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ComposeNodeUtilsTest {

    private fun textNode() = ComposeNode(
        type = ComposeType.Text,
        properties = NodeProperties.TextProps(text = "Text")
    )

    @Test
    fun compatibleTypes_scaffoldWithChild_excludesLeafTypes() {
        val scaffold = ComposeNode(
            type = ComposeType.Scaffold,
            properties = NodeProperties.ScaffoldProps(child = textNode())
        )

        val compatible = scaffold.compatibleTypes()

        assertFalse(ComposeType.Text in compatible)
        assertTrue(ComposeType.Column in compatible)
        assertTrue(ComposeType.Card in compatible)
    }

    @Test
    fun compatibleTypes_emptyScaffold_includesAllTypes() {
        val scaffold = ComposeNode(
            type = ComposeType.Scaffold,
            properties = NodeProperties.ScaffoldProps()
        )

        assertEquals(ComposeType.entries.toList(), scaffold.compatibleTypes())
    }

    @Test
    fun compatibleTypes_columnWithTwoChildren_excludesSingleChildContainers() {
        val column = ComposeNode(
            type = ComposeType.Column,
            properties = NodeProperties.ColumnProps(children = listOf(textNode(), textNode()))
        )

        val compatible = column.compatibleTypes()

        assertFalse(ComposeType.Button in compatible)
        assertFalse(ComposeType.Text in compatible)
        assertTrue(ComposeType.Row in compatible)
        assertTrue(ComposeType.Box in compatible)
    }
}
