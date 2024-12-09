package io.github.kotlin.fibonacci

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.core.Is.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class JsonToComposeTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun toComposeShouldComposeTextWithExpectedText() {
        val expectedText = Random.nextInt().toString()
        composeTestRule.setContent {
            """
                {
                    "type": "Text",
                    "text": "$expectedText"
                }
            """.trimIndent().ToCompose()
        }

        composeTestRule.onNodeWithText(expectedText).assertIsDisplayed()
    }

    @Test
    fun toComposeShouldComposeButtonWithExpectedEventName() {
        val expectedEventName = Random.nextInt().toString()
        var actualEventName: String? = null
        composeTestRule.setContent {
            """
                {
                    "type": "Button",
                    "onClickEventName": "$expectedEventName",
                    "child": {
                        "type": "Text",
                        "text": "Click me!"
                    }
                }
            """.trimIndent().ToCompose(object : Behavior {
                override fun onClick(eventName: String) {
                    actualEventName = eventName
                }
            })
        }

        composeTestRule.onNodeWithText("Click me!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Click me!").performClick()
        assertThat(actualEventName, `is`(expectedEventName))
    }

    @Test
    fun toComposeShouldComposeColumnWithExpectedChildren() {
        composeTestRule.setContent {
            """
                {
                    "type": "Column",
                    "children": [
                        {
                            "type": "Text",
                            "text": "First text"
                        },
                        {
                            "type": "Text",
                            "text": "Second text"
                        }
                    ]
                }
            """.trimIndent()
                .ToCompose()
        }

        composeTestRule.onNodeWithText("First text").assertIsDisplayed()
        composeTestRule.onNodeWithText("Second text").assertIsDisplayed()
    }

    @Test
    fun toComposeShouldComposeRowWithExpectedChildren() {
        composeTestRule.setContent {
            """
                {
                    "type": "Row",
                    "children": [
                        {
                            "type": "Text",
                            "text": "First text"
                        },
                        {
                            "type": "Text",
                            "text": "Second text"
                        }
                    ]
                }
            """.trimIndent()
                .ToCompose()
        }

        composeTestRule.onNodeWithText("First text").assertIsDisplayed()
        composeTestRule.onNodeWithText("Second text").assertIsDisplayed()
    }


}