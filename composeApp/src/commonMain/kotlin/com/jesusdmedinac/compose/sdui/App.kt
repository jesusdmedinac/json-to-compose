package com.jesusdmedinac.compose.sdui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.jesusdmedinac.jsontocompose.Behavior
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.LocalDrawableResources
import com.jesusdmedinac.jsontocompose.ToCompose
import json_to_compose.composeapp.generated.resources.Res
import json_to_compose.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val drawableResources = mapOf(
        "compose-multiplatform" to Res.drawable.compose_multiplatform
    )

    val behaviors = mapOf(
        "button_clicked" to object : Behavior {
            override fun onClick(eventName: String) {
                println("Button clicked: $eventName")
            }
        }
    )

    CompositionProviders(
        drawableResources = drawableResources,
        behaviors = behaviors
    ) {
        MaterialTheme {
            JSON_AS_STRING.ToCompose()
        }
    }
}

@Composable
fun CompositionProviders(
    drawableResources: Map<String, DrawableResource>,
    behaviors: Map<String, Behavior>,
    content: @Composable () -> Unit
) {
    DrawableResourcesComposition(drawableResources = drawableResources) {
        BehaviorComposition(behaviors = behaviors) {
            content()
        }
    }
}

@Composable
fun DrawableResourcesComposition(
    drawableResources: Map<String, DrawableResource>,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalDrawableResources provides drawableResources) {
        content()
    }
}

@Composable
fun BehaviorComposition(
    behaviors: Map<String, Behavior>,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalBehavior provides behaviors) {
        content()
    }
}

val JSON_AS_STRING = """
    {
        "type": "Column",
        "children": [
            {
                "type": "Text",
                "text": "String to Text"
            },
            {
                "type": "Text",
                "text": "{\"type\": \"Text\",\"text\": \"String to Text\"}"
            },
            {
                "type": "Text",
                "text": "String to Text"
            },
            {
                "type": "Text",
                "text": "String to Button"
            },
            {
                "type": "Text",
                "text": "{\"type\": \"Button\",\"onClickEventName\": \"button_clicked\",\"child\": {\"type\": \"Text\",\"text\": \"Click me!\"}}"
            },
            {
                "type": "Button",
                "onClickEventName": "button_clicked",
                "child": {
                    "type": "Text",
                    "text": "Click me!"
                }
            },
            {
                "type": "Text",
                "text": "String to Column"
            },
            {
                "type": "Text",
                "text": "{\"type\": \"Column\",\"children\": [{\"type\": \"Text\",\"text\": \"First text\"}, {\"type\": \"Text\",\"text\": \"Second text\"}]}"
            },
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
            },
            {
                "type": "Text",
                "text": "String to Row"
            },
            {
                "type": "Text",
                "text": "{\"type\": \"Row\",\"children\": [{\"type\": \"Text\",\"text\": \"First text\"}, {\"type\": \"Text\",\"text\": \"Second text\"}]}"
            },
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
            },
            {
                "type": "Text",
                "text": "String to Box"
            },
            {
                "type": "Text",
                "text": "{\"type\": \"Box\",\"children\": [{\"type\": \"Text\",\"text\": \"First text\"}, {\"type\": \"Text\",\"text\": \"Second text\"}]}"
            },
            {
                "type": "Box",
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
        ]
    }
""".trimIndent()
