package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.*
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.state.MutableStateHost
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class SliderRendererTest {

    // --- Scenario: Render a Slider with default range 0 to 1 ---
    @Test
    fun sliderRendersWithDefaultRange() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Slider,
            properties = NodeProperties.SliderProps(
                value = 0.5f
            )
        )

        setContent { node.ToSlider() }

        onNodeWithTag(ComposeType.Slider.name).assertExists()
        onNodeWithTag(ComposeType.Slider.name).assertRangeInfoEquals(androidx.compose.ui.semantics.ProgressBarRangeInfo(0.5f, 0f..1f))
    }

    // --- Scenario: Render a Slider with custom valueRange ---
    @Test
    fun sliderRendersWithCustomValueRange() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Slider,
            properties = NodeProperties.SliderProps(
                value = 25f,
                valueRange = NodeProperties.FloatRange(0f, 100f)
            )
        )

        setContent { node.ToSlider() }

        onNodeWithTag(ComposeType.Slider.name).assertRangeInfoEquals(androidx.compose.ui.semantics.ProgressBarRangeInfo(25f, 0f..100f))
    }

    // --- Scenario: Render a Slider with steps ---
    @Test
    fun sliderRendersWithSteps() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Slider,
            properties = NodeProperties.SliderProps(
                value = 40f,
                valueRange = NodeProperties.FloatRange(0f, 100f),
                steps = 4
            )
        )

        setContent { node.ToSlider() }

        onNodeWithTag(ComposeType.Slider.name).assertRangeInfoEquals(androidx.compose.ui.semantics.ProgressBarRangeInfo(40f, 0f..100f, 4))
    }

    // --- Scenario: Slider value controlled by state ---
    @Test
    fun sliderValueControlledByState() = runComposeUiTest {
        val state = MutableStateHost(0.7f)
        val node = ComposeNode(
            type = ComposeType.Slider,
            properties = NodeProperties.SliderProps(
                valueStateHostName = "volume"
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf("volume" to state)
            ) {
                node.ToSlider()
            }
        }

        onNodeWithTag(ComposeType.Slider.name).assertRangeInfoEquals(androidx.compose.ui.semantics.ProgressBarRangeInfo(0.7f, 0f..1f))

        state.onStateChange(0.3f)
        onNodeWithTag(ComposeType.Slider.name).assertRangeInfoEquals(androidx.compose.ui.semantics.ProgressBarRangeInfo(0.3f, 0f..1f))
    }

    // --- Scenario: Slider onValueChange triggers state update ---
    @Test
    fun sliderOnValueChangeTriggersStateUpdateAndBehavior() = runComposeUiTest {
        val state = MutableStateHost(0.5f)
        var behaviorInvoked = false
        val behavior = object : Behavior {
            override fun invoke() {
                behaviorInvoked = true
            }
        }

        val node = ComposeNode(
            type = ComposeType.Slider,
            properties = NodeProperties.SliderProps(
                valueStateHostName = "volume",
                onValueChangeEventName = "updateVolume"
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf("volume" to state),
                LocalBehavior provides mapOf("updateVolume" to behavior)
            ) {
                node.ToSlider()
            }
        }

        onNodeWithTag(ComposeType.Slider.name).performSemanticsAction(androidx.compose.ui.semantics.SemanticsActions.SetProgress) {
            it(0.8f)
        }
        
        assertEquals(0.8f, state.state)
        assertTrue(behaviorInvoked)
    }
}
