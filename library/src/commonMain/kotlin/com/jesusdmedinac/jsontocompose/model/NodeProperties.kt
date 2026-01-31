package com.jesusdmedinac.jsontocompose.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
@SerialName("NodeProperties")
sealed interface NodeProperties {
    @Serializable
    @SerialName("TextProps")
    data class TextProps(
        val text: String? = null,
    ) : NodeProperties

    @Serializable
    @SerialName("ButtonProps")
    data class ButtonProps(
        val onClickEventName: String? = null,
        val child: ComposeNode? = null,
    ) : NodeProperties

    @Serializable
    @SerialName("ColumnProps")
    data class ColumnProps(
        val children: List<ComposeNode>? = null,
        val verticalArrangement: String? = null,
        val horizontalAlignment: String? = null,
    ) : NodeProperties

    @Serializable
    @SerialName("RowProps")
    data class RowProps(
        val children: List<ComposeNode>? = null,
        val verticalAlignment: String? = null,
        val horizontalArrangement: String? = null,
    ) : NodeProperties

    @Serializable
    @SerialName("BoxProps")
    data class BoxProps(
        val children: List<ComposeNode>? = null,
        val contentAlignment: String? = null,
        val propagateMinConstraints: Boolean? = false,
    ) : NodeProperties

    @Serializable
    @SerialName("ImageProps")
    data class ImageProps(
        val url: String? = null,
        val resourceName: String? = null,
        val contentDescription: String? = null,
        val contentScale: String = "Fit"
    ) : NodeProperties

    @Serializable
    @SerialName("TextFieldProps")
    data class TextFieldProps(
        /**
         * Name of the [StateHost]<[String]> that provides and receives the text field's current value.
         * The host app must register a `StateHost<String>` with this name in [LocalStateHost].
         */
        val valueStateHostName: String? = null,
    ) : NodeProperties

    @Serializable
    @SerialName("ScaffoldProps")
    data class ScaffoldProps(
        val child: ComposeNode? = null,
    ) : NodeProperties

    @Serializable
    @SerialName("CardProps")
    data class CardProps(
        val child: ComposeNode? = null,
        val elevation: Int? = null,
        val cornerRadius: Int? = null,
    ) : NodeProperties

    @Serializable
    @SerialName("DialogProps")
    data class DialogProps(
        val title: String? = null,
        val content: String? = null,
        val child: ComposeNode? = null,
        val confirmButtonText: String? = null,
        val dismissButtonText: String? = null,
        val onConfirmEventName: String? = null,
        val onDismissEventName: String? = null,
        /**
         * Name of the [StateHost]<[Boolean]> that controls whether the dialog is visible.
         * When `state == false`, the dialog is not rendered. On confirm or dismiss,
         * the renderer sets the state to `false` automatically.
         * The host app must register a `StateHost<Boolean>` with this name in [LocalStateHost].
         */
        val visibilityStateHostName: String? = null,
    ) : NodeProperties

    @Serializable
    @SerialName("CustomProps")
    data class CustomProps(
        val customType: String,
        val customData: JsonObject? = null,
    ) : NodeProperties
}
