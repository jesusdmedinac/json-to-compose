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
        val color: Int? = null,
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
     * @property tint Tint color for the icon as an ARGB integer.
     * @property tintStateHostName Name of a `StateHost<Int>` for dynamic tint color.
     * @property contentDescription Accessibility description for the icon.
     */
    @Serializable
    @SerialName("IconProps")
    data class IconProps(
        val iconName: String? = null,
        val iconNameStateHostName: String? = null,
        val tint: Int? = null,
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
     * @property containerColor Background color of the FAB as an ARGB integer.
     */
    @Serializable
    @SerialName("FabProps")
    data class FabProps(
        val onClickEventName: String? = null,
        val icon: ComposeNode? = null,
        val containerColor: Int? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.ExtendedFloatingActionButton] component.
     *
     * @property onClickEventName Name of the [Behavior][com.jesusdmedinac.jsontocompose.behavior.Behavior]
     *   invoked when the button is clicked.
     * @property icon The icon node rendered inside the FAB.
     * @property text The text node rendered inside the FAB.
     * @property containerColor Background color of the FAB as an ARGB integer.
     */
    @Serializable
    @SerialName("ExtendedFabProps")
    data class ExtendedFabProps(
        val onClickEventName: String? = null,
        val icon: ComposeNode? = null,
        val text: ComposeNode? = null,
        val containerColor: Int? = null,
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
     */
    @Serializable
    @SerialName("TextFieldProps")
    data class TextFieldProps(
        val value: String? = null,
        val valueStateHostName: String? = null,
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
        val backgroundColor: Int? = null,
        val contentColor: Int? = null,
        val visibilityStateHostName: String? = null,
        val onDismissRequestEventName: String? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.TopAppBar] component.
     *
     * @property title The node rendered as the app bar title.
     * @property navigationIcon The node rendered as the navigation icon (typically an Image or Button).
     * @property actions List of nodes rendered as action items in the app bar.
     * @property backgroundColor Background color as an ARGB integer.
     * @property contentColor Content color as an ARGB integer.
     */
    @Serializable
    @SerialName("TopAppBarProps")
    data class TopAppBarProps(
        val title: ComposeNode? = null,
        val navigationIcon: ComposeNode? = null,
        val actions: List<ComposeNode>? = null,
        val backgroundColor: Int? = null,
        val contentColor: Int? = null,
    ) : NodeProperties

    /**
     * Properties for a [ComposeType.BottomBar] component.
     *
     * @property children List of child nodes (typically [ComposeType.BottomNavigationItem] nodes).
     * @property backgroundColor Background color as an ARGB integer.
     * @property contentColor Content color as an ARGB integer.
     */
    @Serializable
    @SerialName("BottomBarProps")
    data class BottomBarProps(
        val children: List<ComposeNode>? = null,
        val backgroundColor: Int? = null,
        val contentColor: Int? = null,
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
