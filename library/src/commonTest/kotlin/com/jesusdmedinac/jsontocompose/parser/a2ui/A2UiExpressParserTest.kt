package com.jesusdmedinac.jsontocompose.parser.a2ui

import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class A2UiExpressParserTest {

    @Test
    fun shouldParseStandardLinearComponents() {
        val snippet = """
            header = Text(text="Bienvenido")
            root = Column(children=[header])
        """.trimIndent()

        val parser = A2UiExpressParser()
        val rootNode = parser.parse(snippet)

        assertNotNull(rootNode)
        assertEquals(ComposeType.Column, rootNode.type)
        val columnProps = rootNode.properties as? NodeProperties.ColumnProps
        assertNotNull(columnProps)
        val children = columnProps.children
        assertNotNull(children)
        assertEquals(1, children.size)

        val textChild = children.first()
        assertEquals(ComposeType.Text, textChild.type)
        val textProps = textChild.properties as? NodeProperties.TextProps
        assertNotNull(textProps)
        assertEquals("Bienvenido", textProps.text)
    }

    @Test
    fun shouldParseStandardLinearComponentsWithPositionalArgs() {
        val snippet = """
            header = Text("Bienvenido")
            root = Column([header])
        """.trimIndent()

        val parser = A2UiExpressParser()
        val rootNode = parser.parse(snippet)

        assertNotNull(rootNode)
        assertEquals(ComposeType.Column, rootNode.type)
        val columnProps = rootNode.properties as? NodeProperties.ColumnProps
        assertNotNull(columnProps)
        val children = columnProps.children
        assertNotNull(children)
        assertEquals(1, children.size)

        val textChild = children.first()
        assertEquals(ComposeType.Text, textChild.type)
        val textProps = textChild.properties as? NodeProperties.TextProps
        assertNotNull(textProps)
        assertEquals("Bienvenido", textProps.text)
    }
}
