package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.modifier.from

/**
 * Temporary stub renderers for Phase 3 components.
 *
 * Each stub renders an empty [Box] with the component's type name as a testTag,
 * so that the system can route any new component without crashing and the component
 * is identifiable in tests. These stubs will be replaced with full implementations
 * scenario by scenario.
 */

// --- Input Components ---

// --- Layout Components ---

// --- Pager Components ---

// --- ModalBottomSheet ---

@Composable
fun ComposeNode.ToModalBottomSheet() {
    Box(modifier = (Modifier from composeModifier).testTag(type.name))
}

// --- Display Components ---

@Composable
fun ComposeNode.ToBadge() {
    Box(modifier = (Modifier from composeModifier).testTag(type.name))
}

@Composable
fun ComposeNode.ToBadgedBox() {
    Box(modifier = (Modifier from composeModifier).testTag(type.name))
}

@Composable
fun ComposeNode.ToAssistChip() {
    Box(modifier = (Modifier from composeModifier).testTag(type.name))
}

@Composable
fun ComposeNode.ToFilterChip() {
    Box(modifier = (Modifier from composeModifier).testTag(type.name))
}

@Composable
fun ComposeNode.ToInputChip() {
    Box(modifier = (Modifier from composeModifier).testTag(type.name))
}

@Composable
fun ComposeNode.ToSuggestionChip() {
    Box(modifier = (Modifier from composeModifier).testTag(type.name))
}

@Composable
fun ComposeNode.ToCircularProgressIndicator() {
    Box(modifier = (Modifier from composeModifier).testTag(type.name))
}

@Composable
fun ComposeNode.ToLinearProgressIndicator() {
    Box(modifier = (Modifier from composeModifier).testTag(type.name))
}

@Composable
fun ComposeNode.ToPlainTooltip() {
    Box(modifier = (Modifier from composeModifier).testTag(type.name))
}

@Composable
fun ComposeNode.ToRichTooltip() {
    Box(modifier = (Modifier from composeModifier).testTag(type.name))
}

// --- Snackbar ---

@Composable
fun ComposeNode.ToSnackbarHost() {
    Box(modifier = (Modifier from composeModifier).testTag(type.name))
}

// --- ListItem ---

@Composable
fun ComposeNode.ToListItem() {
    Box(modifier = (Modifier from composeModifier).testTag(type.name))
}
