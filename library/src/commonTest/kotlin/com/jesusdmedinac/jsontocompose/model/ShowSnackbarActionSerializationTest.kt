package com.jesusdmedinac.jsontocompose.model

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ShowSnackbarActionSerializationTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun serializeAndDeserializeShowSnackbar() {
        val original = ComposeAction.ShowSnackbar(
            message = "Item saved",
            actionLabel = "Undo",
            duration = SnackbarDuration.Short,
            withDismissAction = true,
            onActionEventName = "undoDelete",
            snackbarHostStateHostName = "snackbarState"
        )

        val encoded = json.encodeToString(ComposeAction.serializer(), original)
        val decoded = json.decodeFromString(ComposeAction.serializer(), encoded)

        assertEquals(original, decoded)
    }

    @Test
    fun showSnackbarDefaultValues() {
        val original = ComposeAction.ShowSnackbar(message = "Hello")
        val encoded = json.encodeToString(ComposeAction.serializer(), original)
        val decoded = json.decodeFromString(ComposeAction.serializer(), encoded)

        assertEquals(original, decoded)
        assertEquals("snackbarState", (decoded as ComposeAction.ShowSnackbar).snackbarHostStateHostName)
    }
}
