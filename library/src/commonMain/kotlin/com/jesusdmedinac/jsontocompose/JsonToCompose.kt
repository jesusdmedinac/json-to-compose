package com.jesusdmedinac.jsontocompose

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.renderer.ToAlertDialog
import com.jesusdmedinac.jsontocompose.renderer.ToAssistChip
import com.jesusdmedinac.jsontocompose.renderer.ToBadge
import com.jesusdmedinac.jsontocompose.renderer.ToBadgedBox
import com.jesusdmedinac.jsontocompose.renderer.ToBottomBar
import com.jesusdmedinac.jsontocompose.renderer.ToBottomNavigationItem
import com.jesusdmedinac.jsontocompose.renderer.ToBox
import com.jesusdmedinac.jsontocompose.renderer.ToButton
import com.jesusdmedinac.jsontocompose.renderer.ToCard
import com.jesusdmedinac.jsontocompose.renderer.ToCheckbox
import com.jesusdmedinac.jsontocompose.renderer.ToCircularProgressIndicator
import com.jesusdmedinac.jsontocompose.renderer.ToColumn
import com.jesusdmedinac.jsontocompose.renderer.ToCustom
import com.jesusdmedinac.jsontocompose.renderer.ToDatePicker
import com.jesusdmedinac.jsontocompose.renderer.ToElevatedCard
import com.jesusdmedinac.jsontocompose.renderer.ToExtendedFloatingActionButton
import com.jesusdmedinac.jsontocompose.renderer.ToFilterChip
import com.jesusdmedinac.jsontocompose.renderer.ToFloatingActionButton
import com.jesusdmedinac.jsontocompose.renderer.ToFlowColumn
import com.jesusdmedinac.jsontocompose.renderer.ToFlowRow
import com.jesusdmedinac.jsontocompose.renderer.ToHorizontalDivider
import com.jesusdmedinac.jsontocompose.renderer.ToHorizontalPager
import com.jesusdmedinac.jsontocompose.renderer.ToIcon
import com.jesusdmedinac.jsontocompose.renderer.ToIconButton
import com.jesusdmedinac.jsontocompose.renderer.ToImage
import com.jesusdmedinac.jsontocompose.renderer.ToInputChip
import com.jesusdmedinac.jsontocompose.renderer.ToLazyColumn
import com.jesusdmedinac.jsontocompose.renderer.ToLazyRow
import com.jesusdmedinac.jsontocompose.renderer.ToLinearProgressIndicator
import com.jesusdmedinac.jsontocompose.renderer.ToListItem
import com.jesusdmedinac.jsontocompose.renderer.ToModalBottomSheet
import com.jesusdmedinac.jsontocompose.renderer.ToModalNavigationDrawer
import com.jesusdmedinac.jsontocompose.renderer.ToMultiChoiceSegmentedButtonRow
import com.jesusdmedinac.jsontocompose.renderer.ToNavigationBar
import com.jesusdmedinac.jsontocompose.renderer.ToNavigationBarItem
import com.jesusdmedinac.jsontocompose.renderer.ToNavigationDrawerItem
import com.jesusdmedinac.jsontocompose.renderer.ToNavigationRail
import com.jesusdmedinac.jsontocompose.renderer.ToNavigationRailItem
import com.jesusdmedinac.jsontocompose.renderer.ToOutlinedCard
import com.jesusdmedinac.jsontocompose.renderer.ToPlainTooltip
import com.jesusdmedinac.jsontocompose.renderer.ToRadioButton
import com.jesusdmedinac.jsontocompose.renderer.ToRichTooltip
import com.jesusdmedinac.jsontocompose.renderer.ToRow
import com.jesusdmedinac.jsontocompose.renderer.ToScaffold
import com.jesusdmedinac.jsontocompose.renderer.ToScrollableTabRow
import com.jesusdmedinac.jsontocompose.renderer.ToSearchBar
import com.jesusdmedinac.jsontocompose.renderer.ToSegmentedButton
import com.jesusdmedinac.jsontocompose.renderer.ToSingleChoiceSegmentedButtonRow
import com.jesusdmedinac.jsontocompose.renderer.ToSlider
import com.jesusdmedinac.jsontocompose.renderer.ToSnackbarHost
import com.jesusdmedinac.jsontocompose.renderer.ToSpacer
import com.jesusdmedinac.jsontocompose.renderer.ToSuggestionChip
import com.jesusdmedinac.jsontocompose.renderer.ToSurface
import com.jesusdmedinac.jsontocompose.renderer.ToSwitch
import com.jesusdmedinac.jsontocompose.renderer.ToTab
import com.jesusdmedinac.jsontocompose.renderer.ToTabRow
import com.jesusdmedinac.jsontocompose.renderer.ToText
import com.jesusdmedinac.jsontocompose.renderer.ToTextField
import com.jesusdmedinac.jsontocompose.renderer.ToTimePicker
import com.jesusdmedinac.jsontocompose.renderer.ToTopAppBar
import com.jesusdmedinac.jsontocompose.renderer.ToVerticalDivider
import com.jesusdmedinac.jsontocompose.renderer.ToVerticalPager
import com.jesusdmedinac.jsontocompose.state.StateHost
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
        ComposeType.TextField,
        ComposeType.OutlinedTextField -> ToTextField()
        ComposeType.LazyColumn -> ToLazyColumn()
        ComposeType.LazyRow -> ToLazyRow()
        ComposeType.Scaffold -> ToScaffold()
        ComposeType.Card -> ToCard()
        ComposeType.ElevatedCard -> ToElevatedCard()
        ComposeType.OutlinedCard -> ToOutlinedCard()
        ComposeType.AlertDialog -> ToAlertDialog()
        ComposeType.TopAppBar -> ToTopAppBar()
        ComposeType.NavigationBar -> ToNavigationBar()
        ComposeType.NavigationBarItem -> ToNavigationBarItem()
        ComposeType.NavigationRail -> ToNavigationRail()
        ComposeType.NavigationRailItem -> ToNavigationRailItem()
        ComposeType.ModalNavigationDrawer -> ToModalNavigationDrawer()
        ComposeType.NavigationDrawerItem -> ToNavigationDrawerItem()
        ComposeType.TabRow -> ToTabRow()
        ComposeType.ScrollableTabRow -> ToScrollableTabRow()
        ComposeType.Tab -> ToTab()
        ComposeType.BottomBar -> ToBottomBar()
        ComposeType.BottomNavigationItem -> ToBottomNavigationItem()
        ComposeType.Switch -> ToSwitch()
        ComposeType.Checkbox -> ToCheckbox()

        // --- Phase 3: Stub routes ---
        ComposeType.Slider -> ToSlider()
        ComposeType.RadioButton -> ToRadioButton()
        ComposeType.SingleChoiceSegmentedButtonRow -> ToSingleChoiceSegmentedButtonRow()
        ComposeType.MultiChoiceSegmentedButtonRow -> ToMultiChoiceSegmentedButtonRow()
        ComposeType.SegmentedButton -> ToSegmentedButton()
        ComposeType.DatePicker -> ToDatePicker()
        ComposeType.TimePicker -> ToTimePicker()
        ComposeType.SearchBar -> ToSearchBar()
        ComposeType.HorizontalDivider -> ToHorizontalDivider()
        ComposeType.VerticalDivider -> ToVerticalDivider()
        ComposeType.FlowRow -> ToFlowRow()
        ComposeType.FlowColumn -> ToFlowColumn()
        ComposeType.Surface -> ToSurface()
        ComposeType.HorizontalPager -> ToHorizontalPager()
        ComposeType.VerticalPager -> ToVerticalPager()
        ComposeType.ModalBottomSheet -> ToModalBottomSheet()
        ComposeType.Badge -> ToBadge()
        ComposeType.BadgedBox -> ToBadgedBox()
        ComposeType.AssistChip -> ToAssistChip()
        ComposeType.FilterChip -> ToFilterChip()
        ComposeType.InputChip -> ToInputChip()
        ComposeType.SuggestionChip -> ToSuggestionChip()
        ComposeType.CircularProgressIndicator -> ToCircularProgressIndicator()
        ComposeType.LinearProgressIndicator -> ToLinearProgressIndicator()
        ComposeType.PlainTooltip -> ToPlainTooltip()
        ComposeType.RichTooltip -> ToRichTooltip()
        ComposeType.SnackbarHost -> ToSnackbarHost()
        ComposeType.ListItem -> ToListItem()

        ComposeType.Custom -> ToCustom()
    }
}

