package com.jesusdmedinac.jsontocompose.runtime

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Handles device or platform-specific actions triggered from server-driven UI documents.
 */
interface PlatformHandler {
    /**
     * Launches the given external [url].
     */
    fun launchUrl(url: String)

    /**
     * Copies the given [text] to the device clipboard.
     */
    fun copyToClipboard(text: String)
}

/**
 * CompositionLocal providing the current [PlatformHandler].
 */
val LocalPlatformHandler = staticCompositionLocalOf<PlatformHandler?> { null }
