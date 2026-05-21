package com.jesusdmedinac.jsontocompose.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

/**
 * Represents a declarative action that can be triggered by user interactions.
 *
 * Instead of writing Kotlin [com.jesusdmedinac.jsontocompose.behavior.Behavior] callbacks,
 * consumers define actions in JSON. The library interprets and executes them automatically.
 *
 * Actions are used inside a [ComposeDocument]'s `actions` map, where each named entry
 * maps to a list of [ComposeAction]s that execute in sequence when the corresponding
 * behavior key is triggered.
 */
@Serializable
sealed class ComposeAction {

    /**
     * Sets a state value identified by [stateKey] to the given [value].
     *
     * At runtime, the dispatcher finds the [com.jesusdmedinac.jsontocompose.state.StateHost]
     * registered under [stateKey] and calls `onStateChange` with the deserialized value.
     *
     * Supports any JSON-representable value: String, Boolean, Int, Float, etc.
     *
     * @property stateKey The name of the state host to update.
     * @property value The new value as a [JsonElement].
     */
    @Serializable
    @SerialName("setState")
    data class SetState(
        val stateKey: String,
        val value: JsonElement,
    ) : ComposeAction()

    /**
     * Toggles a boolean state value identified by [stateKey].
     *
     * Shortcut for reading the current boolean value and setting its inverse.
     * Logs a warning if the state is not a Boolean.
     *
     * @property stateKey The name of the boolean state host to toggle.
     */
    @Serializable
    @SerialName("toggleState")
    data class ToggleState(
        val stateKey: String,
    ) : ComposeAction()

    /**
     * Outputs a debug message.
     *
     * Intended for development and debugging. The [message] is printed to the console.
     *
     * @property message The message to log.
     */
    @Serializable
    @SerialName("log")
    data class Log(
        val message: String,
    ) : ComposeAction()

    /**
     * Executes multiple actions in order.
     *
     * Allows chaining actions for a single event (e.g., close a dialog, then log, then navigate).
     *
     * @property actions The ordered list of child actions to execute.
     */
    @Serializable
    @SerialName("sequence")
    data class Sequence(
        val actions: List<ComposeAction>,
    ) : ComposeAction()

    /**
     * A consumer-defined action delegated to a registered handler.
     *
     * The library does not interpret this action directly. Instead, it looks up a handler
     * registered under [customType] in `LocalCustomActionHandlers` and invokes it with [params].\n     *
     * Use this for domain-specific actions like navigation, HTTP requests, or analytics.
     *
     * @property customType The action type identifier (e.g., "navigate", "httpRequest").
     * @property params Arbitrary parameters passed to the handler.
     */
    @Serializable
    @SerialName("custom")
    data class Custom(
        val customType: String,
        val params: JsonObject = JsonObject(emptyMap()),
    ) : ComposeAction()

    /**
     * Displays a Material 3 Snackbar message via the [SnackbarHost] in the current Scaffold.
     *
     * At runtime, the dispatcher finds the `SnackbarHostState` registered under
     * [snackbarHostStateHostName] (or the default "snackbarState") and calls `showSnackbar()`.
     *
     * @property message The text to display in the Snackbar.
     * @property actionLabel Optional label for the Snackbar action button.
     * @property duration Snackbar duration: "Short", "Long", or "Indefinite". Defaults to "Short".
     * @property withDismissAction Whether to show a dismiss (X) icon. Defaults to false.
     * @property onActionEventName Event name to trigger when the action button is clicked.
     * @property snackbarHostStateHostName Name of the StateHost that holds the SnackbarHostState. Defaults to "snackbarState".
     */
    @Serializable
    @SerialName("showSnackbar")
    data class ShowSnackbar(
        val message: String,
        val actionLabel: String? = null,
        val duration: String? = null,
        val withDismissAction: Boolean = false,
        val onActionEventName: String? = null,
        val snackbarHostStateHostName: String = "snackbarState",
    ) : ComposeAction()
}
