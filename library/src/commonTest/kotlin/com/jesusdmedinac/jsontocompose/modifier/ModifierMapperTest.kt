package com.jesusdmedinac.jsontocompose.modifier

import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.model.ComposeShape
import com.jesusdmedinac.jsontocompose.renderer.ToText
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class ModifierMapperTest {

    // --- Padding ---

    @Test
    fun paddingModifierAppliesCorrectly() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Padded"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Padding(value = 16)
                )
            )
        )

        setContent {
            node.ToText()
        }

        onNodeWithText("Padded").assertExists()
        onNodeWithText("Padded").assertIsDisplayed()
    }

    // --- FillMaxSize ---

    @Test
    fun fillMaxSizeModifierAppliesCorrectly() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Full"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.FillMaxSize
                )
            )
        )

        setContent {
            node.ToText()
        }

        onNodeWithText("Full").assertExists()
        onNodeWithText("Full").assertIsDisplayed()
    }

    // --- FillMaxWidth ---

    @Test
    fun fillMaxWidthModifierAppliesCorrectly() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Wide"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.FillMaxWidth
                )
            )
        )

        setContent {
            node.ToText()
        }

        onNodeWithText("Wide").assertExists()
        onNodeWithText("Wide").assertIsDisplayed()
    }

    // --- FillMaxHeight ---

    @Test
    fun fillMaxHeightModifierAppliesCorrectly() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Tall"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.FillMaxHeight
                )
            )
        )

        setContent {
            node.ToText()
        }

        onNodeWithText("Tall").assertExists()
        onNodeWithText("Tall").assertIsDisplayed()
    }

    // --- Width ---

    @Test
    fun widthModifierAppliesCorrectly() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Fixed Width"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Width(value = 200)
                )
            )
        )

        setContent {
            node.ToText()
        }

        onNodeWithText("Fixed Width").assertExists()
        onNodeWithText("Fixed Width").assertIsDisplayed()
    }

    // --- Height ---

    @Test
    fun heightModifierAppliesCorrectly() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Fixed Height"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Height(value = 100)
                )
            )
        )

        setContent {
            node.ToText()
        }

        onNodeWithText("Fixed Height").assertExists()
        onNodeWithText("Fixed Height").assertIsDisplayed()
    }

    // --- BackgroundColor ---

    @Test
    fun backgroundColorModifierAppliesCorrectly() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Blue BG"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.BackgroundColor(hexColor = "#FF0000FF")
                )
            )
        )

        setContent {
            node.ToText()
        }

        onNodeWithText("Blue BG").assertExists()
        onNodeWithText("Blue BG").assertIsDisplayed()
    }

    @Test
    fun backgroundColorWithInvalidColorFallsBackToWhite() {
        // toColorInt with invalid hex should return Color.White
        val color = "invalid".toColorInt()
        assertEquals(
            expected = androidx.compose.ui.graphics.Color.White,
            actual = color,
            message = "Invalid hex color should fall back to Color.White"
        )
    }

    @Test
    fun backgroundColorWithShortHexFallsBackToWhite() {
        // "#FF00FF" is only 7 chars, not 9 (#AARRGGBB)
        val color = "#FF00FF".toColorInt()
        assertEquals(
            expected = androidx.compose.ui.graphics.Color.White,
            actual = color,
            message = "Short hex color should fall back to Color.White"
        )
    }

    @Test
    fun backgroundColorWithValidHexParsesCorrectly() {
        // #FF0000FF = alpha=FF, red=00, green=00, blue=FF
        val color = "#FF0000FF".toColorInt()
        assertEquals(
            expected = androidx.compose.ui.graphics.Color(red = 0, green = 0, blue = 255, alpha = 255),
            actual = color,
            message = "Valid hex #FF0000FF should parse to blue with full alpha"
        )
    }

    @Test
    fun backgroundColorRedParsesCorrectly() {
        // #FFFF0000 = alpha=FF, red=FF, green=00, blue=00
        val color = "#FFFF0000".toColorInt()
        assertEquals(
            expected = androidx.compose.ui.graphics.Color(red = 255, green = 0, blue = 0, alpha = 255),
            actual = color,
            message = "Valid hex #FFFF0000 should parse to red with full alpha"
        )
    }

    // --- Multiple modifiers combined ---

    @Test
    fun multipleModifiersCombinedApplyCorrectly() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Styled"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Padding(value = 8),
                    ComposeModifier.Operation.FillMaxWidth,
                    ComposeModifier.Operation.BackgroundColor(hexColor = "#FFFF0000"),
                )
            )
        )

        setContent {
            node.ToText()
        }

        onNodeWithText("Styled").assertExists()
        onNodeWithText("Styled").assertIsDisplayed()
    }

    // --- Border ---

    @Test
    fun borderModifierAppliesCorrectly() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Bordered"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Border(
                        width = 2,
                        color = "#FF000000"
                    )
                )
            )
        )

        setContent {
            node.ToText()
        }

        onNodeWithText("Bordered").assertExists()
        onNodeWithText("Bordered").assertIsDisplayed()
    }

    // --- Shadow ---

    @Test
    fun shadowModifierAppliesCorrectly() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Shadowed"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Shadow(
                        elevation = 4
                    )
                )
            )
        )

        setContent {
            node.ToText()
        }

        onNodeWithText("Shadowed").assertExists()
        onNodeWithText("Shadowed").assertIsDisplayed()
    }

    // --- Clip ---

    @Test
    fun clipModifierAppliesCorrectly() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Clipped"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Clip(
                        shape = ComposeShape.Circle
                    )
                )
            )
        )

        setContent {
            node.ToText()
        }

        onNodeWithText("Clipped").assertExists()
        onNodeWithText("Clipped").assertIsDisplayed()
    }

    // --- Background (with Shape) ---

    @Test
    fun backgroundWithShapeModifierAppliesCorrectly() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Shaped BG"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Background(
                        color = "#FF00FF00",
                        shape = ComposeShape.RoundedCorner(all = 8)
                    )
                )
            )
        )

        setContent {
            node.ToText()
        }

        onNodeWithText("Shaped BG").assertExists()
        onNodeWithText("Shaped BG").assertIsDisplayed()
    }

    // --- Alpha ---

    @Test
    fun alphaModifierAppliesCorrectly() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Faded"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Alpha(value = 0.5f)
                )
            )
        )

        setContent {
            node.ToText()
        }

        onNodeWithText("Faded").assertExists()
        onNodeWithText("Faded").assertIsDisplayed()
    }

    // --- Rotate ---

    @Test
    fun rotateModifierAppliesCorrectly() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Rotated"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Rotate(degrees = 45f)
                )
            )
        )

        setContent {
            node.ToText()
        }

        onNodeWithText("Rotated").assertExists()
        onNodeWithText("Rotated").assertIsDisplayed()
    }

    // --- Custom Rounded Corner ---

    @Test
    fun customRoundedCornerShapeAppliesCorrectly() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Custom Corner"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Background(
                        color = "#FFFF0000",
                        shape = ComposeShape.RoundedCorner(
                            topStart = 10,
                            topEnd = 0,
                            bottomEnd = 20,
                            bottomStart = 5
                        )
                    )
                )
            )
        )

        setContent {
            node.ToText()
        }

        onNodeWithText("Custom Corner").assertExists()
        onNodeWithText("Custom Corner").assertIsDisplayed()
    }
}
