package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.state.StateHost
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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

    @Test
    fun textFieldRendersWithComplexProperties() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.TextField,
            properties = NodeProperties.TextFieldProps(
                value = "Test Value",
                placeholder = ComposeNode(ComposeType.Text, NodeProperties.TextProps(text = "Placeholder")),
                label = ComposeNode(ComposeType.Text, NodeProperties.TextProps(text = "Label")),
                supportingText = ComposeNode(ComposeType.Text, NodeProperties.TextProps(text = "SupportingText")),
                prefix = ComposeNode(ComposeType.Text, NodeProperties.TextProps(text = "$")),
                suffix = ComposeNode(ComposeType.Text, NodeProperties.TextProps(text = ".00")),
                readOnly = true,
                isError = true
            )
        )

        setContent {
            node.ToCompose()
        }

        // We aren't testing that "Test Value" is displayed if they are visually hidden due to placeholder logic
        // But placeholder logic in Compose only displays placeholder if text is empty. Wait, if value="Test Value",
        // then Placeholder won't be visible!
        // To be safe, test existence of Label, SupportingText, Prefix, Suffix, and the text itself ("Test Value").
        onNodeWithText("Test Value").assertExists()
        onNodeWithText("Label").assertExists()
        onNodeWithText("SupportingText").assertExists()
        onNodeWithText("$").assertExists()
        onNodeWithText(".00").assertExists()
    }

    @Test
    fun outlinedTextFieldRendersWithIcons() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.OutlinedTextField,
            properties = NodeProperties.TextFieldProps(
                value = "",
                placeholder = ComposeNode(ComposeType.Text, NodeProperties.TextProps(text = "Placeholder")),
                leadingIcon = ComposeNode(ComposeType.Text, NodeProperties.TextProps(text = "LeadingIcon")),
                trailingIcon = ComposeNode(ComposeType.Text, NodeProperties.TextProps(text = "TrailingIcon")),
                singleLine = true,
                keyboardType = "Email"
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithText("Placeholder").assertExists() // It's empty, so Placeholder should be displayed
        onNodeWithText("LeadingIcon").assertExists()
        onNodeWithText("TrailingIcon").assertExists()
    }

    @Test
    fun serializeAndDeserializeTextFieldNode() {
        val node = ComposeNode(
            type = ComposeType.TextField,
            properties = NodeProperties.TextFieldProps(
                value = "SerializationTest",
                isError = true,
                singleLine = false,
                maxLines = 5,
                keyboardType = "Password"
            )
        )

        val jsonString = Json.encodeToString(node)
        val decodedNode = Json.decodeFromString<ComposeNode>(jsonString)

        val decodedProps = decodedNode.properties as NodeProperties.TextFieldProps
        assertEquals("SerializationTest", decodedProps.value)
        assertEquals(true, decodedProps.isError)
        assertEquals(false, decodedProps.singleLine)
        assertEquals(5, decodedProps.maxLines)
        assertEquals("Password", decodedProps.keyboardType)
    }
}
