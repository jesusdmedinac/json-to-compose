# Phase 4: Semantics & Testability

## Idea (Step 1)

**Goal:** Enhance the library's testability by exposing all visual styles and modifier operations through Jetpack Compose Semantics.

**Why:** Current tests like `textRendersWithStateDrivenFontSize` only check that a component exists (`assertExists()`), but they do not verify that the *correct* property value (e.g., `fontSize=24.sp`) was actually applied. This makes tests brittle and prone to false positives.

**How:** 
1. Define custom `SemanticsPropertyKey`s for all currently untestable properties (fontSize, color, background, padding, size, etc.).
2. Update all Renderers (`TextRenderer`, `BoxRenderer`, etc.) and `ModifierMapper` to attach these semantics.
3. Update existing tests to use `assert(SemanticsMatcher.expectValue(...))` instead of just `assertExists()`.

This phase acts as a cross-cutting concern that improves the robustness of the entire library, ensuring that dynamic state updates (like changing font size via a `StateHost`) are actually reflected in the UI tree.

## Scope

This project touches the `library/` module.

### New Features:
- **Typography Semantics:** `fontSize`, `fontWeight`, `color`, `lineHeight`, etc.
- **Modifier Semantics:** `padding`, `size`, `background`, `border`, etc.
- **Layout Semantics:** `verticalArrangement`, `horizontalAlignment`, `contentAlignment`.
- **Component-Specific Semantics:** `placeholder`, `label`, `error`, `keyboardType`, etc.

## Success Criteria

- All `Text` typography properties are assertable via `SemanticsMatcher`.
- All `Modifier` operations (padding, size, background) are assertable.
- Layout properties (arrangement/alignment) are assertable.
- Component-specific properties (e.g., `TextField` error state) are assertable.
- Existing tests are updated to verify precise values, not just existence.
