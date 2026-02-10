package com.jesusdmedinac.jsontocompose.runtime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state.MutableStateHost
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state.StateHost
import com.jesusdmedinac.jsontocompose.model.ComposeAction
import com.jesusdmedinac.jsontocompose.model.ComposeDocument

/**
 * Provides named custom action handlers for [ComposeAction.Custom] actions.
 *
 * Map custom action type names to handler functions that receive the full [ComposeAction.Custom].
 */
val LocalCustomActionHandlers =
    androidx.compose.runtime.staticCompositionLocalOf<Map<String, (ComposeAction.Custom) -> Unit>> { emptyMap() }

/**
 * Renders this [ComposeDocument] as a fully interactive Compose UI.
 *
 * Auto-wires [initialState] into [MutableStateHost] instances provided via [LocalStateHost],
 * and [actions] into [Behavior] instances provided via [LocalBehavior].
 * The consumer only needs to call this function — no manual state or behavior setup required.
 *
 * Custom action handlers from [LocalCustomActionHandlers] are forwarded to the dispatcher.
 *
 * ```kotlin
 * val document = Json.decodeFromString<ComposeDocument>(jsonFromServer)
 * document.ToCompose()
 * ```
 */
@Composable
fun ComposeDocument.ToCompose() {
    val customActionHandlers = LocalCustomActionHandlers.current
    val existingBehaviors = LocalBehavior.current
    val existingStateHosts = LocalStateHost.current

    val autoStateHosts: Map<String, StateHost<*>> = remember(initialState) {
        initialState.mapValues { (_, jsonElement) ->
            val nativeValue: Any? = jsonElement.toNativeValue()
            MutableStateHost(nativeValue)
        }
    }

    // Merge: existing manual entries + auto-wired (auto-wired wins on conflict)
    val mergedStateHosts = existingStateHosts + autoStateHosts

    val dispatcher = remember(mergedStateHosts, customActionHandlers) {
        ActionDispatcher(
            stateHosts = mergedStateHosts,
            customActionHandlers = customActionHandlers,
        )
    }

    val autoBehaviors: Map<String, Behavior> = remember(actions, dispatcher) {
        actions.mapValues { (_, actionList) ->
            object : Behavior {
                override fun invoke() {
                    actionList.forEach { dispatcher.dispatch(it) }
                }
            }
        }
    }

    // Merge: existing manual entries + auto-wired (auto-wired wins on conflict)
    val mergedBehaviors = existingBehaviors + autoBehaviors

    CompositionLocalProvider(
        LocalStateHost provides mergedStateHosts,
        LocalBehavior provides mergedBehaviors,
    ) {
        root.ToCompose()
    }
}
