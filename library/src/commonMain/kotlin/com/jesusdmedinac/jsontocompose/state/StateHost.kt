package com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state

interface StateHost<T> {
    val state: T
    fun onStateChange(state: T)
}
