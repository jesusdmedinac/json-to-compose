package com.jesusdmedinac.jsontocompose

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.*
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class JsonToComposeTest {

    @Test
    fun toComposeShouldComposeTextWithExpectedText() = runComposeUiTest {
        val expectedText = Random.nextInt().toString()
        setContent {
            """
                {
                    "type": "Text",
                    "properties": {
                        "type": "TextProps",
                        "text": "$expectedText"
                    }
                }
            """.trimIndent().ToCompose()
        }

        onNodeWithText(expectedText).assertIsDisplayed()
    }

    @Test
    fun toComposeShouldComposeButtonWithExpectedEventName() = runComposeUiTest {
        val expectedEventName = "my_button_click"
        var clicked = false
        val mockBehavior = object : Behavior {
            override fun invoke() {
                clicked = true
            }
        }

        setContent {
            CompositionLocalProvider(
                LocalBehavior provides mapOf(expectedEventName to mockBehavior)
            ) {
                """
                    {
                        "type": "Button",
                        "properties": {
                            "type": "ButtonProps",
                            "onClickEventName": "$expectedEventName",
                            "child": {
                                "type": "Text",
                                "properties": {
                                    "type": "TextProps",
                                    "text": "Click me!"
                                }
                            }
                        }
                    }
                """.trimIndent().ToCompose()
            }
        }

        onNodeWithText("Click me!").assertIsDisplayed()
        onNodeWithText("Click me!").performClick()
        assertTrue(clicked)
    }

    @Test
    fun toComposeShouldComposeColumnWithExpectedChildren() = runComposeUiTest {
        setContent {
            """
                {
                    "type": "Column",
                    "properties": {
                        "type": "ColumnProps",
                        "children": [
                            {
                                "type": "Text",
                                "properties": {
                                    "type": "TextProps",
                                    "text": "First text"
                                }
                            },
                            {
                                "type": "Text",
                                "properties": {
                                    "type": "TextProps",
                                    "text": "Second text"
                                }
                            }
                        ]
                    }
                }
            """.trimIndent().ToCompose()
        }

        onNodeWithText("First text").assertIsDisplayed()
        onNodeWithText("Second text").assertIsDisplayed()
    }

    @Test
    fun toComposeShouldComposeRowWithExpectedChildren() = runComposeUiTest {
        setContent {
            """
                {
                    "type": "Row",
                    "properties": {
                        "type": "RowProps",
                        "children": [
                            {
                                "type": "Text",
                                "properties": {
                                    "type": "TextProps",
                                    "text": "First text"
                                }
                            },
                            {
                                "type": "Text",
                                "properties": {
                                    "type": "TextProps",
                                    "text": "Second text"
                                }
                            }
                        ]
                    }
                }
            """.trimIndent().ToCompose()
        }

        onNodeWithText("First text").assertIsDisplayed()
        onNodeWithText("Second text").assertIsDisplayed()
    }
}
