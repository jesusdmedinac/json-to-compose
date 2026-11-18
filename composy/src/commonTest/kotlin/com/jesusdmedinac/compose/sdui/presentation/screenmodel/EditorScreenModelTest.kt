package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import com.jesusdmedinac.jsontocompose.modifier.ModifierOperation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlin.test.assertNotNull

class EditorScreenModelTest {

    @Test
    fun testUpdateNodeType_preservesChildrenIfCompatible() {
        val screenModel = EditorScreenModel()
        
        // Root is Box. We add a Column.
        screenModel.onIntent(EditorIntent.AddNode(screenModel.state.value.rootNode.id, ComposeType.Column))
        val columnNode = screenModel.state.value.rootNode.children().first()
        
        // Add a Text child to the Column
        screenModel.onIntent(EditorIntent.AddNode(columnNode.id, ComposeType.Text))
        
        // Verify state
        val updatedColumn = screenModel.state.value.rootNode.children().first()
        assertEquals(ComposeType.Column, updatedColumn.type)
        assertEquals(1, updatedColumn.children().size)
        
        // Now dispatch UpdateNodeType to change Column to Row
        screenModel.onIntent(EditorIntent.UpdateNodeType(updatedColumn.id, ComposeType.Row))
        
        val rowNode = screenModel.state.value.rootNode.children().first()
        assertEquals(ComposeType.Row, rowNode.type)
        assertIs<NodeProperties.RowProps>(rowNode.properties)
        // Children should be preserved
        assertEquals(1, rowNode.children().size)
        assertEquals(ComposeType.Text, rowNode.children().first().type)
    }

    @Test
    fun testUpdateNodeText() {
        val screenModel = EditorScreenModel()
        
        // Add a Text node
        screenModel.onIntent(EditorIntent.AddNode(screenModel.state.value.rootNode.id, ComposeType.Text))
        val textNodeId = screenModel.state.value.rootNode.children().first().id
        
        // Update its text
        screenModel.onIntent(EditorIntent.UpdateNodeText(textNodeId, "Hello MVI"))
        
        val updatedTextNode = screenModel.state.value.rootNode.children().first()
        val props = updatedTextNode.properties
        assertIs<NodeProperties.TextProps>(props)
        assertEquals("Hello MVI", props.text)
    }

    @Test
    fun testModifiersCRUD() {
        val screenModel = EditorScreenModel()
        val rootId = screenModel.state.value.rootNode.id
        
        // ADD
        screenModel.onIntent(EditorIntent.AddModifier(rootId, ModifierOperation.Padding))
        
        var ops = screenModel.state.value.rootNode.composeModifier.operations
        assertEquals(1, ops.size)
        assertIs<ComposeModifier.Operation.Padding>(ops[0])
        assertEquals(0, (ops[0] as ComposeModifier.Operation.Padding).value)
        
        // UPDATE
        val updatedOp = ComposeModifier.Operation.Padding(32)
        screenModel.onIntent(EditorIntent.UpdateModifier(rootId, 0, updatedOp))
        
        ops = screenModel.state.value.rootNode.composeModifier.operations
        assertEquals(1, ops.size)
        assertEquals(32, (ops[0] as ComposeModifier.Operation.Padding).value)
        
        // DELETE
        screenModel.onIntent(EditorIntent.DeleteModifier(rootId, 0))
        
        ops = screenModel.state.value.rootNode.composeModifier.operations
        assertTrue(ops.isEmpty())
    }

    @Test
    fun testUpdateModifier_outOfBoundsIndex_keepsRootNodeInstance() {
        val screenModel = EditorScreenModel()
        val rootId = screenModel.state.value.rootNode.id
        screenModel.onIntent(EditorIntent.AddModifier(rootId, ModifierOperation.Padding))
        val rootBefore = screenModel.state.value.rootNode

        screenModel.onIntent(EditorIntent.UpdateModifier(rootId, 5, ComposeModifier.Operation.Padding(32)))

        assertSame(rootBefore, screenModel.state.value.rootNode)
    }

    @Test
    fun testDeleteModifier_outOfBoundsIndex_keepsRootNodeInstance() {
        val screenModel = EditorScreenModel()
        val rootId = screenModel.state.value.rootNode.id
        screenModel.onIntent(EditorIntent.AddModifier(rootId, ModifierOperation.Padding))
        val rootBefore = screenModel.state.value.rootNode

        screenModel.onIntent(EditorIntent.DeleteModifier(rootId, 5))

        assertSame(rootBefore, screenModel.state.value.rootNode)
    }

    @Test
    fun testUpdateNodeType_incompatibleType_keepsRootNodeInstance() {
        val screenModel = EditorScreenModel()
        val rootId = screenModel.state.value.rootNode.id
        screenModel.onIntent(EditorIntent.AddNode(rootId, ComposeType.Column))
        val columnId = screenModel.state.value.rootNode.children().first().id
        screenModel.onIntent(EditorIntent.AddNode(columnId, ComposeType.Text))
        screenModel.onIntent(EditorIntent.AddNode(columnId, ComposeType.Text))
        val rootBefore = screenModel.state.value.rootNode

        // A Column with two children cannot become a single-child Button
        screenModel.onIntent(EditorIntent.UpdateNodeType(columnId, ComposeType.Button))

        assertSame(rootBefore, screenModel.state.value.rootNode)
    }

    @Test
    fun testUpdateNodeType_emptyNode_convertsToAnyType() {
        val screenModel = EditorScreenModel()
        val rootId = screenModel.state.value.rootNode.id
        screenModel.onIntent(EditorIntent.AddNode(rootId, ComposeType.Column))
        val columnId = screenModel.state.value.rootNode.children().first().id

        // A childless Column may become a leaf type
        screenModel.onIntent(EditorIntent.UpdateNodeType(columnId, ComposeType.Text))

        val converted = screenModel.state.value.rootNode.children().first()
        assertEquals(ComposeType.Text, converted.type)
        assertIs<NodeProperties.TextProps>(converted.properties)
    }

    @Test
    fun testUpdateNodeText_nonTextNode_keepsRootNodeInstance() {
        val screenModel = EditorScreenModel()
        val rootId = screenModel.state.value.rootNode.id
        screenModel.onIntent(EditorIntent.AddNode(rootId, ComposeType.Column))
        val columnId = screenModel.state.value.rootNode.children().first().id
        val rootBefore = screenModel.state.value.rootNode

        screenModel.onIntent(EditorIntent.UpdateNodeText(columnId, "ignored"))

        assertSame(rootBefore, screenModel.state.value.rootNode)
    }

    @Test
    fun testUpdateNodeRecursive_untouchedSiblingsKeepIdentity() {
        val screenModel = EditorScreenModel()
        val rootId = screenModel.state.value.rootNode.id
        screenModel.onIntent(EditorIntent.AddNode(rootId, ComposeType.Text))
        screenModel.onIntent(EditorIntent.AddNode(rootId, ComposeType.Text))
        val childrenBefore = screenModel.state.value.rootNode.children()
        val targetId = childrenBefore[0].id
        val untouchedSibling = childrenBefore[1]

        screenModel.onIntent(EditorIntent.UpdateNodeText(targetId, "Changed"))

        val childrenAfter = screenModel.state.value.rootNode.children()
        val targetProps = childrenAfter[0].properties
        assertIs<NodeProperties.TextProps>(targetProps)
        assertEquals("Changed", targetProps.text)
        assertSame(untouchedSibling, childrenAfter[1])
    }

    @Test
    fun testSetLeftPanelDisplayed_updatesFlagIndependently() {
        val screenModel = EditorScreenModel()
        assertTrue(screenModel.state.value.isLeftPanelDisplayed)
        val rightBefore = screenModel.state.value.isRightPanelDisplayed

        screenModel.onIntent(EditorIntent.SetLeftPanelDisplayed(false))
        assertFalse(screenModel.state.value.isLeftPanelDisplayed)
        assertEquals(rightBefore, screenModel.state.value.isRightPanelDisplayed)

        screenModel.onIntent(EditorIntent.SetLeftPanelDisplayed(true))
        assertTrue(screenModel.state.value.isLeftPanelDisplayed)
        assertEquals(rightBefore, screenModel.state.value.isRightPanelDisplayed)
    }

    @Test
    fun testSetRightPanelDisplayed_updatesFlagIndependently() {
        val screenModel = EditorScreenModel()
        assertTrue(screenModel.state.value.isRightPanelDisplayed)
        val leftBefore = screenModel.state.value.isLeftPanelDisplayed

        screenModel.onIntent(EditorIntent.SetRightPanelDisplayed(false))
        assertFalse(screenModel.state.value.isRightPanelDisplayed)
        assertEquals(leftBefore, screenModel.state.value.isLeftPanelDisplayed)

        screenModel.onIntent(EditorIntent.SetRightPanelDisplayed(true))
        assertTrue(screenModel.state.value.isRightPanelDisplayed)
        assertEquals(leftBefore, screenModel.state.value.isLeftPanelDisplayed)
    }

    @Test
    fun testSetDeviceType_updatesDeviceTypeAndComputesSize() {
        val screenModel = EditorScreenModel()
        assertEquals(DeviceType.Smartphone, screenModel.state.value.deviceType)
        assertEquals(DeviceOrientation.Portrait, screenModel.state.value.deviceOrientation)
        assertEquals(DeviceSize(412, 917), screenModel.state.value.deviceSize)

        screenModel.onIntent(EditorIntent.SetDeviceType(DeviceType.Tablet))
        assertEquals(DeviceType.Tablet, screenModel.state.value.deviceType)
        assertEquals(DeviceSize(800, 1280), screenModel.state.value.deviceSize)

        screenModel.onIntent(EditorIntent.SetDeviceType(DeviceType.Desktop))
        assertEquals(DeviceType.Desktop, screenModel.state.value.deviceType)
        assertEquals(DeviceSize(1024, 1440), screenModel.state.value.deviceSize)
    }

    @Test
    fun testSetDeviceOrientation_updatesOrientationAndComputesSize() {
        val screenModel = EditorScreenModel()

        screenModel.onIntent(EditorIntent.SetDeviceOrientation(DeviceOrientation.Landscape))
        assertEquals(DeviceOrientation.Landscape, screenModel.state.value.deviceOrientation)
        assertEquals(DeviceSize(917, 412), screenModel.state.value.deviceSize)

        screenModel.onIntent(EditorIntent.SetDeviceType(DeviceType.Tablet))
        assertEquals(DeviceSize(1280, 800), screenModel.state.value.deviceSize)

        screenModel.onIntent(EditorIntent.SetDeviceType(DeviceType.Desktop))
        assertEquals(DeviceSize(1440, 1024), screenModel.state.value.deviceSize)

        screenModel.onIntent(EditorIntent.SetDeviceOrientation(DeviceOrientation.Portrait))
        assertEquals(DeviceOrientation.Portrait, screenModel.state.value.deviceOrientation)
        assertEquals(DeviceSize(1024, 1440), screenModel.state.value.deviceSize)
    }

    @Test
    fun testSetSearchKeyword_filtersComposeTypesCaseInsensitively() {
        val screenModel = EditorScreenModel()
        assertEquals("", screenModel.state.value.searchKeyword)
        assertEquals(ComposeType.entries, screenModel.state.value.filteredComposeTypes)

        // Exact case match
        screenModel.onIntent(EditorIntent.SetSearchKeyword("Column"))
        assertEquals("Column", screenModel.state.value.searchKeyword)
        val columnMatches = screenModel.state.value.filteredComposeTypes
        assertTrue(columnMatches.contains(ComposeType.Column))
        assertTrue(columnMatches.contains(ComposeType.LazyColumn))
        assertTrue(columnMatches.contains(ComposeType.FlowColumn))
        assertTrue(columnMatches.all { it.name.contains("Column", ignoreCase = true) })

        // Case-insensitive match
        screenModel.onIntent(EditorIntent.SetSearchKeyword("text"))
        val textMatches = screenModel.state.value.filteredComposeTypes
        assertTrue(textMatches.contains(ComposeType.Text))
        assertTrue(textMatches.contains(ComposeType.TextField))
        assertTrue(textMatches.contains(ComposeType.OutlinedTextField))
        assertTrue(textMatches.all { it.name.contains("text", ignoreCase = true) })

        // Query with surrounding whitespace is trimmed
        screenModel.onIntent(EditorIntent.SetSearchKeyword("  Button  "))
        val buttonMatches = screenModel.state.value.filteredComposeTypes
        assertTrue(buttonMatches.contains(ComposeType.Button))
        assertTrue(buttonMatches.contains(ComposeType.IconButton))
        assertTrue(buttonMatches.all { it.name.contains("Button", ignoreCase = true) })

        // No matches
        screenModel.onIntent(EditorIntent.SetSearchKeyword("NonExistentComponentXyz"))
        assertTrue(screenModel.state.value.filteredComposeTypes.isEmpty())

        // Blank restores all entries
        screenModel.onIntent(EditorIntent.SetSearchKeyword("   "))
        assertEquals(ComposeType.entries, screenModel.state.value.filteredComposeTypes)
    }

    @Test
    fun testTreePanel_dispatchesIntents_and_rendersFromRootNode() {
        val screenModel = EditorScreenModel()
        val rootId = screenModel.state.value.rootNode.id

        // 1. Add Node to root
        screenModel.onIntent(EditorIntent.AddNode(rootId, ComposeType.Column))
        val column = screenModel.state.value.rootNode.children().first()
        assertEquals(ComposeType.Column, column.type)

        // 2. Select Node
        screenModel.onIntent(EditorIntent.SelectNode(column.id))
        assertEquals(column.id, screenModel.state.value.selectedNodeId)
        assertEquals(column.id, screenModel.state.value.selectedNode?.id)

        // 3. Add second child to test reordering
        screenModel.onIntent(EditorIntent.AddNode(rootId, ComposeType.Text))
        val text = screenModel.state.value.rootNode.children()[1]
        assertEquals(ComposeType.Text, text.type)

        // 4. Reorder Node (move second child up)
        screenModel.onIntent(EditorIntent.ReorderNode(text.id, EditorIntent.ReorderNode.Direction.UP))
        assertEquals(text.id, screenModel.state.value.rootNode.children()[0].id)
        assertEquals(column.id, screenModel.state.value.rootNode.children()[1].id)

        // 5. Delete Node (delete currently selected node)
        screenModel.onIntent(EditorIntent.DeleteNode(column.id))
        assertEquals(1, screenModel.state.value.rootNode.children().size)
        // Since selected node was deleted, selection should be cleared
        assertEquals(null, screenModel.state.value.selectedNodeId)
        assertEquals(null, screenModel.state.value.selectedNode)
    }
}
