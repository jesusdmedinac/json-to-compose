package com.jesusdmedinac.jsontocompose.model

import com.jesusdmedinac.jsontocompose.modifier.ModifierOperation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Holds the list of modifier operations applied to a [ComposeNode].
 *
 * Modifiers are applied in order during rendering, allowing composition of
 * layout, styling, and visual effects (padding, background, border, etc.).
 *
 * @property operations Ordered list of modifier operations to apply.
 */
@Serializable
@SerialName("ComposeModifier")
data class ComposeModifier(
    val operations: List<Operation> = emptyList(),
) {
    /**
     * Sealed class representing a single modifier operation.
     *
     * Each subclass maps to a Compose `Modifier` function (e.g., [Padding] maps to
     * `Modifier.padding()`, [Border] maps to `Modifier.border()`).
     *
     * @property modifierOperation The corresponding [ModifierOperation] enum value.
     *   Transient — not serialized.
     */
    @Serializable
    @SerialName("Operation")
    sealed class Operation(
        @Transient
        val modifierOperation: ModifierOperation? = null,
    ) {
        /**
         * Applies uniform padding around the content.
         *
         * @property value Padding size in dp. Must be non-negative.
         */
        @Serializable
        @SerialName("Padding")
        data class Padding(val value: Int) : Operation(
            modifierOperation = ModifierOperation.Padding
        )

        /** Makes the component fill the maximum available size in both axes. */
        @Serializable
        @SerialName("FillMaxSize")
        data object FillMaxSize : Operation(
            modifierOperation = ModifierOperation.FillMaxSize
        )

        /** Makes the component fill the maximum available width. */
        @Serializable
        @SerialName("FillMaxWidth")
        data object FillMaxWidth : Operation(
            modifierOperation = ModifierOperation.FillMaxWidth
        )

        /** Makes the component fill the maximum available height. */
        @Serializable
        @SerialName("FillMaxHeight")
        data object FillMaxHeight : Operation(
            modifierOperation = ModifierOperation.FillMaxHeight
        )

        /**
         * Sets a fixed width for the component.
         *
         * @property value Width in dp. Must be non-negative.
         */
        @Serializable
        @SerialName("Width")
        data class Width(val value: Int) : Operation(
            modifierOperation = ModifierOperation.Width
        )

        /**
         * Sets a fixed height for the component.
         *
         * @property value Height in dp. Must be non-negative.
         */
        @Serializable
        @SerialName("Height")
        data class Height(val value: Int) : Operation(
            modifierOperation = ModifierOperation.Height
        )

        /**
         * Applies a solid background color.
         *
         * @property hexColor Color in `#AARRGGBB` hex format (e.g., `"#FF0000FF"` for opaque blue).
         */
        @Serializable
        @SerialName("BackgroundColor")
        data class BackgroundColor(val hexColor: String) : Operation(
            modifierOperation = ModifierOperation.BackgroundColor
        )

        /**
         * Draws a border around the component.
         *
         * @property width Border width in dp. Must be non-negative.
         * @property color Border color in `#AARRGGBB` hex format.
         * @property shape Shape of the border (defaults to [ComposeShape.Rectangle]).
         */
        @Serializable
        @SerialName("Border")
        data class Border(
            val width: Int,
            val color: String,
            val shape: ComposeShape = ComposeShape.Rectangle
        ) : Operation(
            modifierOperation = ModifierOperation.Border
        )

        /**
         * Draws a shadow (elevation effect) beneath the component.
         *
         * @property elevation Shadow elevation in dp. Must be non-negative.
         * @property shape Shape of the shadow (defaults to [ComposeShape.Rectangle]).
         * @property clip Whether to clip the content to the shadow shape.
         */
        @Serializable
        @SerialName("Shadow")
        data class Shadow(
            val elevation: Int,
            val shape: ComposeShape = ComposeShape.Rectangle,
            val clip: Boolean = false
        ) : Operation(
            modifierOperation = ModifierOperation.Shadow
        )

        /**
         * Clips the content to a specific shape.
         *
         * @property shape The clipping shape.
         */
        @Serializable
        @SerialName("Clip")
        data class Clip(
            val shape: ComposeShape
        ) : Operation(
            modifierOperation = ModifierOperation.Clip
        )

        /**
         * Applies a background color with a specific shape.
         *
         * @property color Background color in `#AARRGGBB` hex format.
         * @property shape Shape of the background (defaults to [ComposeShape.Rectangle]).
         */
        @Serializable
        @SerialName("Background")
        data class Background(
            val color: String,
            val shape: ComposeShape = ComposeShape.Rectangle
        ) : Operation(
            modifierOperation = ModifierOperation.Background
        )

        /**
         * Sets the opacity of the component.
         *
         * @property value Alpha value between 0.0 (fully transparent) and 1.0 (fully opaque).
         */
        @Serializable
        @SerialName("Alpha")
        data class Alpha(
            val value: Float
        ) : Operation(
            modifierOperation = ModifierOperation.Alpha
        )

        /**
         * Rotates the component by the specified angle.
         *
         * @property degrees Rotation angle in degrees. Positive values rotate clockwise.
         */
        @Serializable
        @SerialName("Rotate")
        data class Rotate(
            val degrees: Float
        ) : Operation(
            modifierOperation = ModifierOperation.Rotate
        )

        // --- Phase 3: Missing Modifiers ---

        /** Makes the component clickable. Event resolved via LocalBehavior. */
        @Serializable
        @SerialName("Clickable")
        data class Clickable(
            val onClickEventName: String? = null
        ) : Operation(
            modifierOperation = ModifierOperation.Clickable
        )

        /** Assigns a weight within a Row or Column scope. */
        @Serializable
        @SerialName("Weight")
        data class Weight(
            val weight: Float
        ) : Operation(
            modifierOperation = ModifierOperation.Weight
        )

        /** Enables vertical scrolling on the component. */
        @Serializable
        @SerialName("VerticalScroll")
        data object VerticalScroll : Operation(
            modifierOperation = ModifierOperation.VerticalScroll
        )

        /** Enables horizontal scrolling on the component. */
        @Serializable
        @SerialName("HorizontalScroll")
        data object HorizontalScroll : Operation(
            modifierOperation = ModifierOperation.HorizontalScroll
        )

        /**
         * Offsets the component by x and y dp.
         *
         * @property x Horizontal offset in dp (positive = right).
         * @property y Vertical offset in dp (positive = down).
         */
        @Serializable
        @SerialName("Offset")
        data class Offset(
            val x: Int = 0,
            val y: Int = 0
        ) : Operation(
            modifierOperation = ModifierOperation.Offset
        )

        /**
         * Sets explicit width and height for the component.
         *
         * @property width Width in dp.
         * @property height Height in dp.
         */
        @Serializable
        @SerialName("Size")
        data class Size(
            val width: Int,
            val height: Int
        ) : Operation(
            modifierOperation = ModifierOperation.Size
        )

        /** Wraps the component's width to its content. */
        @Serializable
        @SerialName("WrapContentWidth")
        data object WrapContentWidth : Operation(
            modifierOperation = ModifierOperation.WrapContentWidth
        )

        /** Wraps the component's height to its content. */
        @Serializable
        @SerialName("WrapContentHeight")
        data object WrapContentHeight : Operation(
            modifierOperation = ModifierOperation.WrapContentHeight
        )

        /**
         * Constrains the component to a specific aspect ratio.
         *
         * @property ratio Width-to-height ratio (e.g., 16f/9f for 16:9).
         */
        @Serializable
        @SerialName("AspectRatio")
        data class AspectRatio(
            val ratio: Float
        ) : Operation(
            modifierOperation = ModifierOperation.AspectRatio
        )

        /**
         * Sets the z-index for overlay ordering.
         *
         * @property zIndex Z-index value. Higher values are drawn on top.
         */
        @Serializable
        @SerialName("ZIndex")
        data class ZIndex(
            val zIndex: Float
        ) : Operation(
            modifierOperation = ModifierOperation.ZIndex
        )

        /**
         * Sets a minimum width for the component.
         *
         * @property value Minimum width in dp.
         */
        @Serializable
        @SerialName("MinWidth")
        data class MinWidth(val value: Int) : Operation(
            modifierOperation = ModifierOperation.MinWidth
        )

        /**
         * Sets a minimum height for the component.
         *
         * @property value Minimum height in dp.
         */
        @Serializable
        @SerialName("MinHeight")
        data class MinHeight(val value: Int) : Operation(
            modifierOperation = ModifierOperation.MinHeight
        )

        /**
         * Sets a maximum width for the component.
         *
         * @property value Maximum width in dp.
         */
        @Serializable
        @SerialName("MaxWidth")
        data class MaxWidth(val value: Int) : Operation(
            modifierOperation = ModifierOperation.MaxWidth
        )

        /**
         * Sets a maximum height for the component.
         *
         * @property value Maximum height in dp.
         */
        @Serializable
        @SerialName("MaxHeight")
        data class MaxHeight(val value: Int) : Operation(
            modifierOperation = ModifierOperation.MaxHeight
        )

        /** Animates size changes of the component's content. */
        @Serializable
        @SerialName("AnimateContentSize")
        data object AnimateContentSize : Operation(
            modifierOperation = ModifierOperation.AnimateContentSize
        )

        /**
         * Applies a test tag for UI test identification.
         *
         * @property tag The tag string used for test identification.
         */
        @Serializable
        @SerialName("TestTag")
        data class TestTag(
            val tag: String
        ) : Operation(
            modifierOperation = ModifierOperation.TestTag
        )
    }
}
