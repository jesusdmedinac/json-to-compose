package com.jesusdmedinac.jsontocompose.state

/**
 * Interface for providing and updating reactive state to JSON-driven components.
 *
 * Register named state hosts via `LocalStateHost` and reference them in component properties
 * using `*StateHostName` fields (e.g., `textStateHostName`, `checkedStateHostName`).
 *
 * @param T The type of state value this host manages.
 * @see MutableStateHost for a ready-to-use Compose-aware implementation.
 */
interface StateHost<T> {
    /** The current state value. */
    val state: T

    /**
     * Updates the state to the given [state] value.
     *
     * @param state The new state value.
     */
    fun onStateChange(state: T)
}
