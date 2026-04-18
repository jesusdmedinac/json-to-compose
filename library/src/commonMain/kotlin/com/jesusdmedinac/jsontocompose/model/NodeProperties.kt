package com.jesusdmedinac.jsontocompose.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Sealed interface for type-specific component configuration.
 *
 * Each [ComposeType] expects a specific [NodeProperties] subclass. For example,
 * [ComposeType.Text] expects [TextProps] and [ComposeType.Column] expects [ColumnProps].
 * The `type` discriminator in JSON determines which subclass is deserialized.
 */
@Serializable
@SerialName("NodeProperties")
sealed interface NodeProperties {

    /**
     * Properties for a [ComposeType.Text] component.
     *
     * @property text The static text to display.
     * @property textStateHostName Name of a `StateHost<String>` that provides the text dynamically.
     * @property fontSize The font size in sp.
     * @property fontSizeStateHostName Name of a `StateHost<Double>` that provides the font size dynamically.
     * @property fontWeight Font weight (e.g., "Bold", "Light", "Normal").
     * @property fontWeightStateHostName Name of a `StateHost<String>` for dynamic font weight.
     * @property fontStyle Font style ("Normal", "Italic").
     * @property fontStyleStateHostName Name of a `StateHost<String>` for dynamic font style.
     * @property color Text color as an ARGB integer.
     * @property colorStateHostName Name of a `StateHost<Int>` for dynamic color.
     * @property textAlign Text alignment ("Start", "Center", "End", "Justify").
     * @property textAlignStateHostName Name of a `StateHost<String>` for dynamic text alignment.
     * @property maxLines Maximum number of lines to display.
     * @property maxLinesStateHostName Name of a `StateHost<Int>` for dynamic max lines.
     * @property overflow Text overflow strategy ("Clip", "Ellipsis", "Visible").
     * @property overflowStateHostName Name of a `StateHost<String>` for dynamic overflow.
     * @property letterSpacing Letter spacing in sp.
     * @property letterSpacingStateHostName Name of a `StateHost<Double>` for dynamic letter spacing.
     * @property lineHeight Line height in sp.
     * @property lineHeightStateHostName Name of a `StateHost<Double>` for dynamic line height.
     * @property textDecoration Text decoration ("Underline", "LineThrough", "None").
     * @property textDecorationStateHostName Name of a `StateHost<String>` for dynamic text decoration.
     * @property minLines Minimum number of lines to display.
     * @property minLinesStateHostName Name of a `StateHost<Int>` for dynamic min lines.
     */
    @Serializable
    @SerialName("TextProps")
    data class TextProps(
        val text: String? = null,
        val textStateHostName: String? = null,
        val fontSize: Double? = null,
        val fontSizeStateHostName: String? = null,
        val fontWeight: String? = null,
        val fontWeightStateHostName: String? = null,
        val fontStyle: String? = null,
        val fontStyleStateHostName: String? = null,
        val color: String? = null,
        val colorStateHostName: String? = null,
        val textAlign: String? = null,
        val textAlignStateHostName: String? = null,
        val maxLines: Int? = null,
        val maxLinesStateHostName: String? = null,
        val overflow: String? = null,
        val overflowStateHostName: String? = null,
        val letterSpacing: Double? = null,
        val letterSpacingStateHostName: String? = null,
        val lineHeight: Double? = null,
        val lineHeightStateHostName: String? = null,
        val textDecoration: String? = null,
        val textDecorationStateHostName: String? = null,
        val minLines: Int? = null,
        val minLinesStateHostName: String? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.Icon] component.
     *
     * @property iconName The name of the icon.
     * @property iconNameStateHostName Name of a `StateHost<String>` for dynamic icon selection.
     * @property tint Tint color for the icon as an ARGB hex string (#AARRGGBB or #RRGGBB).
     * @property tintStateHostName Name of a `StateHost<String>` for dynamic tint color.
     * @property contentDescription Accessibility description for the icon.
     */
    @Serializable
    @SerialName("IconProps")
    data class IconProps(
        val iconName: String? = null,
        val iconNameStateHostName: String? = null,
        val tint: String? = null,
        val tintStateHostName: String? = null,
        val contentDescription: String? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.Button] component and its variants (OutlinedButton, TextButton, ElevatedButton, FilledTonalButton, IconButton).
     *
     * @property onClickEventName Name of the [Behavior][com.jesusdmedinac.jsontocompose.behavior.Behavior]
     *   invoked when the button is clicked.
     * @property child The single child node rendered inside the button.
     * @property enabled Whether the button is enabled for interaction.
     * @property enabledStateHostName Name of a `StateHost<Boolean>` for dynamic enabled state.
     */
    @Serializable
    @SerialName("ButtonProps")
    data class ButtonProps(
        val onClickEventName: String? = null,
        val child: ComposeNode? = null,
        val enabled: Boolean? = null,
        val enabledStateHostName: String? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.FloatingActionButton] component.
     *
     * @property onClickEventName Name of the [Behavior][com.jesusdmedinac.jsontocompose.behavior.Behavior]
     *   invoked when the button is clicked.
     * @property icon The icon node rendered inside the FAB.
     * @property containerColor Background color of the FAB as an ARGB hex string (#AARRGGBB or #RRGGBB).
     */
    @Serializable
    @SerialName("FabProps")
    data class FabProps(
        val onClickEventName: String? = null,
        val icon: ComposeNode? = null,
        val containerColor: String? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.ExtendedFloatingActionButton] component.
     *
     * @property onClickEventName Name of the [Behavior][com.jesusdmedinac.jsontocompose.behavior.Behavior]
     *   invoked when the button is clicked.
     * @property icon The icon node rendered inside the FAB.
     * @property text The text node rendered inside the FAB.
     * @property containerColor Background color of the FAB as an ARGB hex string (#AARRGGBB or #RRGGBB).
     */
    @Serializable
    @SerialName("ExtendedFabProps")
    data class ExtendedFabProps(
        val onClickEventName: String? = null,
        val icon: ComposeNode? = null,
        val text: ComposeNode? = null,
        val containerColor: String? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.Column] or [ComposeType.LazyColumn] component.
     *
     * @property children The list of child nodes arranged vertically.
     * @property verticalArrangement Vertical arrangement strategy (e.g., "Top", "Center", "SpaceBetween").
     * @property horizontalAlignment Horizontal alignment of children (e.g., "Start", "CenterHorizontally", "End").
     */
    @Serializable
    @SerialName("ColumnProps")
    data class ColumnProps(
        val children: List<ComposeNode>? = null,
        val verticalArrangement: String? = null,
        val horizontalAlignment: String? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.Row] or [ComposeType.LazyRow] component.
     *
     * @property children The list of child nodes arranged horizontally.
     * @property verticalAlignment Vertical alignment of children (e.g., "Top", "CenterVertically", "Bottom").
     * @property horizontalArrangement Horizontal arrangement strategy (e.g., "Start", "Center", "SpaceEvenly").
     */
    @Serializable
    @SerialName("RowProps")
    data class RowProps(
        val children: List<ComposeNode>? = null,
        val verticalAlignment: String? = null,
        val horizontalArrangement: String? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.Box] component.
     *
     * @property children The list of child nodes stacked on top of each other.
     * @property contentAlignment Two-dimensional alignment for children (e.g., "Center", "TopStart", "BottomEnd").
     * @property propagateMinConstraints Whether to propagate minimum constraints to children.
     * @property propagateMinConstraintsStateHostName Name of a `StateHost<Boolean>` for dynamic control.
     */
    @Serializable
    @SerialName("BoxProps")
    data class BoxProps(
        val children: List<ComposeNode>? = null,
        val contentAlignment: String? = null,
        val propagateMinConstraints: Boolean? = null,
        val propagateMinConstraintsStateHostName: String? = null,
    ) : NodeProperties

    /**
     * Singleton properties that represent a [ComposeType.Spacer] component. [ComposeType.Spacer] on compose just receive
     * a modifier, so SpacerProps is an empty object.
     */
    @Serializable
    @SerialName("SpacerProps")
    data object SpacerProps

    /**
     * Properties for a [ComposeType.Image] component.
     *
     * @property url URL of the image to load remotely.
     * @property urlStateHostName Name of a `StateHost<String>` that provides the URL dynamically.
     * @property resourceName Name of a local drawable resource registered in `LocalDrawableResources`.
     * @property resourceNameStateHostName Name of a `StateHost<String>` for dynamic resource selection.
     * @property contentDescription Accessibility description for the image.
     * @property contentDescriptionStateHostName Name of a `StateHost<String>` for dynamic description.
     * @property contentScale How the image should be scaled (e.g., "Crop", "Fit", "FillBounds").
     * @property contentScaleStateHostName Name of a `StateHost<String>` for dynamic content scale.
     */
    @Serializable
    @SerialName("ImageProps")
    data class ImageProps(
        val url: String? = null,
        val urlStateHostName: String? = null,
        val resourceName: String? = null,
        val resourceNameStateHostName: String? = null,
        val contentDescription: String? = null,
        val contentDescriptionStateHostName: String? = null,
        val contentScale: String? = null,
        val contentScaleStateHostName: String? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.TextField] component.
     *
     * @property value The initial text value of the field.
     * @property valueStateHostName Name of a `StateHost<String>` that provides and receives the text value.
     *   Required for the TextField to render — without it, the field is not displayed.
     * @property placeholder The node rendered as the placeholder text.
     * @property label The node rendered as the label text.
     * @property leadingIcon The node rendered as the leading icon.
     * @property trailingIcon The node rendered as the trailing icon.
     * @property isError Whether the text field is in an error state.
     * @property isErrorStateHostName Name of a `StateHost<Boolean>` for dynamic error state.
     * @property supportingText The node rendered as supporting text.
     * @property singleLine Whether the text field is a single line.
     * @property maxLines The maximum height in terms of maximum number of visible lines.
     * @property keyboardType The keyboard type (e.g., "Text", "Number", "Email", "Password").
     * @property visualTransformation The visual transformation (e.g., "Password", "None").
     * @property readOnly Whether the text field is read only.
     * @property prefix The node rendered as the prefix text.
     * @property suffix The node rendered as the suffix text.
     */
    @Serializable
    @SerialName("TextFieldProps")
    data class TextFieldProps(
        val value: String? = null,
        val valueStateHostName: String? = null,
        val placeholder: ComposeNode? = null,
        val label: ComposeNode? = null,
        val leadingIcon: ComposeNode? = null,
        val trailingIcon: ComposeNode? = null,
        val isError: Boolean? = null,
        val isErrorStateHostName: String? = null,
        val supportingText: ComposeNode? = null,
        val singleLine: Boolean? = null,
        val maxLines: Int? = null,
        val keyboardType: String? = null,
        val visualTransformation: String? = null,
        val readOnly: Boolean? = null,
        val prefix: ComposeNode? = null,
        val suffix: ComposeNode? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.Scaffold] component.
     *
     * @property topBar The node rendered in the top app bar slot.
     * @property bottomBar The node rendered in the bottom bar slot.
     * @property child The main content node.
     */
    @Serializable
    @SerialName("ScaffoldProps")
    data class ScaffoldProps(
        val topBar: ComposeNode? = null,
        val bottomBar: ComposeNode? = null,
        val child: ComposeNode? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.Card] component.
     *
     * @property child The single child node rendered inside the card.
     * @property elevation The card elevation in dp.
     * @property elevationStateHostName Name of a `StateHost<Int>` for dynamic elevation.
     * @property cornerRadius The corner radius in dp.
     * @property cornerRadiusStateHostName Name of a `StateHost<Int>` for dynamic corner radius.
     */
    @Serializable
    @SerialName("CardProps")
    data class CardProps(
        val child: ComposeNode? = null,
        val elevation: Int? = null,
        val elevationStateHostName: String? = null,
        val cornerRadius: Int? = null,
        val cornerRadiusStateHostName: String? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.OutlinedCard] component.
     *
     * @property child The single child node rendered inside the card.
     * @property borderColor Custom border color as an ARGB hex string (#AARRGGBB or #RRGGBB).
     * @property borderColorStateHostName Name of a `StateHost<String>` for dynamic border color.
     * @property cornerRadius The corner radius in dp.
     * @property cornerRadiusStateHostName Name of a `StateHost<Int>` for dynamic corner radius.
     */
    @Serializable
    @SerialName("OutlinedCardProps")
    data class OutlinedCardProps(
        val child: ComposeNode? = null,
        val borderColor: String? = null,
        val borderColorStateHostName: String? = null,
        val cornerRadius: Int? = null,
        val cornerRadiusStateHostName: String? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.AlertDialog] component.
     *
     * @property confirmButton The node rendered as the confirm action button.
     * @property dismissButton The node rendered as the dismiss action button.
     * @property title The node rendered as the dialog title.
     * @property text The node rendered as the dialog content text.
     * @property backgroundColor Background color of the dialog as an ARGB integer.
     * @property contentColor Content color of the dialog as an ARGB integer.
     * @property visibilityStateHostName Name of the `StateHost<Boolean>` that controls whether the dialog
     *   is visible. When `state == false`, the dialog is not rendered. On confirm or dismiss,
     *   the renderer sets the state to `false` automatically.
     * @property onDismissRequestEventName Name of the [Behavior][com.jesusdmedinac.jsontocompose.behavior.Behavior]
     *   invoked when the dialog is dismissed.
     */
    @Serializable
    @SerialName("AlertDialogProps")
    data class AlertDialogProps(
        val confirmButton: ComposeNode? = null,
        val dismissButton: ComposeNode? = null,
        val title: ComposeNode? = null,
        val text: ComposeNode? = null,
        val backgroundColor: String? = null,
        val contentColor: String? = null,
        val visibilityStateHostName: String? = null,
        val onDismissRequestEventName: String? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.TopAppBar] component.
     *
     * @property title The node rendered as the app bar title.
     * @property navigationIcon The node rendered as the navigation icon (typically an Image or Button).
     * @property actions List of nodes rendered as action items in the app bar.
     * @property backgroundColor Background color as an ARGB hex string (#AARRGGBB or #RRGGBB).
     * @property contentColor Content color as an ARGB hex string (#AARRGGBB or #RRGGBB).
     */
    @Serializable
    @SerialName("TopAppBarProps")
    data class TopAppBarProps(
        val title: ComposeNode? = null,
        val navigationIcon: ComposeNode? = null,
        val actions: List<ComposeNode>? = null,
        val backgroundColor: String? = null,
        val contentColor: String? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.NavigationBar] component.
     *
     * @property children List of child nodes (typically [ComposeType.NavigationBarItem] nodes).
     * @property containerColor Background color as an ARGB hex string (#AARRGGBB or #RRGGBB).
     * @property contentColor Content color as an ARGB hex string (#AARRGGBB or #RRGGBB).
     */
    @Serializable
    @SerialName("NavigationBarProps")
    data class NavigationBarProps(
        val children: List<ComposeNode>? = null,
        val containerColor: String? = null,
        val contentColor: String? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.NavigationBarItem] component.
     *
     * @property selected Whether this item is currently selected.
     * @property selectedStateHostName Name of a `StateHost<Boolean>` for dynamic selection state.
     * @property onClickEventName Name of the [Behavior][com.jesusdmedinac.jsontocompose.behavior.Behavior]
     *   invoked when the item is clicked.
     * @property label The node rendered as the item label (typically a Text node).
     * @property icon The node rendered as the item icon (typically an Image or Icon node).
     * @property enabled Whether the item is enabled for interaction.
     * @property enabledStateHostName Name of a `StateHost<Boolean>` for dynamic enabled state.
     * @property alwaysShowLabel Whether to always show the label, even when not selected.
     * @property alwaysShowLabelStateHostName Name of a `StateHost<Boolean>` for dynamic label visibility.
     */
    @Serializable
    @SerialName("NavigationBarItemProps")
    data class NavigationBarItemProps(
        val selected: Boolean? = null,
        val selectedStateHostName: String? = null,
        val onClickEventName: String? = null,
        val label: ComposeNode? = null,
        val icon: ComposeNode? = null,
        val enabled: Boolean? = null,
        val enabledStateHostName: String? = null,
        val alwaysShowLabel: Boolean? = null,
        val alwaysShowLabelStateHostName: String? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.NavigationRail] component.
     *
     * @property children List of child nodes (typically [ComposeType.NavigationRailItem] nodes).
     * @property containerColor Background color as an ARGB hex string (#AARRGGBB or #RRGGBB).
     * @property contentColor Content color as an ARGB hex string (#AARRGGBB or #RRGGBB).
     * @property header The node rendered at the top of the rail (typically a FAB).
     */
    @Serializable
    @SerialName("NavigationRailProps")
    data class NavigationRailProps(
        val children: List<ComposeNode>? = null,
        val containerColor: String? = null,
        val contentColor: String? = null,
        val header: ComposeNode? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.NavigationRailItem] component.
     *
     * @property selected Whether this item is currently selected.
     * @property selectedStateHostName Name of a `StateHost<Boolean>` for dynamic selection state.
     * @property onClickEventName Name of the [Behavior][com.jesusdmedinac.jsontocompose.behavior.Behavior]
     *   invoked when the item is clicked.
     * @property label The node rendered as the item label (typically a Text node).
     * @property icon The node rendered as the item icon (typically an Image or Icon node).
     * @property enabled Whether the item is enabled for interaction.
     * @property enabledStateHostName Name of a `StateHost<Boolean>` for dynamic enabled state.
     * @property alwaysShowLabel Whether to always show the label, even when not selected.
     * @property alwaysShowLabelStateHostName Name of a `StateHost<Boolean>` for dynamic label visibility.
     */
    @Serializable
    @SerialName("NavigationRailItemProps")
    data class NavigationRailItemProps(
        val selected: Boolean? = null,
        val selectedStateHostName: String? = null,
        val onClickEventName: String? = null,
        val label: ComposeNode? = null,
        val icon: ComposeNode? = null,
        val enabled: Boolean? = null,
        val enabledStateHostName: String? = null,
        val alwaysShowLabel: Boolean? = null,
        val alwaysShowLabelStateHostName: String? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.ModalNavigationDrawer] component.
     *
     * @property drawerContent List of nodes rendered inside the drawer sheet.
     * @property isOpen Whether the drawer is currently open.
     * @property isOpenStateHostName Name of a `StateHost<Boolean>` for dynamic open/close control.
     * @property onDismissRequestEventName Name of the [Behavior][com.jesusdmedinac.jsontocompose.behavior.Behavior]
     *   invoked when the drawer is dismissed.
     * @property child The main content node.
     */
    @Serializable
    @SerialName("NavigationDrawerProps")
    data class NavigationDrawerProps(
        val drawerContent: List<ComposeNode>? = null,
        val isOpen: Boolean? = null,
        val isOpenStateHostName: String? = null,
        val onDismissRequestEventName: String? = null,
        val child: ComposeNode? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.NavigationDrawerItem] component.
     *
     * @property label The node rendered as the item label (typically a Text node).
     * @property selected Whether this item is currently selected.
     * @property selectedStateHostName Name of a `StateHost<Boolean>` for dynamic selection state.
     * @property onClickEventName Name of the [Behavior][com.jesusdmedinac.jsontocompose.behavior.Behavior]
     *   invoked when the item is clicked.
     * @property icon The node rendered as the item icon.
     * @property badge The node rendered as the item badge.
     */
    @Serializable
    @SerialName("NavigationDrawerItemProps")
    data class NavigationDrawerItemProps(
        val label: ComposeNode? = null,
        val selected: Boolean? = null,
        val selectedStateHostName: String? = null,
        val onClickEventName: String? = null,
        val icon: ComposeNode? = null,
        val badge: ComposeNode? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.TabRow] or [ComposeType.ScrollableTabRow] component.
     *
     * @property selectedTabIndex The index of the currently selected tab.
     * @property selectedTabIndexStateHostName Name of a `StateHost<Int>` for dynamic tab index.
     * @property containerColor Background color as an ARGB hex string (#AARRGGBB or #RRGGBB).
     * @property contentColor Content color as an ARGB hex string (#AARRGGBB or #RRGGBB).
     * @property children List of child nodes (typically [ComposeType.Tab] nodes).
     */
    @Serializable
    @SerialName("TabRowProps")
    data class TabRowProps(
        val selectedTabIndex: Int? = null,
        val selectedTabIndexStateHostName: String? = null,
        val containerColor: String? = null,
        val contentColor: String? = null,
        val children: List<ComposeNode>? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.Tab] component.
     *
     * @property selected Whether this tab is currently selected.
     * @property selectedStateHostName Name of a `StateHost<Boolean>` for dynamic selection state.
     * @property onClickEventName Name of the [Behavior][com.jesusdmedinac.jsontocompose.behavior.Behavior]
     *   invoked when the tab is clicked.
     * @property text The node rendered as the tab text.
     * @property icon The node rendered as the tab icon.
     * @property enabled Whether the tab is enabled for interaction.
     * @property enabledStateHostName Name of a `StateHost<Boolean>` for dynamic enabled state.
     */
    @Serializable
    @SerialName("TabProps")
    data class TabProps(
        val selected: Boolean? = null,
        val selectedStateHostName: String? = null,
        val onClickEventName: String? = null,
        val text: ComposeNode? = null,
        val icon: ComposeNode? = null,
        val enabled: Boolean? = null,
        val enabledStateHostName: String? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.BottomBar] component.
     *
     * @property children List of child nodes (typically [ComposeType.BottomNavigationItem] nodes).
     * @property backgroundColor Background color as an ARGB hex string (#AARRGGBB or #RRGGBB).
     * @property contentColor Content color as an ARGB hex string (#AARRGGBB or #RRGGBB).
     */
    @Serializable
    @SerialName("BottomBarProps")
    data class BottomBarProps(
        val children: List<ComposeNode>? = null,
        val backgroundColor: String? = null,
        val contentColor: String? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.BottomNavigationItem] component.
     *
     * @property selected Whether this item is currently selected.
     * @property selectedStateHostName Name of a `StateHost<Boolean>` for dynamic selection state.
     * @property onClickEventName Name of the [Behavior][com.jesusdmedinac.jsontocompose.behavior.Behavior]
     *   invoked when the item is clicked.
     * @property label The node rendered as the item label (typically a Text node).
     * @property icon The node rendered as the item icon (typically an Image node).
     * @property enabled Whether the item is enabled for interaction.
     * @property enabledStateHostName Name of a `StateHost<Boolean>` for dynamic enabled state.
     * @property alwaysShowLabel Whether to always show the label, even when not selected.
     * @property alwaysShowLabelStateHostName Name of a `StateHost<Boolean>` for dynamic label visibility.
     */
    @Serializable
    @SerialName("BottomNavigationItemProps")
    data class BottomNavigationItemProps(
        val selected: Boolean? = null,
        val selectedStateHostName: String? = null,
        val onClickEventName: String? = null,
        val label: ComposeNode? = null,
        val icon: ComposeNode? = null,
        val enabled: Boolean? = null,
        val enabledStateHostName: String? = null,
        val alwaysShowLabel: Boolean? = null,
        val alwaysShowLabelStateHostName: String? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.Switch] component.
     *
     * @property checked Whether the switch is in the on position.
     * @property checkedStateHostName Name of a `StateHost<Boolean>` for dynamic checked state.
     * @property onCheckedChangeEventName Name of the [Behavior][com.jesusdmedinac.jsontocompose.behavior.Behavior]
     *   invoked when the switch state changes.
     * @property enabled Whether the switch is enabled for interaction.
     * @property enabledStateHostName Name of a `StateHost<Boolean>` for dynamic enabled state.
     */
    @Serializable
    @SerialName("SwitchProps")
    data class SwitchProps(
        val checked: Boolean? = null,
        val checkedStateHostName: String? = null,
        val onCheckedChangeEventName: String? = null,
        val enabled: Boolean? = null,
        val enabledStateHostName: String? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.Checkbox] component.
     *
     * @property checked Whether the checkbox is checked.
     * @property checkedStateHostName Name of a `StateHost<Boolean>` for dynamic checked state.
     * @property onCheckedChangeEventName Name of the [Behavior][com.jesusdmedinac.jsontocompose.behavior.Behavior]
     *   invoked when the checkbox state changes.
     * @property enabled Whether the checkbox is enabled for interaction.
     * @property enabledStateHostName Name of a `StateHost<Boolean>` for dynamic enabled state.
     */
    @Serializable
    @SerialName("CheckboxProps")
    data class CheckboxProps(
        val checked: Boolean? = null,
        val checkedStateHostName: String? = null,
        val onCheckedChangeEventName: String? = null,
        val enabled: Boolean? = null,
        val enabledStateHostName: String? = null,
    ) : NodeProperties

    // --- Phase 3: Input Components ---

    /** Range of values for a [ComposeType.Slider] component. */
    @Serializable
    data class FloatRange(
        val start: Float = 0f,
        val endInclusive: Float = 1f,
    )

    /** Properties for a [ComposeType.Slider] component. */
    @Serializable
    @SerialName("SliderProps")
    data class SliderProps(
        val value: Float? = null,
        val valueStateHostName: String? = null,
        val valueRange: FloatRange? = null,
        val steps: Int? = null,
        val onValueChangeEventName: String? = null,
        val onValueChangeFinishedEventName: String? = null,
        val enabled: Boolean? = null,
        val enabledStateHostName: String? = null,
    ) : NodeProperties

    /** Properties for a [ComposeType.RadioButton] component. */
    @Serializable
    @SerialName("RadioButtonProps")
    data class RadioButtonProps(
        val selected: Boolean? = null,
        val selectedStateHostName: String? = null,
        val onClickEventName: String? = null,
        val enabled: Boolean? = null,
        val enabledStateHostName: String? = null,
    ) : NodeProperties

    /** Properties for [ComposeType.SingleChoiceSegmentedButtonRow] and [ComposeType.MultiChoiceSegmentedButtonRow]. */
    @Serializable
    @SerialName("SegmentedButtonRowProps")
    data class SegmentedButtonRowProps(
        val children: List<ComposeNode>? = null,
    ) : NodeProperties

    /** Properties for a [ComposeType.SegmentedButton] component. */
    @Serializable
    @SerialName("SegmentedButtonProps")
    data class SegmentedButtonProps(
        val selected: Boolean? = null,
        val selectedStateHostName: String? = null,
        val onClickEventName: String? = null,
        val label: ComposeNode? = null,
        val icon: ComposeNode? = null,
        val enabled: Boolean? = null,
        val enabledStateHostName: String? = null,
    ) : NodeProperties

    /** Properties for a [ComposeType.DatePicker] component. */
    @Serializable
    @SerialName("DatePickerProps")
    data class DatePickerProps(
        val selectedDateMillis: Long? = null,
        val selectedDateMillisStateHostName: String? = null,
    ) : NodeProperties

    /** Properties for a [ComposeType.TimePicker] component. */
    @Serializable
    @SerialName("TimePickerProps")
    data class TimePickerProps(
        val hour: Int? = null,
        val hourStateHostName: String? = null,
        val minute: Int? = null,
        val minuteStateHostName: String? = null,
        val is24Hour: Boolean? = null,
    ) : NodeProperties

    /** Properties for a [ComposeType.SearchBar] component. */
    @Serializable
    @SerialName("SearchBarProps")
    data class SearchBarProps(
        val query: String? = null,
        val queryStateHostName: String? = null,
        val placeholder: ComposeNode? = null,
        val leadingIcon: ComposeNode? = null,
        val trailingIcon: ComposeNode? = null,
        val active: Boolean? = null,
        val activeStateHostName: String? = null,
        val onQueryChangeEventName: String? = null,
        val onSearchEventName: String? = null,
        val children: List<ComposeNode>? = null,
    ) : NodeProperties

    // --- Phase 3: Layout Components ---

    /** Properties for [ComposeType.HorizontalDivider] and [ComposeType.VerticalDivider]. */
    @Serializable
    @SerialName("DividerProps")
    data class DividerProps(
        val thickness: Int? = null,
        val color: String? = null,
    ) : NodeProperties

    /** Properties for a [ComposeType.FlowRow] component. */
    @Serializable
    @SerialName("FlowRowProps")
    data class FlowRowProps(
        val children: List<ComposeNode>? = null,
        val horizontalArrangement: String? = null,
        val verticalArrangement: String? = null,
    ) : NodeProperties

    /** Properties for a [ComposeType.FlowColumn] component. */
    @Serializable
    @SerialName("FlowColumnProps")
    data class FlowColumnProps(
        val children: List<ComposeNode>? = null,
        val horizontalArrangement: String? = null,
        val verticalArrangement: String? = null,
    ) : NodeProperties

    /** Properties for a [ComposeType.Surface] component. */
    @Serializable
    @SerialName("SurfaceProps")
    data class SurfaceProps(
        val child: ComposeNode? = null,
        val tonalElevation: Int? = null,
        val shadowElevation: Int? = null,
        val color: String? = null,
        val shape: String? = null,
    ) : NodeProperties

    // --- Phase 3: Pager Components ---

    /** Properties for [ComposeType.HorizontalPager] and [ComposeType.VerticalPager]. */
    @Serializable
    @SerialName("PagerProps")
    data class PagerProps(
        val pages: List<ComposeNode>? = null,
        val currentPage: Int? = null,
        val currentPageStateHostName: String? = null,
        val beyondViewportPageCount: Int? = null,
        val userScrollEnabled: Boolean? = null,
        val userScrollEnabledStateHostName: String? = null,
    ) : NodeProperties

    // --- Phase 3: ModalBottomSheet ---

    /** Properties for a [ComposeType.ModalBottomSheet] component. */
    @Serializable
    @SerialName("ModalBottomSheetProps")
    data class ModalBottomSheetProps(
        val child: ComposeNode? = null,
        val visible: Boolean? = null,
        val visibleStateHostName: String? = null,
        val onDismissRequestEventName: String? = null,
        val dragHandle: ComposeNode? = null,
        val shape: String? = null,
        val scrimColor: String? = null,
    ) : NodeProperties

    // --- Phase 3: Display Components ---

    /** Properties for a [ComposeType.Badge] component. */
    @Serializable
    @SerialName("BadgeProps")
    data class BadgeProps(
        val text: String? = null,
        val textStateHostName: String? = null,
        val containerColor: String? = null,
        val contentColor: String? = null,
    ) : NodeProperties

    /** Properties for a [ComposeType.BadgedBox] component. */
    @Serializable
    @SerialName("BadgedBoxProps")
    data class BadgedBoxProps(
        val badge: ComposeNode? = null,
        val child: ComposeNode? = null,
    ) : NodeProperties

    /** Properties for [ComposeType.AssistChip] and [ComposeType.SuggestionChip]. */
    @Serializable
    @SerialName("ChipProps")
    data class ChipProps(
        val label: ComposeNode? = null,
        val leadingIcon: ComposeNode? = null,
        val onClickEventName: String? = null,
        val enabled: Boolean? = null,
        val enabledStateHostName: String? = null,
    ) : NodeProperties

    /** Properties for a [ComposeType.FilterChip] component. */
    @Serializable
    @SerialName("FilterChipProps")
    data class FilterChipProps(
        val selected: Boolean? = null,
        val selectedStateHostName: String? = null,
        val label: ComposeNode? = null,
        val leadingIcon: ComposeNode? = null,
        val onClickEventName: String? = null,
        val enabled: Boolean? = null,
        val enabledStateHostName: String? = null,
    ) : NodeProperties

    /** Properties for a [ComposeType.InputChip] component. */
    @Serializable
    @SerialName("InputChipProps")
    data class InputChipProps(
        val selected: Boolean? = null,
        val selectedStateHostName: String? = null,
        val label: ComposeNode? = null,
        val leadingIcon: ComposeNode? = null,
        val trailingIcon: ComposeNode? = null,
        val onClickEventName: String? = null,
        val enabled: Boolean? = null,
        val enabledStateHostName: String? = null,
    ) : NodeProperties

    /** Properties for [ComposeType.CircularProgressIndicator] and [ComposeType.LinearProgressIndicator]. */
    @Serializable
    @SerialName("ProgressIndicatorProps")
    data class ProgressIndicatorProps(
        val progress: Float? = null,
        val progressStateHostName: String? = null,
        val color: String? = null,
        val trackColor: String? = null,
    ) : NodeProperties

    /** Properties for a [ComposeType.PlainTooltip] component. */
    @Serializable
    @SerialName("PlainTooltipProps")
    data class PlainTooltipProps(
        val text: String? = null,
        val anchor: ComposeNode? = null,
    ) : NodeProperties

    /** Properties for a [ComposeType.RichTooltip] component. */
    @Serializable
    @SerialName("RichTooltipProps")
    data class RichTooltipProps(
        val title: ComposeNode? = null,
        val text: ComposeNode? = null,
        val action: ComposeNode? = null,
        val anchor: ComposeNode? = null,
    ) : NodeProperties

    // --- Phase 3: Snackbar ---

    /** Properties for a [ComposeType.SnackbarHost] component. */
    @Serializable
    @SerialName("SnackbarHostProps")
    data class SnackbarHostProps(
        val snackbarHostStateHostName: String? = null,
    ) : NodeProperties

    // --- Phase 3: ListItem ---

    /** Properties for a [ComposeType.ListItem] component. */
    @Serializable
    @SerialName("ListItemProps")
    data class ListItemProps(
        val headlineContent: ComposeNode? = null,
        val supportingContent: ComposeNode? = null,
        val overlineContent: ComposeNode? = null,
        val leadingContent: ComposeNode? = null,
        val trailingContent: ComposeNode? = null,
        val onClickEventName: String? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.Custom] component.
     *
     * Custom components are rendered by user-provided composable functions registered
     * in `LocalCustomRenderers`.
     *
     * @property customType The key used to look up the custom renderer in `LocalCustomRenderers`.
     * @property customData Arbitrary JSON data passed to the custom renderer.
     */
    @Serializable
    @SerialName("CustomProps")
    data class CustomProps(
        val customType: String,
        val customData: JsonObject? = null,
    ) : NodeProperties
}
