package com.jesusdmedinac.jsontocompose.model

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ComposeTypeTest {

    // --- Scenario 1: isLayout returns true for layout types ---

    @Test
    fun isLayoutReturnsTrueForLayoutTypes() {
        assertTrue(ComposeType.Column.isLayout())
        assertTrue(ComposeType.Row.isLayout())
        assertTrue(ComposeType.Box.isLayout())
    }

    // --- Scenario 2: isLayout returns false for non-layout types ---

    @Test
    fun isLayoutReturnsFalseForNonLayoutTypes() {
        val nonLayout = listOf(
            ComposeType.Text,
            ComposeType.Button,
            ComposeType.Image,
            ComposeType.TextField,
            ComposeType.LazyColumn,
            ComposeType.LazyRow,
            ComposeType.Scaffold,
            ComposeType.Card,
            ComposeType.Dialog,
            ComposeType.Custom,
        )
        nonLayout.forEach { type ->
            assertFalse(type.isLayout(), "${type.name} should not be a layout")
        }
    }

    // --- Scenario 3: hasChild returns true for single-child types ---

    @Test
    fun hasChildReturnsTrueForSingleChildTypes() {
        assertTrue(ComposeType.Button.hasChild())
        assertTrue(ComposeType.Card.hasChild())
    }

    // --- Scenario 4: hasChild returns false for non-single-child types ---

    @Test
    fun hasChildReturnsFalseForNonSingleChildTypes() {
        val noChild = listOf(
            ComposeType.Column,
            ComposeType.Row,
            ComposeType.Box,
            ComposeType.Text,
            ComposeType.Image,
            ComposeType.TextField,
            ComposeType.LazyColumn,
            ComposeType.LazyRow,
            ComposeType.Scaffold,
            ComposeType.Dialog,
            ComposeType.Custom,
        )
        noChild.forEach { type ->
            assertFalse(type.hasChild(), "${type.name} should not have a single child")
        }
    }
}
