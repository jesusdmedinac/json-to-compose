# ADR-001: Replace Pixel-Based Snapshot Tests with Semantic Structure Tests

**Status:** Accepted
**Date:** 2026-01-31
**Decision makers:** Project maintainers

## Context

Phase 1 of the json-to-compose library ("Solidify the Library") includes a feature called **"Snapshot tests for rendered components"** with 8 scenarios that were originally designed to capture visual snapshots (pixel-based screenshots) of rendered composables and compare them against reference images.

During implementation, we evaluated the feasibility of snapshot testing across the library's 4 target platforms: Android, iOS, Desktop (JVM), and WebAssembly (WASM).

## Options Evaluated

### Option 1: Official Compose Multiplatform Snapshot Testing API

**Does not exist.** As of January 2026, JetBrains has not shipped a common snapshot/screenshot testing API for Compose Multiplatform. There is an open feature request ([JetBrains/compose-multiplatform#2520](https://github.com/JetBrains/compose-jb/issues/2520)) but no implementation.

### Option 2: Android-Only Snapshot Tools

- **[Compose Preview Screenshot Testing](https://developer.android.com/studio/preview/compose-screenshot-testing)** — Official Google tool. Works only with Android Gradle Plugin, does not support non-Android targets in KMP projects.
- **[Paparazzi](https://github.com/cashapp/paparazzi)** / **[Roborazzi](https://github.com/takahirom/roborazzi)** — JVM-based rendering for Android layouts. Can be used with [ComposablePreviewScanner](https://github.com/sergio-sastre/ComposablePreviewScanner) (~150k monthly downloads, the most mature option). However, these tools only target Android/JVM and cannot run in `commonTest`.

**Verdict:** Would only cover 1 of 4 platforms. Contradicts the project's multiplatform goal.

### Option 3: Third-Party KMP Snapshot Libraries

- **[Telereso KMP Screenshot Testing](https://kmp.telereso.io/Testing/Screenshot.html)** — Aims at KMP `commonTest`, but is young and not widely adopted. Adds an external dependency with uncertain long-term support.
- **[Snappy (kotlin-snapshot-testing)](https://github.com/QuickBirdEng/kotlin-snapshot-testing)** — Snapshots of serializable properties, not pixel-based rendering. Different purpose.
- **[Comshot](https://github.com/mahozad/comshot)** — Experimental, tested only on Windows and Android.
- **[KMP-Capturable-Compose](https://github.com/suwasto/KMP-Capturable-Compose)** — Android and iOS only, not a testing tool per se.

**Verdict:** No library offers stable, cross-platform snapshot testing for all 4 targets. Adopting one would introduce risk and a fragile dependency.

### Option 4: Semantic Structure Tests (Selected)

Use the existing Compose Multiplatform testing API (`runComposeUiTest`, finders, assertions, actions) to verify the **semantic structure** of rendered component trees instead of comparing pixels. This approach:

- Runs in `commonTest` on all 4 platforms.
- Uses the stable, official `compose.uiTest` API.
- Verifies hierarchy (parent-child relationships), node count, content, visibility, and interactivity.
- Requires adding `Modifier.testTag()` to renderers so that nodes can be found by their structural role, not just by text content.

## Decision

**We will implement Option 4: Semantic Structure Tests.**

The 8 snapshot test scenarios will be rewritten as semantic structure tests that verify:

1. **Node existence** — The expected composables are present in the tree.
2. **Node count** — Container components have the correct number of children.
3. **Content correctness** — Text nodes display the right content.
4. **Visibility** — Nodes are displayed (not just in the tree but rendered).
5. **Interactivity** — Clickable elements have click actions.
6. **Hierarchy** — Parent nodes contain the expected children (using `testTag` + `onChildren()`).

To enable hierarchy verification, the library's renderers will be refactored to apply `Modifier.testTag()` using `ComposeNode.type.name` as the tag. This is a small, non-breaking change that also makes the library more testable for consumers.

## Consequences

### Positive

- All tests run in `commonTest` across 4 platforms with zero additional dependencies.
- Tests are deterministic — no flakiness from GPU rendering, font differences, or platform-specific antialiasing.
- Tests are fast — no image capture, storage, or comparison needed.
- The `testTag` addition makes the library more testable for downstream consumers.
- No new third-party dependencies.

### Negative

- Cannot detect purely visual regressions (e.g., a color shifting by 1 shade, a font rendering differently).
- Does not replace the value of true screenshot tests for design QA purposes.

### Future

- When JetBrains ships an official cross-platform snapshot testing API, we should revisit this decision and add pixel-based tests as a complement (not replacement) to these semantic tests.
- The `testTag` infrastructure added now will also benefit future snapshot tests.

## References

- [Compose Multiplatform Testing Docs](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-test.html)
- [JetBrains Issue #2520 — Desktop screenshot tests](https://github.com/JetBrains/compose-jb/issues/2520)
- [ComposablePreviewScanner](https://github.com/sergio-sastre/ComposablePreviewScanner)
- [Telereso KMP Screenshot Testing](https://kmp.telereso.io/Testing/Screenshot.html)
- [Snappy (kotlin-snapshot-testing)](https://github.com/QuickBirdEng/kotlin-snapshot-testing)
- [Android Compose Preview Screenshot Testing](https://developer.android.com/studio/preview/compose-screenshot-testing)
