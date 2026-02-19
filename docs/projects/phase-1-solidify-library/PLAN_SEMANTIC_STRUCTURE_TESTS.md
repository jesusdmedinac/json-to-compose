# Plan: Migrate Snapshot Tests to Semantic Structure Tests

**Related ADR:** [`docs/adr/ADR-001-snapshot-testing-approach.md`](../../adr/ADR-001-snapshot-testing-approach.md)

## Overview

The original 8 snapshot scenarios in `snapshot_tests.feature` compare rendered output against reference images. Since there is no stable cross-platform snapshot testing API for Compose Multiplatform, we will replace these with **semantic structure tests** that verify the composable tree using `testTag`, finders, and assertions.

This plan has two parts:

1. **Refactor renderers** to add `Modifier.testTag()` so nodes can be found by structural role.
2. **Rewrite the 8 scenarios** as semantic structure tests.

---

## Part 1: Add `testTag` to Renderers

### What changes

Every renderer (each in its own file under `renderer/`) will add `Modifier.testTag()` based on `ComposeNode.type.name` to the root composable it emits. This allows tests to locate nodes by their component type.

### Tag format

```
testTag = "{ComposeType.name}"
```

For example, a `Column` renderer will produce `Modifier.testTag("Column")`. When there are multiple nodes of the same type, tests can use `onAllNodesWithTag("Text")` with index access.

### Files to modify

| File | Change |
|------|--------|
| `renderer/*Renderer.kt` | Each renderer file already has `.testTag(type.name)` in its modifier chain |

### Renderer changes (pseudocode)

```kotlin
// Before
val modifier = Modifier from composeModifier

// After
val modifier = (Modifier from composeModifier).testTag(type.name)
```

This applies to all 14 renderers: `ToColumn`, `ToRow`, `ToBox`, `ToText`, `ToButton`, `ToImage`, `ToTextField`, `ToLazyColumn`, `ToLazyRow`, `ToScaffold`, `ToCard`, `ToAlertDialog`, `ToTopAppBar`, `ToCustom`.

### Impact on existing tests

- Existing tests use `onNodeWithText()` which does not depend on `testTag`. They will continue to pass.
- The new `testTag` is additive and non-breaking for library consumers.

---

## Part 2: Rewrite Snapshot Scenarios

### Updated feature file

The original `snapshot_tests.feature` will be replaced with `semantic_structure_tests.feature`. Each scenario now verifies **structure** instead of **pixels**.

### Scenario mapping

| # | Original Scenario | New Semantic Scenario | What it verifies |
|---|-------------------|-----------------------|------------------|
| 1 | Snapshot of basic Text | Structure of basic Text | Text node exists, is displayed, has correct content |
| 2 | Snapshot of Column with multiple children | Structure of Column with multiple children | Column tag exists, 3 Text nodes with correct content in order |
| 3 | Snapshot of Row with SpaceEvenly arrangement | Structure of Row with SpaceEvenly arrangement | Row tag exists, 3 Text children present |
| 4 | Snapshot of Box with contentAlignment Center | Structure of Box with contentAlignment Center | Box tag exists, Text child present and displayed |
| 5 | Snapshot of Button with Text child | Structure of Button with Text child | Button tag exists, has click action, Text child with correct content |
| 6 | Snapshot of component with modifiers applied | Structure of component with modifiers applied | Text node exists and is displayed with modifiers (no crash) |
| 7 | Snapshot of complex nested layout | Structure of complex nested layout | All expected tags exist (Column, Row, Box, Text, Button), text content correct |
| 8 | Snapshot of LazyColumn with items | Structure of LazyColumn with items | LazyColumn tag exists, visible items are displayed |

### Key assertions per scenario

**Scenario 1 — Basic Text:**
```kotlin
onNodeWithTag("Text").assertIsDisplayed()
onNodeWithTag("Text").assertTextEquals("Snapshot Test")
```

**Scenario 2 — Column with 3 children:**
```kotlin
onNodeWithTag("Column").assertExists()
onAllNodesWithText("A").assertCountEquals(1)
onAllNodesWithText("B").assertCountEquals(1)
onAllNodesWithText("C").assertCountEquals(1)
onAllNodesWithTag("Text").assertCountEquals(3)
```

**Scenario 3 — Row with SpaceEvenly:**
```kotlin
onNodeWithTag("Row").assertExists()
onAllNodesWithTag("Text").assertCountEquals(3)
```

**Scenario 4 — Box with Center alignment:**
```kotlin
onNodeWithTag("Box").assertExists()
onNodeWithText("Centered").assertIsDisplayed()
```

**Scenario 5 — Button with Text child:**
```kotlin
onNodeWithTag("Button").assertExists()
onNodeWithTag("Button").assertHasClickAction()
onNodeWithText("Click Me").assertExists()
```

**Scenario 6 — Component with modifiers:**
```kotlin
onNodeWithTag("Text").assertExists()
onNodeWithTag("Text").assertIsDisplayed()
```

**Scenario 7 — Complex nested layout:**
```kotlin
onNodeWithTag("Column").assertExists()
onNodeWithTag("Row").assertExists()
onNodeWithTag("Button").assertExists()
onNodeWithTag("Button").assertHasClickAction()
// Verify all expected text content
onNodeWithText("Title").assertExists()
onNodeWithText("Subtitle").assertExists()
onNodeWithText("Action").assertExists()
```

**Scenario 8 — LazyColumn with items:**
```kotlin
onNodeWithTag("LazyColumn").assertExists()
onNodeWithText("Item 1").assertIsDisplayed()
onNodeWithText("Item 2").assertIsDisplayed()
```

---

## Implementation Steps

### Step 1: Update the feature file

Replace `docs/projects/phase-1-solidify-library/features/snapshot_tests.feature` with the new semantic structure scenarios.

### Step 2: Update PROGRESS.md

Replace the 8 snapshot scenario lines with the corresponding semantic structure scenario lines.

### Step 3: Refactor renderers to add `testTag`

Each renderer file (e.g., `ColumnRenderer.kt`, `TextRenderer.kt`) already appends `.testTag(type.name)` to its modifier. The `type` property is available on every `ComposeNode` receiver.

### Step 4: Write the test file

Create `library/src/commonTest/kotlin/.../renderer/SemanticStructureTest.kt` with the 8 test methods.

### Step 5: Run tests

```bash
./gradlew :library:desktopTest
```

### Step 6: Update PROGRESS.md

Mark all 8 scenarios as complete.

### Step 7: Update TESTING_GUIDE.md

Add a section explaining the semantic structure testing approach and how `testTag` works in the library.

---

## Risks and Mitigations

| Risk | Mitigation |
|------|------------|
| Adding `testTag` to all renderers could affect consumer tests that rely on specific testTag values | The tag is additive; it does not replace consumer-defined testTags. However, if a consumer also applies `testTag` via JSON modifiers, there could be a conflict. We should document this. |
| Multiple nodes of the same type make `onNodeWithTag("Text")` ambiguous | Use `onAllNodesWithTag("Text")[index]` or combine with `onNodeWithText()` for precision. |
| `testTag` on every node adds overhead | `testTag` is a no-op in production builds where no test framework is active. The overhead is negligible. |
