package com.jesusdmedinac.compose.sdui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import io.github.kotlin.fibonacci.Behavior
import io.github.kotlin.fibonacci.ToCompose
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        JSON_AS_STRING.ToCompose(object : Behavior {
            override fun onClick(eventName: String) {
                println("Event: $eventName")
            }
        })
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
