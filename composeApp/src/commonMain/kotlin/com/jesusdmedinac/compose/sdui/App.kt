package com.jesusdmedinac.compose.sdui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.LocalDrawableResources
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state.StateHost
import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
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
            override fun onClick() {
                println("Button clicked: button_clicked")
            }
        }
    )

    var textFieldValue by remember { mutableStateOf("") }
    val stateHosts = mapOf(
        "text_field_value" to object : StateHost<String> {
            override val state: String
                get() = textFieldValue

            override fun onStateChange(state: String) {
                textFieldValue = state
            }
        }
    )

    CompositionProviders(
        drawableResources = drawableResources,
        behaviors = behaviors,
        stateHosts = stateHosts,
    ) {
        MaterialTheme {
            val composeNode = ComposeNode(
                type = ComposeType.Column,
                properties = NodeProperties.ColumnProps(
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(
                                text = "Text Node"
                            ),
                        ),
                        ComposeNode(
                            type = ComposeType.Button,
                            properties = NodeProperties.ButtonProps(
                                onClickEventName = "button_clicked",
                                child = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(
                                        text = "Button Node"
                                    )
                                )
                            )
                        ),
                        ComposeNode(
                            type = ComposeType.Image,
                            properties = NodeProperties.ImageProps(
                                url = "https://relatos.jesusdmedinac.com/_astro/carta-al-lector.OLllKYCu_Z1cdMQV.webp",
                                contentDescription = "Image Node from url",
                                contentScale = "Fit"
                            )
                        ),
                        ComposeNode(
                            type = ComposeType.Image,
                            properties = NodeProperties.ImageProps(
                                resourceName = "compose-multiplatform",
                                contentDescription = "Image Node from resource",
                                contentScale = "Fit"
                            )
                        ),
                        ComposeNode(
                            type = ComposeType.Column,
                            properties = NodeProperties.ColumnProps(
                                children = listOf(
                                    ComposeNode(
                                        type = ComposeType.Text,
                                        properties = NodeProperties.TextProps(
                                            text = "First text"
                                        )
                                    ),
                                    ComposeNode(
                                        type = ComposeType.Text,
                                        properties = NodeProperties.TextProps(
                                            text = "Second text"
                                        )
                                    ),
                                )
                            )
                        ),
                        ComposeNode(
                            type = ComposeType.Row,
                            properties = NodeProperties.RowProps(
                                children = listOf(
                                    ComposeNode(
                                        type = ComposeType.Text,
                                        properties = NodeProperties.TextProps(
                                            text = "First text"
                                        )
                                    ),
                                    ComposeNode(
                                        type = ComposeType.Text,
                                        properties = NodeProperties.TextProps(
                                            text = "Second text"
                                        )
                                    ),
                                )
                            )
                        ),
                        ComposeNode(
                            type = ComposeType.Box,
                            properties = NodeProperties.BoxProps(
                                children = listOf(
                                    ComposeNode(
                                        type = ComposeType.Text,
                                        properties = NodeProperties.TextProps(
                                            text = "First text"
                                        )
                                    ),
                                    ComposeNode(
                                        type = ComposeType.Text,
                                        properties = NodeProperties.TextProps(
                                            text = "Second text"
                                        )
                                    ),
                                )
                            )
                        ),
                        ComposeNode(
                            type = ComposeType.TextField,
                            properties = NodeProperties.TextFieldProps(
                                onTextChangeEventName = "text_field_value"
                            )
                        ),
                        ComposeNode(
                            type = ComposeType.LazyColumn,
                            composeModifier = ComposeModifier(
                                operations = listOf(
                                    ComposeModifier.Operation.FillMaxWidth,
                                    ComposeModifier.Operation.Height(64),
                                )
                            ),
                            properties = NodeProperties.ColumnProps(
                                children = listOf(
                                    ComposeNode(
                                        type = ComposeType.Text,
                                        properties = NodeProperties.TextProps(
                                            text = "First text on lazy column"
                                        ),
                                    ),
                                    ComposeNode(
                                        type = ComposeType.Text,
                                        properties = NodeProperties.TextProps(
                                            text = "Second text on lazy column"
                                        ),
                                    ),
                                    ComposeNode(
                                        type = ComposeType.Text,
                                        properties = NodeProperties.TextProps(
                                            text = "Third text on lazy column"
                                        ),
                                    ),
                                    ComposeNode(
                                        type = ComposeType.Text,
                                        properties = NodeProperties.TextProps(
                                            text = "Fourth text on lazy column"
                                        ),
                                    ),
                                ),
                            ),
                        ),
                        ComposeNode(
                            type = ComposeType.LazyRow,
                            composeModifier = ComposeModifier(
                                operations = listOf(
                                    ComposeModifier.Operation.FillMaxWidth,
                                    ComposeModifier.Operation.Width(64),
                                )
                            ),
                            properties = NodeProperties.RowProps(
                                children = listOf(
                                    ComposeNode(
                                        type = ComposeType.Text,
                                        properties = NodeProperties.TextProps(
                                            text = "First text on lazy row"
                                        ),
                                    ),
                                    ComposeNode(
                                        type = ComposeType.Text,
                                        properties = NodeProperties.TextProps(
                                            text = "Second text on lazy row"
                                        ),
                                    ),
                                    ComposeNode(
                                        type = ComposeType.Text,
                                        properties = NodeProperties.TextProps(
                                            text = "Third text on lazy row"
                                        ),
                                    ),
                                    ComposeNode(
                                        type = ComposeType.Text,
                                        properties = NodeProperties.TextProps(
                                            text = "Fourth text on lazy row"
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    )
                )
            )
            val composeAsString = composeNode.toString()
            println(composeAsString)
            LazyColumn {
                item {
                    composeAsString.ToCompose()
                }

                item {
                    Divider()
                }

                item {
                    Text(text = composeAsString)
                }

                item {
                    Divider()
                }

                item {
                    Column {
                        JSON_AS_STRING.ToCompose()
                    }
                }
            }
        }
    }
}

@Composable
fun CompositionProviders(
    drawableResources: Map<String, DrawableResource>,
    behaviors: Map<String, Behavior>,
    stateHosts: Map<String, StateHost<*>>,
    content: @Composable () -> Unit
) {
    DrawableResourcesComposition(drawableResources = drawableResources) {
        BehaviorComposition(behaviors = behaviors) {
            StateHostComposition(stateHosts = stateHosts) {
                content()
            }
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

@Composable
fun StateHostComposition(
    stateHosts: Map<String, StateHost<*>>,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalStateHost provides stateHosts) {
        content()
    }
}

val JSON_AS_STRING = """
{
  "type": "Column",
  "properties": {
    "type": "ColumnProps",
    "children": [
      {
        "type": "Text",
        "properties": {
          "type": "TextProps",
          "text": "Text Node"
        }
      },
      {
        "type": "Button",
        "properties": {
          "type": "ButtonProps",
          "onClickEventName": "button_clicked",
          "child": {
            "type": "Text",
            "properties": {
              "type": "TextProps",
              "text": "Button Node"
            }
          }
        }
      },
      {
        "type": "Image",
        "properties": {
          "type": "ImageProps",
          "url": "https://relatos.jesusdmedinac.com/_astro/carta-al-lector.OLllKYCu_Z1cdMQV.webp",
          "contentDescription": "Image Node from url"
        }
      },
      {
        "type": "Image",
        "properties": {
          "type": "ImageProps",
          "resourceName": "compose-multiplatform",
          "contentDescription": "Image Node from resource"
        }
      },
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
      },
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
      },
      {
        "type": "Box",
        "properties": {
          "type": "BoxProps",
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
      },
      {
        "type": "TextField",
        "properties": {
          "type": "TextFieldProps",
          "onTextChangeEventName": "text_field_value"
        }
      },
      {
        "type": "LazyColumn",
        "properties": {
          "type": "ColumnProps",
          "children": [
            {
              "type": "Text",
              "properties": {
                "type": "TextProps",
                "text": "First text on lazy column"
              }
            },
            {
              "type": "Text",
              "properties": {
                "type": "TextProps",
                "text": "Second text on lazy column"
              }
            },
            {
              "type": "Text",
              "properties": {
                "type": "TextProps",
                "text": "Third text on lazy column"
              }
            },
            {
              "type": "Text",
              "properties": {
                "type": "TextProps",
                "text": "Fourth text on lazy column"
              }
            }
          ]
        },
        "composeModifier": {
          "operations": [
            {
              "type": "FillMaxWidth"
            },
            {
              "type": "Height",
              "value": 64
            }
          ]
        }
      },
      {
        "type": "LazyRow",
        "properties": {
          "type": "RowProps",
          "children": [
            {
              "type": "Text",
              "properties": {
                "type": "TextProps",
                "text": "First text on lazy row"
              }
            },
            {
              "type": "Text",
              "properties": {
                "type": "TextProps",
                "text": "Second text on lazy row"
              }
            },
            {
              "type": "Text",
              "properties": {
                "type": "TextProps",
                "text": "Third text on lazy row"
              }
            },
            {
              "type": "Text",
              "properties": {
                "type": "TextProps",
                "text": "Fourth text on lazy row"
              }
            }
          ]
        },
        "composeModifier": {
          "operations": [
            {
              "type": "FillMaxWidth"
            },
            {
              "type": "Width",
              "value": 64
            }
          ]
        }
      }
    ]
  }
}
""".trimIndent()
