package com.jesusdmedinac.jsontocompose.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    @SerialName("LayoutProps")
    data class LayoutProps(
        val children: List<ComposeNode>? = null,
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
        val onTextChangeEventName: String? = null,
    ) : NodeProperties
}
