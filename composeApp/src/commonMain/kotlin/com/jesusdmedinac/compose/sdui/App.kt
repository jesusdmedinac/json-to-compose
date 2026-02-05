package com.jesusdmedinac.compose.sdui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.LocalCustomRenderers
import com.jesusdmedinac.jsontocompose.LocalDrawableResources
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state.StateHost
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import json_to_compose.composeapp.generated.resources.Res
import json_to_compose.composeapp.generated.resources.compose_multiplatform
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val drawableResources = mapOf(
        "compose-multiplatform" to Res.drawable.compose_multiplatform
    )

    // Load State and Behaviors
    val showcaseState = rememberShowcaseState()

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

    // Construct the UI Tree
    val sections = listOf(
        layoutSection(),
        contentSection(),
        inputSection(),
        containerSection(),
        navigationSection(),
        lazyListSection(),
        modifiersSection(),
        customSection()
    ).flatten()

    val rootScaffold = ComposeNode(
        type = ComposeType.Scaffold,
        properties = NodeProperties.ScaffoldProps(
            topBar = ComposeNode(
                type = ComposeType.TopAppBar,
                properties = NodeProperties.TopAppBarProps(
                    title = ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(
                            text = "JSON to Compose Showcase"
                        )
                    )
                )
            ),
            bottomBar = ComposeNode(
                type = ComposeType.BottomBar,
                properties = NodeProperties.BottomBarProps(
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.BottomNavigationItem,
                            properties = NodeProperties.BottomNavigationItemProps(
                                selectedStateHostName = "nav_home_selected",
                                onClickEventName = "onNavHome",
                                label = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Home")
                                ),
                                icon = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "H")
                                )
                            )
                        ),
                        ComposeNode(
                            type = ComposeType.BottomNavigationItem,
                            properties = NodeProperties.BottomNavigationItemProps(
                                selectedStateHostName = "nav_search_selected",
                                onClickEventName = "onNavSearch",
                                label = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Search")
                                ),
                                icon = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "S")
                                )
                            )
                        ),
                        ComposeNode(
                            type = ComposeType.BottomNavigationItem,
                            properties = NodeProperties.BottomNavigationItemProps(
                                selectedStateHostName = "nav_profile_selected",
                                onClickEventName = "onNavProfile",
                                label = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Profile")
                                ),
                                icon = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "P")
                                )
                            )
                        )
                    )
                )
            ),
            child = ComposeNode(
                type = ComposeType.LazyColumn,
                properties = NodeProperties.ColumnProps(
                    children = sections
                )
            )
        )
    )

    CompositionProviders(
        drawableResources = drawableResources,
        behaviors = showcaseState.behaviors,
        stateHosts = showcaseState.stateHosts,
        customRenderers = customRenderers,
    ) {
        MaterialTheme {
            rootScaffold.ToCompose()
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
