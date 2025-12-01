package com.jesusdmedinac.jsontocompose.model

import com.jesusdmedinac.jsontocompose.modifier.ModifierOperation
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ComposeModifier(
    val operations: List<Operation> = emptyList(),
) {
    @Serializable
    sealed class Operation(
        @Transient
        val modifierOperation: ModifierOperation? = null,
    ) {
        @Serializable
        data class Padding(val value: Int) : Operation(
            modifierOperation = ModifierOperation.Padding
        )

        @Serializable
        data object FillMaxSize : Operation(
            modifierOperation = ModifierOperation.FillMaxSize
        )

        @Serializable
        data object FillMaxWidth : Operation(
            modifierOperation = ModifierOperation.FillMaxWidth
        )

        @Serializable
        data object FillMaxHeight : Operation(
            modifierOperation = ModifierOperation.FillMaxHeight
        )

        @Serializable
        data class Width(val value: Int) : Operation(
            modifierOperation = ModifierOperation.Width
        )

        @Serializable
        data class Height(val value: Int) : Operation(
            modifierOperation = ModifierOperation.Height
        )

        @Serializable
        data class BackgroundColor(val hexColor: String) : Operation(
            modifierOperation = ModifierOperation.BackgroundColor
        )
    }
}
