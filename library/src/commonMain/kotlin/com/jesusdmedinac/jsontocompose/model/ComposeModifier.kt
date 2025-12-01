package com.jesusdmedinac.jsontocompose.model

import com.jesusdmedinac.jsontocompose.modifier.ModifierOperation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@SerialName("ComposeModifier")
data class ComposeModifier(
    val operations: List<Operation> = emptyList(),
) {
    @Serializable
    @SerialName("Operation")
    sealed class Operation(
        @Transient
        val modifierOperation: ModifierOperation? = null,
    ) {
        @Serializable
        @SerialName("Padding")
        data class Padding(val value: Int) : Operation(
            modifierOperation = ModifierOperation.Padding
        )

        @Serializable
        @SerialName("FillMaxSize")
        data object FillMaxSize : Operation(
            modifierOperation = ModifierOperation.FillMaxSize
        )

        @Serializable
        @SerialName("FillMaxWidth")
        data object FillMaxWidth : Operation(
            modifierOperation = ModifierOperation.FillMaxWidth
        )

        @Serializable
        @SerialName("FillMaxHeight")
        data object FillMaxHeight : Operation(
            modifierOperation = ModifierOperation.FillMaxHeight
        )

        @Serializable
        @SerialName("Width")
        data class Width(val value: Int) : Operation(
            modifierOperation = ModifierOperation.Width
        )

        @Serializable
        @SerialName("Height")
        data class Height(val value: Int) : Operation(
            modifierOperation = ModifierOperation.Height
        )

        @Serializable
        @SerialName("BackgroundColor")
        data class BackgroundColor(val hexColor: String) : Operation(
            modifierOperation = ModifierOperation.BackgroundColor
        )
    }
}
