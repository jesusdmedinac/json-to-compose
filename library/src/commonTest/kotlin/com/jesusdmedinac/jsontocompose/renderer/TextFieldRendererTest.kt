package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state.StateHost
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class TextFieldRendererTest {

    @Test
    fun textFieldRendersAndEmitsStateChange() = runComposeUiTest {
        var currentValue by mutableStateOf("initial")
        val mockStateHost = object : StateHost<String> {
            override val state: String get() = currentValue
            override fun onStateChange(state: String) {
                currentValue = state
            }
        }

        val node = ComposeNode(
            type = ComposeType.TextField,
            properties = NodeProperties.TextFieldProps(
                valueStateHostName = "search"
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf("search" to mockStateHost)
            ) {
                node.ToTextField()
            }
        }

        onNodeWithText("initial").assertExists()
        onNodeWithText("initial").performTextReplacement("kotlin")
        assertEquals("kotlin", currentValue)
    }
}
