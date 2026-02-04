package com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Compose-aware [StateHost] implementation backed by `mutableStateOf`.
 *
 * State changes trigger recomposition automatically.
 *
 * ```kotlin
 * val textState = MutableStateHost("initial value")
 * CompositionLocalProvider(LocalStateHost provides mapOf("myText" to textState)) {
 *     json.ToCompose()
 * }
 * ```
 *
 * @param T The type of state value.
 * @param initial The initial state value.
 */
class MutableStateHost<T>(initial: T) : StateHost<T> {
    private var _state by mutableStateOf(initial)
    override val state: T get() = _state
    override fun onStateChange(state: T) { _state = state }
}
