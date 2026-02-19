package com.jesusdmedinac.jsontocompose.behavior

/**
 * Callback interface for handling user interaction events from JSON-driven components.
 *
 * Register named behaviors via `LocalBehavior` and reference them in component properties
 * (e.g., `onClickEventName`, `onCheckedChangeEventName`).
 *
 * ```kotlin
 * val behaviors = mapOf("onLogin" to object : Behavior { override fun invoke() { /* ... */ } })
 * CompositionLocalProvider(LocalBehavior provides behaviors) { json.ToCompose() }
 * ```
 */
interface Behavior {
    /** Invoked when the associated event is triggered by the UI. */
    operator fun invoke()
}
