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
    }
}
