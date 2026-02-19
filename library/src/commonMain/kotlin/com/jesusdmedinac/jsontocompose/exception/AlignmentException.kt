package com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.exception

/**
 * Thrown when a string cannot be mapped to a valid Compose [Alignment][androidx.compose.ui.Alignment].
 *
 * @param message Description of the unsupported alignment value.
 */
class AlignmentException(message: String = "Alignment not supported") : Exception(message)
