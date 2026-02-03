package com.jesusdmedinac.jsontocompose.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ComposeShape {
    @Serializable
    @SerialName("Rectangle")
    data object Rectangle : ComposeShape()

    @Serializable
    @SerialName("Circle")
    data object Circle : ComposeShape()

    @Serializable
    @SerialName("RoundedCorner")
    data class RoundedCorner(
        val all: Int? = null,
        val topStart: Int? = null,
        val topEnd: Int? = null,
        val bottomStart: Int? = null,
        val bottomEnd: Int? = null
    ) : ComposeShape()
}
