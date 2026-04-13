package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.*
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.state.MutableStateHost
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class, ExperimentalMaterial3Api::class)
class SearchBarRendererTest {

    // --- Scenario: Render a SearchBar ---
    @Test
    fun searchBarRenders() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.SearchBar,
            properties = NodeProperties.SearchBarProps(
                query = "Compose",
                placeholder = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Search...")
                )
            )
        )

        setContent { node.ToSearchBar() }

        onNodeWithTag(ComposeType.SearchBar.name).assertExists()
        // Query is shown instead of placeholder
        onNodeWithText("Compose").assertExists()
    }

    // --- Scenario: SearchBar query updates state as user types ---
    @Test
    fun searchBarQueryUpdatesState() = runComposeUiTest {
        val state = MutableStateHost("")
        val node = ComposeNode(
            type = ComposeType.SearchBar,
            properties = NodeProperties.SearchBarProps(
                queryStateHostName = "searchQuery"
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf("searchQuery" to state)
            ) {
                node.ToSearchBar()
            }
        }

        // The SearchBar tag is on the container, we need to find the editable part
        // Or we can use useUnmergedTree = true if needed, but M3 SearchBar merges descendants
        onNode(hasSetTextAction()).performTextInput("Kotlin")
        assertEquals("Kotlin", state.state)
    }
}
