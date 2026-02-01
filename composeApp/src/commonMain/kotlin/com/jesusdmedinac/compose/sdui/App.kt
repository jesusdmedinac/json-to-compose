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
import com.jesusdmedinac.jsontocompose.LocalCustomRenderers
import com.jesusdmedinac.jsontocompose.LocalDrawableResources
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.ToCompose
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
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

    var textFieldValue by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val behaviors = mapOf(
        "button_clicked" to object : Behavior {
            override fun invoke() {
                println("Button clicked: button_clicked")
            }
        },
        "show_dialog" to object : Behavior {
            override fun invoke() {
                showDialog = true
            }
        },
        "dialog_confirm" to object : Behavior {
            override fun invoke() {
                println("Dialog confirmed")
                showDialog = false
            }
        },
        "dialog_dismiss" to object : Behavior {
            override fun invoke() {
                println("Dialog dismissed")
                showDialog = false
            }
        }
    )
    val stateHosts = mapOf(
        "text_field_value" to object : StateHost<String> {
            override val state: String
                get() = textFieldValue

            override fun onStateChange(state: String) {
                textFieldValue = state
            }
        },
        "dialog_visibility" to object : StateHost<Boolean> {
            override val state: Boolean
                get() = showDialog

            override fun onStateChange(state: Boolean) {
                showDialog = state
            }
        }
    )

    val customRenderers: Map<String, @Composable (ComposeNode) -> Unit> = mapOf(
        "ProductCard" to { node ->
            val customProps = node.properties as? NodeProperties.CustomProps
            val customData = customProps?.customData
            val title = customData?.get("title")?.jsonPrimitive?.content ?: "No title"
            val price = customData?.get("price")?.jsonPrimitive?.content ?: "0.00"
            Column {
                Text(text = "🛒 $title")
                Text(text = "Price: $$price")
            }
        }
    )

    CompositionProviders(
        drawableResources = drawableResources,
        behaviors = behaviors,
        stateHosts = stateHosts,
        customRenderers = customRenderers,
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
                                valueStateHostName = "text_field_value"
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
                        ComposeNode(
                            type = ComposeType.Card,
                            composeModifier = ComposeModifier(
                                operations = listOf(
                                    ComposeModifier.Operation.FillMaxWidth,
                                    ComposeModifier.Operation.Padding(8),
                                )
                            ),
                            properties = NodeProperties.CardProps(
                                elevation = 4,
                                cornerRadius = 12,
                                child = ComposeNode(
                                    type = ComposeType.Column,
                                    properties = NodeProperties.ColumnProps(
                                        children = listOf(
                                            ComposeNode(
                                                type = ComposeType.Text,
                                                properties = NodeProperties.TextProps(
                                                    text = "Card Title"
                                                ),
                                                composeModifier = ComposeModifier(
                                                    operations = listOf(
                                                        ComposeModifier.Operation.Padding(16),
                                                    )
                                                ),
                                            ),
                                            ComposeNode(
                                                type = ComposeType.Text,
                                                properties = NodeProperties.TextProps(
                                                    text = "Card body with elevation 4dp and 12dp rounded corners"
                                                ),
                                                composeModifier = ComposeModifier(
                                                    operations = listOf(
                                                        ComposeModifier.Operation.Padding(16),
                                                    )
                                                ),
                                            ),
                                        )
                                    )
                                )
                            )
                        ),
                        ComposeNode(
                            type = ComposeType.Button,
                            properties = NodeProperties.ButtonProps(
                                onClickEventName = "show_dialog",
                                child = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(
                                        text = "Show Dialog"
                                    )
                                )
                            )
                        ),
                        ComposeNode(
                            type = ComposeType.AlertDialog,
                            properties = NodeProperties.AlertDialogProps(
                                title = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(
                                        text = "Confirm Action"
                                    )
                                ),
                                text = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(
                                        text = "Do you want to proceed with this action?"
                                    )
                                ),
                                confirmButton = ComposeNode(
                                    type = ComposeType.Button,
                                    properties = NodeProperties.ButtonProps(
                                        onClickEventName = "dialog_confirm",
                                        child = ComposeNode(
                                            type = ComposeType.Text,
                                            properties = NodeProperties.TextProps(
                                                text = "Confirm"
                                            )
                                        )
                                    )
                                ),
                                dismissButton = ComposeNode(
                                    type = ComposeType.Button,
                                    properties = NodeProperties.ButtonProps(
                                        onClickEventName = "dialog_dismiss",
                                        child = ComposeNode(
                                            type = ComposeType.Text,
                                            properties = NodeProperties.TextProps(
                                                text = "Cancel"
                                            )
                                        )
                                    )
                                ),
                                visibilityStateHostName = "dialog_visibility",
                            )
                        ),
                        ComposeNode(
                            type = ComposeType.TopAppBar,
                            properties = NodeProperties.TopAppBarProps(
                                title = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(
                                        text = "My App"
                                    )
                                ),
                                navigationIcon = ComposeNode(
                                    type = ComposeType.Button,
                                    properties = NodeProperties.ButtonProps(
                                        onClickEventName = "button_clicked",
                                        child = ComposeNode(
                                            type = ComposeType.Text,
                                            properties = NodeProperties.TextProps(
                                                text = "<"
                                            )
                                        )
                                    )
                                ),
                                actions = listOf(
                                    ComposeNode(
                                        type = ComposeType.Button,
                                        properties = NodeProperties.ButtonProps(
                                            onClickEventName = "button_clicked",
                                            child = ComposeNode(
                                                type = ComposeType.Text,
                                                properties = NodeProperties.TextProps(
                                                    text = "Action"
                                                )
                                            )
                                        )
                                    ),
                                ),
                            )
                        ),
                        ComposeNode(
                            type = ComposeType.Scaffold,
                            composeModifier = ComposeModifier(
                                operations = listOf(
                                    ComposeModifier.Operation.FillMaxWidth,
                                    ComposeModifier.Operation.Height(120),
                                )
                            ),
                            properties = NodeProperties.ScaffoldProps(
                                child = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(
                                        text = "Content inside Scaffold"
                                    ),
                                    composeModifier = ComposeModifier(
                                        operations = listOf(
                                            ComposeModifier.Operation.Padding(16),
                                        )
                                    ),
                                )
                            )
                        ),
                        ComposeNode(
                            type = ComposeType.Custom,
                            properties = NodeProperties.CustomProps(
                                customType = "ProductCard",
                                customData = buildJsonObject {
                                    put("title", JsonPrimitive("Custom Product"))
                                    put("price", JsonPrimitive("99.99"))
                                }
                            )
                        ),
                    )
                )
            )
            // The ComposeNode tree can also be serialized to JSON with composeNode.toString()
            // and deserialized back with jsonString.ToCompose().
            // See the project documentation for the full JSON schema reference.
            val composeAsString = composeNode.toString()
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
            }
        }
    }
}

@Composable
fun CompositionProviders(
    drawableResources: Map<String, DrawableResource>,
    behaviors: Map<String, Behavior>,
    stateHosts: Map<String, StateHost<*>>,
    customRenderers: Map<String, @Composable (ComposeNode) -> Unit>,
    content: @Composable () -> Unit
) {
    DrawableResourcesComposition(drawableResources = drawableResources) {
        BehaviorComposition(behaviors = behaviors) {
            StateHostComposition(stateHosts = stateHosts) {
                CustomRenderersComposition(customRenderers = customRenderers) {
                    content()
                }
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

@Composable
fun CustomRenderersComposition(
    customRenderers: Map<String, @Composable (ComposeNode) -> Unit>,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalCustomRenderers provides customRenderers) {
        content()
    }
}

