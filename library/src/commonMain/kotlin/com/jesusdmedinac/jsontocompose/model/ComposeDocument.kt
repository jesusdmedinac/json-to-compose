package com.jesusdmedinac.jsontocompose.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * A self-contained document that describes an entire interactive UI screen.
 *
 * Combines three concerns into a single serializable payload:
 * - [initialState]: named state values that become [com.jesusdmedinac.jsontocompose.state.MutableStateHost] instances at runtime
 * - [actions]: named action lists that become [com.jesusdmedinac.jsontocompose.behavior.Behavior] instances at runtime
 * - [root]: the UI tree rendered via `ComposeNode.ToCompose()`
 *
 * This enables fully server-driven interactive UIs where a backend sends a single
 * JSON payload and the client renders it without writing Kotlin setup code.
 *
 * ```json
 * {
 *   "initialState": { "visible": true, "name": "" },
 *   "actions": {
 *     "toggle": [{ "action": "toggleState", "stateKey": "visible" }]
 *   },
 *   "root": { "type": "Column", "properties": { "type": "ColumnProps", "children": [] } }
 * }
 * ```
 *
 * @property initialState Map of state key names to their initial values.
 *   Each entry is auto-wired into a `MutableStateHost` at runtime.
 *   Supports Boolean, String, Int, and Float values via [JsonElement].
 * @property actions Map of action names to ordered lists of [ComposeAction].
 *   Each entry is auto-wired into a `Behavior` that dispatches the actions when invoked.
 * @property root The root [ComposeNode] that defines the UI tree.
 */
@Serializable
data class ComposeDocument(
    val initialState: Map<String, JsonElement> = emptyMap(),
    val actions: Map<String, List<ComposeAction>> = emptyMap(),
    val root: ComposeNode,
)
