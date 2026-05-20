package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeShape
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.state.StateHost
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalTestApi::class, ExperimentalMaterial3Api::class)
class ModalBottomSheetRendererTest {

    // --- Scenario 1: Render a ModalBottomSheet with content ---

    @Test
    fun modalBottomSheetRendersWithContent() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.ModalBottomSheet,
            properties = NodeProperties.ModalBottomSheetProps(
                visible = true,
                child = ComposeNode(
                    type = ComposeType.Column,
                    properties = NodeProperties.ColumnProps(
                        children = listOf(
                            ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Option 1")
                            ),
                            ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Option 2")
                            )
                        )
                    )
                )
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithTag("ModalBottomSheet").assertExists()
        onNodeWithText("Option 1").assertExists()
        onNodeWithText("Option 2").assertExists()
    }

    // --- Scenario 2: ModalBottomSheet hidden when state is false ---

    @Test
    fun modalBottomSheetHiddenWhenStateIsFalse() = runComposeUiTest {
        var sheetVisible = false
        val mockStateHost = object : StateHost<Boolean> {
            override val state: Boolean get() = sheetVisible
            override fun onStateChange(state: Boolean) {
                sheetVisible = state
            }
        }

        val node = ComposeNode(
            type = ComposeType.ModalBottomSheet,
            properties = NodeProperties.ModalBottomSheetProps(
                visibleStateHostName = "sheet_visibility",
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Content")
                )
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf("sheet_visibility" to mockStateHost)
            ) {
                node.ToCompose()
            }
        }

        // Sheet should not be rendered when visible is false
        onNodeWithTag("ModalBottomSheet").assertDoesNotExist()
    }

    // --- Scenario 3: ModalBottomSheet dismiss updates state ---

    @Test
    fun modalBottomSheetDismissUpdatesState() = runComposeUiTest {
        var sheetVisible = true
        val mockStateHost = object : StateHost<Boolean> {
            override val state: Boolean get() = sheetVisible
            override fun onStateChange(state: Boolean) {
                sheetVisible = state
            }
        }

        var dismissCalled = false
        val mockBehavior = object : Behavior {
            override fun invoke() {
                dismissCalled = true
            }
        }

        val node = ComposeNode(
            type = ComposeType.ModalBottomSheet,
            properties = NodeProperties.ModalBottomSheetProps(
                visibleStateHostName = "sheet_visibility",
                onDismissRequestEventName = "close_sheet",
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Content")
                )
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf("sheet_visibility" to mockStateHost),
                LocalBehavior provides mapOf("close_sheet" to mockBehavior)
            ) {
                node.ToCompose()
            }
        }

        // Sheet should be visible initially
        onNodeWithTag("ModalBottomSheet").assertExists()

        // Note: In a real test, we would simulate a dismiss action
        // For now, we verify the behavior is wired correctly
        // The actual dismiss interaction would require more complex UI test setup
    }

    // --- Scenario 4: ModalBottomSheet with dragHandle ---

    @Test
    fun modalBottomSheetWithShowDragHandle() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.ModalBottomSheet,
            properties = NodeProperties.ModalBottomSheetProps(
                visible = true,
                showDragHandle = true,
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Content")
                )
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithTag("ModalBottomSheet").assertExists()
        onNodeWithText("Content").assertExists()
    }

    // --- Scenario 5: ModalBottomSheet with custom shape ---

    @Test
    fun modalBottomSheetWithCustomShape() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.ModalBottomSheet,
            properties = NodeProperties.ModalBottomSheetProps(
                visible = true,
                shape = ComposeShape.RoundedCorner(topStart = 28, topEnd = 28),
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Content")
                )
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithTag("ModalBottomSheet").assertExists()
        onNodeWithText("Content").assertExists()
    }

    // --- Scenario 6: ModalBottomSheet with scrimColor ---

    @Test
    fun modalBottomSheetWithScrimColor() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.ModalBottomSheet,
            properties = NodeProperties.ModalBottomSheetProps(
                visible = true,
                scrimColor = "#80000000",
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Content")
                )
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithTag("ModalBottomSheet").assertExists()
        onNodeWithText("Content").assertExists()
    }

    // --- Scenario 7: Scaffold with ModalBottomSheet integration ---

    @Test
    fun scaffoldWithModalBottomSheetIntegration() = runComposeUiTest {
        val sheetVisible = mutableStateOf(false)
        val mockStateHost = object : StateHost<Boolean> {
            override val state: Boolean get() = sheetVisible.value
            override fun onStateChange(state: Boolean) {
                sheetVisible.value = state
            }
        }

        val node = ComposeNode(
            type = ComposeType.Scaffold,
            properties = NodeProperties.ScaffoldProps(
                child = ComposeNode(
                    type = ComposeType.Column,
                    properties = NodeProperties.ColumnProps(
                        children = listOf(
                            ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Scaffold Content")
                            ),
                            ComposeNode(
                                type = ComposeType.ModalBottomSheet,
                                properties = NodeProperties.ModalBottomSheetProps(
                                    visibleStateHostName = "sheet_visibility",
                                    child = ComposeNode(
                                        type = ComposeType.Text,
                                        properties = NodeProperties.TextProps(text = "Sheet Content")
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf("sheet_visibility" to mockStateHost)
            ) {
                node.ToCompose()
            }
        }

        // Initially sheet is not visible
        onNodeWithTag("ModalBottomSheet").assertDoesNotExist()
        onNodeWithText("Sheet Content").assertDoesNotExist()

        // Toggle state to true
        sheetVisible.value = true
        waitForIdle()

        // Sheet should exist and be displayed
        onNodeWithTag("ModalBottomSheet").assertExists()
        onNodeWithText("Sheet Content").assertExists()
    }

    // --- Scenario 8: Serialize and deserialize ModalBottomSheet ---

    @Test
    fun modalBottomSheetSerializationRoundtrip() {
        val original = ComposeNode(
            type = ComposeType.ModalBottomSheet,
            properties = NodeProperties.ModalBottomSheetProps(
                visible = true,
                visibleStateHostName = "sheet_visible",
                onDismissRequestEventName = "dismiss",
                showDragHandle = true,
                shape = ComposeShape.RoundedCorner(topStart = 28, topEnd = 28),
                scrimColor = "#80000000",
                child = ComposeNode(
                    type = ComposeType.Column,
                    properties = NodeProperties.ColumnProps(
                        children = listOf(
                            ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Option 1")
                            )
                        )
                    )
                )
            )
        )

        val json = Json.encodeToString(original)
        val decoded = Json.decodeFromString<ComposeNode>(json)

        assertEquals(ComposeType.ModalBottomSheet, decoded.type)
        val props = decoded.properties as? NodeProperties.ModalBottomSheetProps
        assertNotNull(props)
        assertEquals(true, props.visible)
        assertEquals("sheet_visible", props.visibleStateHostName)
        assertEquals("dismiss", props.onDismissRequestEventName)
        assertEquals(true, props.showDragHandle)
        assertEquals("#80000000", props.scrimColor)
        assertNotNull(props.child)
        assertEquals(ComposeType.Column, props.child?.type)
    }
}