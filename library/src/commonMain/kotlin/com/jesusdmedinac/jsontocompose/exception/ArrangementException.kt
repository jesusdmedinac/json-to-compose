package com.jesusdmedinac.jsontocompose.exception

/**
 * Thrown when a string cannot be mapped to a valid Compose [Arrangement][androidx.compose.foundation.layout.Arrangement].
 *
 * @param message Description of the unsupported arrangement value.
 */
class ArrangementException(message: String = "Arrangement not supported") : Exception(message)
