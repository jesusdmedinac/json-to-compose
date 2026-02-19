# Phase: Demo Showcase — Multi-Screen SDUI Showcase App

**Status:** Planned
**Date:** 2026-02-06

## Idea (Step 1)

The json-to-compose library has 18 components and 14 modifier operations, all fully implemented with tests and documentation (Phase 1). However, the demo app is currently a single `App.kt` file of 1337 lines that renders all components in a flat `LazyColumn`. This makes it hard to navigate, hard to find specific demos, and does not represent the library's capabilities in a professional way.

The goal is to transform the demo app into a multi-screen showcase application with 3 navigable screens:

- **Catalog** (Home): A visually appealing landing page with hero section, category cards, and highlights
- **Components**: Interactive demos of all 18 components organized by category
- **Styles**: A gallery showcasing all 14 modifier operations with visual comparisons

The app will use bottom navigation to switch between screens and will dogfood the library by building all UI from `ComposeNode` trees rendered via `.toString().ToCompose()`.

## Scope

- **In scope**: `composeApp/` module only
  - `composeApp/src/commonMain/kotlin/com/jesusdmedinac/compose/sdui/App.kt` (refactor)
  - New screen files: `CatalogScreen.kt`, `ComponentsScreen.kt`, `StylesScreen.kt`, `DemoHelpers.kt`
  - `composeApp/build.gradle.kts` (add navigation dependency)
  - `gradle/libs.versions.toml` (add navigation version catalog entry)
- **Out of scope**: `library/`, `composy/`, `server/` modules (read-only reference)

## Success Criteria

1. Three screens navigable via a BottomBar (Catalog, Components, Styles)
2. All content rendered via `ComposeNode.ToCompose()` (dogfooding the library)
3. `BottomNavigationItem` reflects the currently selected screen
4. App compiles and runs on all 4 target platforms (Android, iOS, Desktop, WASM)
5. Code organized into separate files by screen (no single monolithic file)
6. All 18 components demonstrated in the Components screen
7. All 14 modifier operations demonstrated in the Styles screen
8. Consistent design language (color palette, spacing, typography) across screens

## Technical References

- **Navigation pattern**: Based on WIP commit `4241b74` (orphaned). Uses Custom renderer "NavHost" + `LocalNavController` + Behaviors
- **Navigation dependency**: `org.jetbrains.androidx.navigation:navigation-compose:2.9.1`
- **Existing demo pattern**: Every demo builds a `ComposeNode` tree and renders it using `ComposeNode(...).toString().ToCompose()`
- **Convention reference**: Follows structure of `docs/projects/phase-1-solidify-library/`

## Implementation Order

### Phase A: Infrastructure (Features 1 + 2)
1. Add `navigation-compose` dependency
2. Define routes as `@Serializable object`
3. Provide `NavHostController` via `CompositionLocal`
4. Create `DemoHelpers.kt` with helper functions and color palette
5. Scaffold with TopAppBar
6. BottomBar with 3 items
7. Register NavHost as Custom renderer
8. Wire navigation with Behaviors
9. Selected state for BottomNavigationItem via StateHost
10. Verify consistent color palette

### Phase B: Screen Content (Features 3, 4, 5)
11-15. `CatalogScreen.kt` (hero, LazyRow categories, featured, modifier highlights)
16-23. `ComponentsScreen.kt` (layout, content, input, containers, lazy, nav, custom)
24-30. `StylesScreen.kt` (sizing, colors, shapes, border/shadow, alpha/rotate, combined)

### Phase C: Verification (Feature 6)
31-36. Verify file organization and compilation on 4 platforms

## Files Modified During Implementation

| File | Action |
|------|--------|
| `gradle/libs.versions.toml` | Add `navigationCompose` version + library |
| `composeApp/build.gradle.kts` | Add `navigation-compose` + possibly `kotlinSerialization` |
| `composeApp/.../App.kt` | Refactor: app shell + nav + CompositionLocals |
| `composeApp/.../CatalogScreen.kt` | **NEW**: Home/catalog screen |
| `composeApp/.../ComponentsScreen.kt` | **NEW**: Interactive component demos |
| `composeApp/.../StylesScreen.kt` | **NEW**: Modifier gallery |
| `composeApp/.../DemoHelpers.kt` | **NEW**: Shared helper functions |
