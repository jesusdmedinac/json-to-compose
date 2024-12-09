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
        """
            {
                "type": "Column",
                "children": [
                    {
                        "type": "Text",
                        "text": "Hello, World!"
                    },
                    {
                        "type": "Button",
                        "onClickEventName": "button_click",
                        "child": {
                            "type": "Text",
                            "text": "Click me!"
                        }
                    }
                ]
            }
        """.trimIndent().ToCompose(object : Behavior {
            override fun onClick(eventName: String) {
                println("Event: $eventName")
            }
        })
    }
}