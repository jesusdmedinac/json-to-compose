package com.jesusdmedinac.jsontocompose.runtime

import com.jesusdmedinac.jsontocompose.model.*
import com.jesusdmedinac.jsontocompose.state.StateHost
import kotlinx.coroutines.launch
import kotlinx.serialization.json.*

/**
 * Executes [ComposeAction]s by modifying state, logging, or delegating to custom handlers.
 *
 * The dispatcher is the runtime engine of the actions system. It receives an action
 * and a context (state hosts + custom handlers) and performs the corresponding side effect.
 *
 * @property stateHosts Map of state key names to their [StateHost] instances.
 * @property customActionHandlers Map of custom action type names to their handler functions.
 * @property snackbarHandler Optional handler for [ComposeAction.ShowSnackbar] actions. Should call
 *   `SnackbarHostState.showSnackbar()` in a coroutine scope.
 * @property navigationHandler Optional handler for navigation actions.
 * @property platformHandler Optional handler for platform-specific actions.
 * @property logger Function used for log output. Defaults to [println]. Override for testing.
 * @property coroutineScope Optional scope used to run asynchronous actions (like [ComposeAction.Delay]).
 */
class ActionDispatcher(
    private val stateHosts: Map<String, StateHost<*>>,
    private val customActionHandlers: Map<String, (ComposeAction.Custom) -> Unit> = emptyMap(),
    private val snackbarHandler: ((ComposeAction.ShowSnackbar) -> Unit)? = null,
    private val navigationHandler: NavigationHandler? = null,
    private val platformHandler: PlatformHandler? = null,
    private val logger: (String) -> Unit = ::println,
    private val coroutineScope: kotlinx.coroutines.CoroutineScope? = null,
) {

    /**
     * Executes the given [action] against the current context.
     *
     * Automatically switches to an asynchronous coroutine context if the action or any
     * of its child actions (such as inside a Sequence or Conditional) contains a [ComposeAction.Delay].
     * Otherwise, executes fully synchronously on the current thread to preserve test consistency.
     */
    fun dispatch(action: ComposeAction) {
        val hasDelay = hasDelay(action)
        val scope = coroutineScope
        if (hasDelay && scope != null) {
            scope.launch {
                dispatchSuspend(action)
            }
        } else {
            if (hasDelay) {
                logger("Warning: Delay action received but no CoroutineScope registered in ActionDispatcher. Executing synchronously and skipping delays.")
            }
            dispatchSync(action)
        }
    }

    /**
     * Asynchronously and sequentially dispatches [action] allowing suspend delays.
     */
    suspend fun dispatchSuspend(action: ComposeAction) {
        when (action) {
            is ComposeAction.Delay -> {
                kotlinx.coroutines.delay(action.durationMillis)
            }
            is ComposeAction.Sequence -> {
                action.actions.forEach { dispatchSuspend(it) }
            }
            is ComposeAction.Conditional -> {
                val isTrue = evaluateConditional(action)
                if (isTrue) {
                    dispatchSuspend(action.thenAction)
                } else {
                    action.elseAction?.let { dispatchSuspend(it) }
                }
            }
            // All other actions are synchronous and can be run using the standard sync execution
            else -> dispatchSync(action)
        }
    }

    private fun dispatchSync(action: ComposeAction) {
        when (action) {
            is ComposeAction.SetState -> executeSetState(action)
            is ComposeAction.ToggleState -> executeToggleState(action)
            is ComposeAction.Log -> executeLog(action)
            is ComposeAction.Sequence -> {
                action.actions.forEach { dispatchSync(it) }
            }
            is ComposeAction.Custom -> executeCustom(action)
            is ComposeAction.ShowSnackbar -> executeShowSnackbar(action)
            is ComposeAction.Navigate -> executeNavigate(action)
            is ComposeAction.NavigateBack -> executeNavigateBack(action)
            is ComposeAction.Delay -> {
                // Warning printed in dispatch(), do nothing
            }
            is ComposeAction.Conditional -> {
                val isTrue = evaluateConditional(action)
                if (isTrue) {
                    dispatchSync(action.thenAction)
                } else {
                    action.elseAction?.let { dispatchSync(it) }
                }
            }
            is ComposeAction.IncrementState -> executeIncrementState(action)
            is ComposeAction.DecrementState -> executeDecrementState(action)
            is ComposeAction.LaunchUrl -> executeLaunchUrl(action)
            is ComposeAction.CopyToClipboard -> executeCopyToClipboard(action)
            is ComposeAction.UpdateList -> executeUpdateList(action)
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

    private fun executeCustom(action: ComposeAction.Custom) {
        val handler = customActionHandlers[action.customType]
        if (handler == null) {
            logger("Warning: No custom action handler registered for type \"${action.customType}\". Custom action ignored.")
            return
        }
        handler(action)
    }

    private fun executeShowSnackbar(action: ComposeAction.ShowSnackbar) {
        val handler = snackbarHandler
        if (handler == null) {
            logger("Warning: No snackbarHandler registered. ShowSnackbar ignored for message: \"${action.message}\".")
            return
        }
        handler(action)
    }

    private fun executeNavigate(action: ComposeAction.Navigate) {
        val handler = navigationHandler
        if (handler == null) {
            logger("Warning: No NavigationHandler registered. Navigate to \"${action.route}\" ignored.")
            return
        }
        val resolvedArgs = action.args.mapValues { it.value.toNativeValue() }
        handler.navigate(action.route, resolvedArgs)
    }

    private fun executeNavigateBack(action: ComposeAction.NavigateBack) {
        val handler = navigationHandler
        if (handler == null) {
            logger("Warning: No NavigationHandler registered. NavigateBack ignored.")
            return
        }
        handler.navigateBack()
    }

    private fun evaluateConditional(action: ComposeAction.Conditional): Boolean {
        val stateHost = stateHosts[action.stateKey]
        if (stateHost == null) {
            logger("Warning: No StateHost registered with name \"${action.stateKey}\". Conditional action evaluated as false.")
            return false
        }
        val currentStateValue = stateHost.state
        val conditionValue = action.value.toNativeValue()
        return evaluateCondition(currentStateValue, action.operator, conditionValue)
    }

    private fun evaluateCondition(current: Any?, operator: ConditionOperator, target: Any?): Boolean {
        return when (operator) {
            ConditionOperator.Equals -> current == target
            ConditionOperator.NotEquals -> current != target
            ConditionOperator.GreaterThan -> {
                val curNum = current.toDoubleOrNull() ?: return false
                val tarNum = target.toDoubleOrNull() ?: return false
                curNum > tarNum
            }
            ConditionOperator.LessThan -> {
                val curNum = current.toDoubleOrNull() ?: return false
                val tarNum = target.toDoubleOrNull() ?: return false
                curNum < tarNum
            }
            ConditionOperator.Contains -> {
                val curStr = current?.toString() ?: return false
                val tarStr = target?.toString() ?: return false
                curStr.contains(tarStr)
            }
        }
    }

    private fun executeIncrementState(action: ComposeAction.IncrementState) {
        val stateHost = stateHosts[action.stateKey]
        if (stateHost == null) {
            logger("Warning: No StateHost registered with name \"${action.stateKey}\". IncrementState ignored.")
            return
        }
        val current = stateHost.state
        val currentNum = current.toDoubleOrNull() ?: 0.0
        val newValue = currentNum + action.by

        val resolvedNewValue: Any = if (current is Int) {
            newValue.toInt()
        } else {
            newValue
        }

        @Suppress("UNCHECKED_CAST")
        (stateHost as StateHost<Any>).onStateChange(resolvedNewValue)
    }

    private fun executeDecrementState(action: ComposeAction.DecrementState) {
        val stateHost = stateHosts[action.stateKey]
        if (stateHost == null) {
            logger("Warning: No StateHost registered with name \"${action.stateKey}\". DecrementState ignored.")
            return
        }
        val current = stateHost.state
        val currentNum = current.toDoubleOrNull() ?: 0.0
        val newValue = currentNum - action.by

        val resolvedNewValue: Any = if (current is Int) {
            newValue.toInt()
        } else {
            newValue
        }

        @Suppress("UNCHECKED_CAST")
        (stateHost as StateHost<Any>).onStateChange(resolvedNewValue)
    }

    private fun executeLaunchUrl(action: ComposeAction.LaunchUrl) {
        val handler = platformHandler
        if (handler == null) {
            logger("Warning: No PlatformHandler registered. LaunchUrl to \"${action.url}\" ignored.")
            return
        }
        handler.launchUrl(action.url)
    }

    private fun executeCopyToClipboard(action: ComposeAction.CopyToClipboard) {
        val handler = platformHandler
        if (handler == null) {
            logger("Warning: No PlatformHandler registered. CopyToClipboard ignored.")
            return
        }
        handler.copyToClipboard(action.text)
    }

    private fun executeUpdateList(action: ComposeAction.UpdateList) {
        val stateHost = stateHosts[action.stateKey]
        if (stateHost == null) {
            logger("Warning: No StateHost registered with name \"${action.stateKey}\". UpdateList ignored.")
            return
        }
        val current = stateHost.state
        val list = when (current) {
            is List<*> -> current.toMutableList()
            else -> {
                logger("Warning: StateHost \"${action.stateKey}\" is not a List. UpdateList ignored.")
                return
            }
        }

        val nativeItem = action.item.toNativeValue()
        when (action.operation) {
            ListOperation.Add -> {
                @Suppress("UNCHECKED_CAST")
                (list as MutableList<Any?>).add(nativeItem)
            }
            ListOperation.Remove -> {
                @Suppress("UNCHECKED_CAST")
                (list as MutableList<Any?>).remove(nativeItem)
            }
        }

        @Suppress("UNCHECKED_CAST")
        (stateHost as StateHost<Any>).onStateChange(list.toList())
    }

    private fun hasDelay(action: ComposeAction): Boolean {
        return when (action) {
            is ComposeAction.Delay -> true
            is ComposeAction.Sequence -> action.actions.any { hasDelay(it) }
            is ComposeAction.Conditional -> hasDelay(action.thenAction) || (action.elseAction?.let { hasDelay(it) } ?: false)
            else -> false
        }
    }

    private fun Any?.toDoubleOrNull(): Double? {
        return when (this) {
            is Number -> this.toDouble()
            is String -> this.toDoubleOrNull()
            else -> null
        }
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
