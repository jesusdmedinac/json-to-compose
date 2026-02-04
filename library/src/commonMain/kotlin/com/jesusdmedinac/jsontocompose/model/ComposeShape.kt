package com.jesusdmedinac.jsontocompose.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a shape used for borders, backgrounds, shadows, and clipping.
 *
 * Shapes are serialized with a `type` discriminator in JSON.
 */
@Serializable
sealed class ComposeShape {

    /** A rectangular shape with no rounded corners. */
    @Serializable
    @SerialName("Rectangle")
    data object Rectangle : ComposeShape()

    /** A circular shape. */
    @Serializable
    @SerialName("Circle")
    data object Circle : ComposeShape()

    /**
     * A rectangle with rounded corners.
     *
     * If [all] is provided, it applies a uniform corner radius. Otherwise,
     * individual corners can be specified independently.
     *
     * @property all Uniform corner radius in dp applied to all four corners.
     * @property topStart Top-start corner radius in dp.
     * @property topEnd Top-end corner radius in dp.
     * @property bottomStart Bottom-start corner radius in dp.
     * @property bottomEnd Bottom-end corner radius in dp.
     */
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
