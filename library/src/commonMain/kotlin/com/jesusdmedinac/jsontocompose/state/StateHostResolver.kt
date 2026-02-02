package com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state

import androidx.compose.runtime.Composable
import com.jesusdmedinac.jsontocompose.LocalStateHost

data class ResolvedStateHostValue<T>(
    val value: T,
    val stateHost: StateHost<T>?,
)

/**
 * Resolves a value using the following precedence:
 * 1. StateHost registered with [stateHostName] → use its current state
 * 2. [inlineValue] provided in JSON → use it
 * 3. [defaultValue] → hardcoded fallback
 *
 * Returns the resolved value and the typed StateHost (if found), so the renderer
 * can call [StateHost.onStateChange] when needed.
 */
@Composable
inline fun <reified T> resolveStateHostValue(
    stateHostName: String?,
    inlineValue: T?,
    defaultValue: T,
): ResolvedStateHostValue<T> {
    val currentStateHost = LocalStateHost.current
    if (stateHostName != null) {
        val raw = currentStateHost[stateHostName]
        if (raw != null) {
            @Suppress("UNCHECKED_CAST")
            val typed = raw as? StateHost<T>
            if (typed != null) {
                val rawValue = try {
                    typed.state
                } catch (_: Exception) {
                    println("Warning: StateHost \"$stateHostName\" failed to read state. Expected StateHost<${T::class.simpleName}>.")
                    return ResolvedStateHostValue(inlineValue ?: defaultValue, null)
                }
                // Verify the actual runtime type matches T (type erasure makes the cast above always succeed)
                if (rawValue != null && !T::class.isInstance(rawValue)) {
                    println("Warning: StateHost \"$stateHostName\" returned ${rawValue::class.simpleName} but expected ${T::class.simpleName}.")
                    return ResolvedStateHostValue(inlineValue ?: defaultValue, null)
                }
                return ResolvedStateHostValue(rawValue, typed)
            }
            println("Warning: StateHost \"$stateHostName\" type mismatch. Expected StateHost<${T::class.simpleName}>.")
        } else {
            println("Warning: No StateHost registered with name \"$stateHostName\". Falling back to inline value or default.")
        }
    }
    return ResolvedStateHostValue(inlineValue ?: defaultValue, null)
}
