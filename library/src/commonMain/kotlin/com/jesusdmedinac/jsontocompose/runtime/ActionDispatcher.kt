package com.jesusdmedinac.jsontocompose.runtime

import com.jesusdmedinac.jsontocompose.model.ComposeAction
import com.jesusdmedinac.jsontocompose.state.StateHost
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.intOrNull

/**
 * Executes [ComposeAction]s by modifying state, logging, or delegating to custom handlers.
 *
 * The dispatcher is the runtime engine of the actions system. It receives an action
 * and a context (state hosts + custom handlers) and performs the corresponding side effect.
 *
 * @property stateHosts Map of state key names to their [StateHost] instances.
 * @property customActionHandlers Map of custom action type names to their handler functions.
 * @property logger Function used for log output. Defaults to [println]. Override for testing.
 */
class ActionDispatcher(
    private val stateHosts: Map<String, StateHost<*>>,
    private val customActionHandlers: Map<String, (ComposeAction.Custom) -> Unit> = emptyMap(),
    private val logger: (String) -> Unit = ::println,
) {

    /**
     * Executes the given [action] against the current context.
     *
     * - [ComposeAction.SetState]: Updates the state host identified by `stateKey` with the new value.
     * - [ComposeAction.ToggleState]: Flips the boolean state host identified by `stateKey`.
     * - [ComposeAction.Log]: Outputs the message via [logger].
     * - [ComposeAction.Sequence]: Executes each child action in order.
     * - [ComposeAction.Custom]: Delegates to the registered handler for the action's `customType`.
     */
    fun dispatch(action: ComposeAction) {
        when (action) {
            is ComposeAction.SetState -> executeSetState(action)
            is ComposeAction.ToggleState -> executeToggleState(action)
            is ComposeAction.Log -> executeLog(action)
            is ComposeAction.Sequence -> executeSequence(action)
            is ComposeAction.Custom -> executeCustom(action)
        }
    }

    private fun executeSetState(action: ComposeAction.SetState) {
        val stateHost = stateHosts[action.stateKey]
        if (stateHost == null) {
            logger("Warning: No StateHost registered with name \"${action.stateKey}\". SetState ignored.")
            return
        }
        val nativeValue = action.value.toNativeValue()
        @Suppress("UNCHECKED_CAST")
        (stateHost as StateHost<Any?>).onStateChange(nativeValue)
    }

    private fun executeToggleState(action: ComposeAction.ToggleState) {
        val stateHost = stateHosts[action.stateKey]
        if (stateHost == null) {
            logger("Warning: No StateHost registered with name \"${action.stateKey}\". ToggleState ignored.")
            return
        }
        val current = stateHost.state
        if (current !is Boolean) {
            logger("Warning: StateHost \"${action.stateKey}\" holds ${current?.let { it::class.simpleName } ?: "null"}, not Boolean. ToggleState ignored.")
            return
        }
        @Suppress("UNCHECKED_CAST")
        (stateHost as StateHost<Boolean>).onStateChange(!current)
    }

    private fun executeLog(action: ComposeAction.Log) {
        logger(action.message)
    }

    private fun executeSequence(action: ComposeAction.Sequence) {
        action.actions.forEach { dispatch(it) }
    }

    private fun executeCustom(action: ComposeAction.Custom) {
        val handler = customActionHandlers[action.customType]
        if (handler == null) {
            logger("Warning: No custom action handler registered for type \"${action.customType}\". Custom action ignored.")
            return
        }
        handler(action)
    }
}

/**
 * Converts a [JsonPrimitive] to its most specific native Kotlin type.
 *
 * Resolution order: Boolean > Int > Float > String.
 */
internal fun JsonPrimitive.toNativeValue(): Any? {
    booleanOrNull?.let { return it }
    intOrNull?.let { return it }
    floatOrNull?.let { return it }
    return content
}

/**
 * Converts a [kotlinx.serialization.json.JsonElement] to a native Kotlin value.
 *
 * Only [JsonPrimitive] values are converted. Other element types return their string representation.
 */
internal fun kotlinx.serialization.json.JsonElement.toNativeValue(): Any? {
    return if (this is JsonPrimitive) toNativeValue() else toString()
}
