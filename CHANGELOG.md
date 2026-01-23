# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- **Custom Components**: New extensibility system allowing developers to register custom components without forking the library
  - New `ComposeType.Custom` type
  - New `NodeProperties.CustomProps` with `customType` and `customData` fields
  - New `LocalCustomRenderers` CompositionLocal for registering custom renderers
  - New `ToCustom()` renderer function

## [1.1.0] - Upcoming

### Added
- **Custom Components extensibility** - Developers can now extend the library with their own components
- **TextField component** - Text input support with `LocalStateHost` for state management
- **LazyColumn component** - Lazy vertical scrolling list
- **LazyRow component** - Lazy horizontal scrolling list
- **Scaffold component** - Material Design scaffold layout
- **Image component** - Support for URL images (via Coil) and local drawable resources
- **Alignment support** - Box contentAlignment (TopStart, Center, BottomEnd, etc.)
- **Arrangement support** - Column/Row arrangements (SpaceBetween, SpaceEvenly, etc.)
- **propagateMinConstraints** - Box property for constraint propagation
- **ComposeModifier** - Declarative modifier system with operations:
  - `Padding(value)`
  - `FillMaxSize`, `FillMaxWidth`, `FillMaxHeight`
  - `Width(value)`, `Height(value)`
  - `BackgroundColor(hexColor)`

### Changed
- **NodeProperties refactor** - Each component now has specific Props class (ColumnProps, RowProps, BoxProps, etc.)
- **Behavior injection** - Now uses `LocalBehavior` CompositionLocal instead of direct parameter
- **Drawable resources** - Now uses `LocalDrawableResources` CompositionLocal
- **State management** - New `LocalStateHost` CompositionLocal for TextField state

### Infrastructure
- Replaced Ktor CIO engine with platform-specific engines
- Switched Coil network backend to Ktor3
- Refactored library structure and imports

## [1.0.3] - 2024-12-09

### Changed
- Updated README documentation
- Version bump for Maven Central

## [1.0.2] - 2024-12-09

### Changed
- Updated namespace configuration
- Sample app improvements
- README updates

## [1.0.1] - 2024-12-07

### Added
- `String.ToCompose()` extension function for direct JSON to Compose conversion

## [1.0.0] - 2024-12-07

### Added
- Initial release
- Core JSON to Compose conversion
- Basic components support:
  - Text
  - Button
  - Column
  - Row
  - Box
- Behavior interface for click events
- Kotlin Multiplatform support:
  - Android
  - iOS
  - Desktop (JVM)
  - Web (Wasm)

---

## Migration Guide

### From 1.0.x to 1.1.0

#### Behavior injection changed

**Before (1.0.x):**
```kotlin
jsonString.ToCompose(object : Behavior {
    override fun onClick(eventName: String) { }
})
```

**After (1.1.0):**
```kotlin
val behaviors = mapOf(
    "button_clicked" to object : Behavior {
        override fun onClick() { }
    }
)

CompositionLocalProvider(LocalBehavior provides behaviors) {
    jsonString.ToCompose()
}
```

#### JSON structure changed

**Before (1.0.x):**
```json
{
  "type": "Column",
  "children": [...]
}
```

**After (1.1.0):**
```json
{
  "type": "Column",
  "properties": {
    "type": "ColumnProps",
    "children": [...]
  }
}
```

#### New: Custom Components

```kotlin
val customRenderers = mapOf(
    "MyComponent" to { node: ComposeNode ->
        // Your custom composable
    }
)

CompositionLocalProvider(LocalCustomRenderers provides customRenderers) {
    jsonString.ToCompose()
}
```