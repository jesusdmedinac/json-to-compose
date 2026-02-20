package com.jesusdmedinac.jsontocompose

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.renderer.ToSpacer
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state.StateHost
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.renderer.ToBox
import com.jesusdmedinac.jsontocompose.renderer.ToButton
import com.jesusdmedinac.jsontocompose.renderer.ToColumn
import com.jesusdmedinac.jsontocompose.renderer.ToCard
import com.jesusdmedinac.jsontocompose.renderer.ToAlertDialog
import com.jesusdmedinac.jsontocompose.renderer.ToCustom
import com.jesusdmedinac.jsontocompose.renderer.ToExtendedFloatingActionButton
import com.jesusdmedinac.jsontocompose.renderer.ToFloatingActionButton
import com.jesusdmedinac.jsontocompose.renderer.ToIcon
import com.jesusdmedinac.jsontocompose.renderer.ToIconButton
import com.jesusdmedinac.jsontocompose.renderer.ToImage
import com.jesusdmedinac.jsontocompose.renderer.ToLazyColumn
import com.jesusdmedinac.jsontocompose.renderer.ToLazyRow
import com.jesusdmedinac.jsontocompose.renderer.ToRow
import com.jesusdmedinac.jsontocompose.renderer.ToScaffold
import com.jesusdmedinac.jsontocompose.renderer.ToText
import com.jesusdmedinac.jsontocompose.renderer.ToTextField
import com.jesusdmedinac.jsontocompose.renderer.ToBottomBar
import com.jesusdmedinac.jsontocompose.renderer.ToBottomNavigationItem
import com.jesusdmedinac.jsontocompose.renderer.ToCheckbox
import com.jesusdmedinac.jsontocompose.renderer.ToSwitch
import com.jesusdmedinac.jsontocompose.renderer.ToTopAppBar
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.DrawableResource

/**
 * Provides local drawable resources for [ComposeType.Image] components.
 *
 * Map resource names (used in `ImageProps.resourceName`) to [DrawableResource] instances.
 */
val LocalDrawableResources = staticCompositionLocalOf<Map<String, DrawableResource>> { emptyMap() }

/**
 * Provides named [Behavior] callbacks for event-driven components.
 *
 * Map event names (used in `onClickEventName`, `onCheckedChangeEventName`, etc.)
 * to [Behavior] implementations.
 */
val LocalBehavior = staticCompositionLocalOf<Map<String, Behavior>> { emptyMap() }

/**
 * Provides named [StateHost] instances for stateful components.
 *
 * Map state host names (used in `*StateHostName` properties) to [StateHost] instances.
 */
val LocalStateHost = staticCompositionLocalOf<Map<String, StateHost<*>>> { emptyMap() }

/**
 * Provides custom component renderers for [ComposeType.Custom] nodes.
 *
 * Map custom type names (used in `CustomProps.customType`) to composable render functions.
 */
val LocalCustomRenderers = staticCompositionLocalOf<Map<String, @Composable (ComposeNode) -> Unit>> { emptyMap() }

/**
 * Provides the current [RowScope] to child components inside a Row.
 *
 * Used internally by renderers that need Row-specific modifiers (e.g., `Modifier.weight()`).
 */
val LocalRowScope = compositionLocalOf<RowScope?> { null }

/**
 * Deserializes this JSON string into a [ComposeNode] and renders it as a Compose UI tree.
 *
 * This is the main entry point for rendering server-driven UI from a JSON string.
 *
 * @throws kotlinx.serialization.SerializationException if the JSON is malformed or contains unknown types.
 */
@Composable
fun String.ToCompose() {
    Json.decodeFromString<ComposeNode>(this).ToCompose()
}

/**
 * Renders this [ComposeNode] as the corresponding Compose component.
 *
 * Dispatches to the type-specific renderer based on [ComposeNode.type].
 * Child nodes are rendered recursively by each component's renderer.
 */
@Composable
fun ComposeNode.ToCompose() {
    when (type) {
        ComposeType.Column -> ToColumn()
        ComposeType.Row -> ToRow()
        ComposeType.Box -> ToBox()
        ComposeType.Spacer -> ToSpacer()
        ComposeType.Text -> ToText()
        ComposeType.Icon -> ToIcon()
        ComposeType.Button,
        ComposeType.OutlinedButton,
        ComposeType.TextButton,
        ComposeType.ElevatedButton,
        ComposeType.FilledTonalButton -> ToButton()

        ComposeType.IconButton -> ToIconButton()
        ComposeType.FloatingActionButton -> ToFloatingActionButton()
        ComposeType.ExtendedFloatingActionButton -> ToExtendedFloatingActionButton()

        ComposeType.Image -> ToImage()
        ComposeType.TextField -> ToTextField()
        ComposeType.LazyColumn -> ToLazyColumn()
        ComposeType.LazyRow -> ToLazyRow()
        ComposeType.Scaffold -> ToScaffold()
        ComposeType.Card -> ToCard()
        ComposeType.AlertDialog -> ToAlertDialog()
        ComposeType.TopAppBar -> ToTopAppBar()
        ComposeType.BottomBar -> ToBottomBar()
        ComposeType.BottomNavigationItem -> ToBottomNavigationItem()
        ComposeType.Switch -> ToSwitch()
        ComposeType.Checkbox -> ToCheckbox()
        ComposeType.Custom -> ToCustom()
    }
}
