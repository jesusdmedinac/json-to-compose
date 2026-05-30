package com.jesusdmedinac.jsontocompose.runtime

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Handles navigation actions triggered from server-driven UI documents.
 */
interface NavigationHandler {
    /**
     * Navigates to a specific [route] with optional [args].
     */
    fun navigate(route: String, args: Map<String, Any?> = emptyMap())

    /**
     * Pops the current destination from the navigation stack.
     */
    fun navigateBack()
}

/**
 * CompositionLocal providing the current [NavigationHandler].
 */
val LocalNavigationHandler = staticCompositionLocalOf<NavigationHandler?> { null }
