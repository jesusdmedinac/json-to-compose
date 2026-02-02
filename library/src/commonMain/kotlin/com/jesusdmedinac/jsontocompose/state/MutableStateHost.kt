package com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MutableStateHost<T>(initial: T) : StateHost<T> {
    private var _state by mutableStateOf(initial)
    override val state: T get() = _state
    override fun onStateChange(state: T) { _state = state }
}
