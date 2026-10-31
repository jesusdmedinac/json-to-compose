# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.2.0]

### Added
- **Actions System (Server-Driven Actions)**:
  - Declarative actions defined in JSON (`ComposeAction` sealed hierarchy):
    - `SetState`: Updates state values in `StateHost` (supports String, Boolean, Int, Double, Float, and JSON elements).
    - `ToggleState`: Inverts a boolean state value managed by a `StateHost<Boolean>`.
    - `Log`: Prints debug messages to standard output.
    - `Sequence`: Chains multiple actions to execute in deterministic sequential order.
    - `Custom`: Invokes domain-specific actions via handlers registered in `LocalCustomActionHandlers`.
    - `ShowSnackbar`: Displays Material 3 Snackbars via `SnackbarHostState` with configurable message, duration, action label, and dismiss action.
    - `Navigate` & `NavigateBack`: Route-based navigation handling via `NavigationHandler`.
    - `Delay`: Suspends execution for a specified duration before executing subsequent actions.
    - `Conditional`: Branching logic supporting comparison operators (`Equals`, `NotEquals`, `GreaterThan`, `LessThan`, `Contains`).
    - `IncrementState` & `DecrementState`: Modifies numeric state values by a configurable delta.
    - `LaunchUrl`: Opens external URLs in the platform browser via `PlatformHandler`.
    - `CopyToClipboard`: Copies specified text to the device clipboard via `PlatformHandler`.
    - `UpdateList`: Mutates list states (`Add`, `Remove`).
  - `ComposeDocument`: Top-level document model for SDUI payloads with schema versioning, initial state declarations (`initialState`), named action mappings (`actions`), and root `ComposeNode`.
  - `ActionDispatcher`: Hybrid coroutine dispatcher executing sequential actions, handling navigation via `NavigationHandler`, and delegating platform interactions via `PlatformHandler`.
  - CompositionLocals: `LocalActionDispatcher` and `LocalCustomActionHandlers`.
  - `ComposeDocument.ToCompose()` extension function providing automated lifecycle wiring of state hosts and action dispatchers to the Compose hierarchy.
- **Material 3 Components Expansion (30+ new component types)**:
  - Layout & Containers:
    - `Spacer`: Fixed width/height space container (`SpacerProps`).
    - `HorizontalDivider` & `VerticalDivider`: Material 3 dividers with custom thickness and color (`DividerProps`).
    - `FlowRow` & `FlowColumn`: Wrap layouts arranging items horizontally or vertically with spacing support (`FlowRowProps`, `FlowColumnProps`).
    - `Surface`: Material 3 surface container with tonal elevation, shape, and color support (`SurfaceProps`).
  - Pager Components:
    - `HorizontalPager` & `VerticalPager`: Page-based scrolling layouts with page count, initial page, and swipe gestures (`PagerProps`).
  - Navigation Components:
    - `NavigationBar` & `NavigationBarItem`: Material 3 bottom navigation bar with selected state, icons, and labels (`NavigationBarProps`, `NavigationBarItemProps`).
    - `NavigationRail` & `NavigationRailItem`: Material 3 side rail for medium and expanded screen form factors (`NavigationRailProps`, `NavigationRailItemProps`).
    - `ModalNavigationDrawer` & `NavigationDrawerItem`: Material 3 drawer layout with drawer content and gestures (`NavigationDrawerProps`, `NavigationDrawerItemProps`).
    - `TabRow` & `ScrollableTabRow`: Material 3 primary and secondary tab containers (`TabRowProps`).
    - `Tab`: Individual tab items with text, icon, and selected state (`TabProps`).
    - `BottomBar` & `BottomNavigationItem`: Bottom navigation containers with reactive state support (`BottomBarProps`, `BottomNavigationItemProps`).
  - Dialogs, AppBars & Sheets:
    - `CenterAlignedTopAppBar`, `MediumTopAppBar`, `LargeTopAppBar`: Material 3 top app bar variations (`TopAppBarProps`).
    - `ModalBottomSheet`: Material 3 bottom sheet with exit animation handling and scrim support (`ModalBottomSheetProps`).
    - `AlertDialog`: Material 3 alert dialog with title, text, confirm/dismiss buttons, optional icon, and tonal elevation (`AlertDialogProps`).
    - `Card`, `ElevatedCard`, `OutlinedCard`: Material 3 card variations with elevation and border styling (`CardProps`, `OutlinedCardProps`).
  - Buttons & FABs:
    - `OutlinedButton`, `TextButton`, `ElevatedButton`, `FilledTonalButton`, `IconButton`: Material 3 button variants (`ButtonProps`).
    - `FloatingActionButton` & `ExtendedFloatingActionButton`: Standard and extended FABs with icon and text slots (`FabProps`, `ExtendedFabProps`).
  - Input & Selection:
    - `OutlinedTextField`: Material 3 outlined text field with reactive state binding (`TextFieldProps`).
    - `Switch` & `Checkbox`: Toggle components with inline value or `StateHost<Boolean>` integration (`SwitchProps`, `CheckboxProps`).
    - `Slider`: Continuous and discrete range selection slider (`SliderProps`).
    - `RadioButton`: Single-selection radio button (`RadioButtonProps`).
    - `SingleChoiceSegmentedButtonRow` & `MultiChoiceSegmentedButtonRow`: Segmented button containers (`SegmentedButtonRowProps`).
    - `SegmentedButton`: Individual segmented option buttons (`SegmentedButtonProps`).
    - `DatePicker` & `TimePicker`: Material 3 date and time picking components with dynamic state synchronization (`DatePickerProps`, `TimePickerProps`).
    - `SearchBar`: Material 3 search bar with query binding and search action support (`SearchBarProps`).
  - Display, Feedback & Chips:
    - `Icon`: Vector/resource icon component (`IconProps`).
    - `Badge` & `BadgedBox`: Material 3 badges with count or status indicator (`BadgeProps`, `BadgedBoxProps`).
    - `AssistChip`, `FilterChip`, `InputChip`, `SuggestionChip`: Material 3 chip variants with leading/trailing icons and selection state (`ChipProps`, `FilterChipProps`, `InputChipProps`).
    - `CircularProgressIndicator` & `LinearProgressIndicator`: Determinate and indeterminate progress indicators (`ProgressIndicatorProps`).
    - `PlainTooltip` & `RichTooltip`: Material 3 tooltips for contextual help (`PlainTooltipProps`, `RichTooltipProps`).
    - `SnackbarHost`: Container for hosting Material 3 snackbars (`SnackbarHostProps`).
    - `ListItem`: Material 3 list item with headline, supporting text, leading, trailing, and overline slots (`ListItemProps`).
- **Modifiers System Expansion (16+ new modifiers)**:
  - Visual & Shape:
    - `Border(width, color, shape)`: Border stroke with customizable shape.
    - `Shadow(elevation, shape, clip)`: Elevation shadow with clipping control.
    - `Clip(shape)`: Shape clipping modifier.
    - `Background(color, shape)`: Color fill with explicit shape support.
    - `Alpha(value)`: Opacity modifier (0.0 to 1.0).
    - `Rotate(degrees)`: Rotational transform.
    - `ComposeShape`: Shapes supporting `Rectangle`, `Circle`, and `RoundedCorner(radius)`.
  - Layout & Sizing:
    - `Weight(weight)`: Scope-aware weight distribution in Row and Column layouts via dynamic `LocalScope`.
    - `Offset(x, y)`: Relative positioning in dp.
    - `Size(width, height)`: Explicit width and height.
    - `WrapContentWidth` & `WrapContentHeight`: Content wrapping constraints.
    - `AspectRatio(ratio)`: Aspect ratio enforcement.
    - `ZIndex(zIndex)`: Visual draw order layering.
    - `MinWidth(value)`, `MinHeight(value)`, `MaxWidth(value)`, `MaxHeight(value)`: Boundary constraint modifiers.
    - `AnimateContentSize`: Smooth animation for size adjustments.
  - Interaction & Scrolling:
    - `Clickable(onClickEventName)`: Declarative click listener triggering behaviors in `LocalBehavior`.
    - `VerticalScroll` & `HorizontalScroll`: Scrollable modifier support for non-lazy containers.
  - Testing:
    - `TestTag(tag)`: Semantic test tag for UI testing automation.
- **Node & JSON Schema Validation System**:
  - `ComposeNodeValidator`: Engine for validating JSON payloads and `ComposeNode` tree structures.
  - `ValidationMode`: Supports `Strict` (rejects warnings) and `Permissive` (reports issues without halting) modes.
  - `ValidationResult`, `ValidationError`, `ValidationErrorCode`, and `ValidationSeverity`: Standardized diagnostic error reporting with node path tracking.
  - `ValidationRules`: Validates property types, forbids children on leaf nodes, validates alignment and arrangement tokens, checks `#RRGGBB` / `#AARRGGBB` hex color formats, and ensures modifier parameters remain within valid ranges.
  - Extension functions: `ComposeNode.validate()` and `String.validateComposeJson()`.
- **State Management & Architectural Patterns**:
  - ADR-003 Optional StateHost Pattern: Dual-property pattern (`val prop: T?` and `val propStateHostName: String?`) with fallback precedence (StateHost > inline value > default) across all dynamic properties via `resolveStateHostValue()`.
  - `MutableStateHost`: Two-way mutable state binding for interactive inputs.
  - `Arrangement.spacedBy` support for Row, Column, and Flow layouts.

### Changed
- **Renderer Modularization**: Split monolithic renderers into dedicated per-component renderer files in `renderer/` (e.g., `ColumnRenderer.kt`, `TextRenderer.kt`, `CardRenderer.kt`, `TopAppBarRenderer.kt`, etc.).
- **Hex Color Format**: Standardized all color properties across `NodeProperties` (e.g., `TextProps.color`, `BottomBarProps.backgroundColor`) to use hexadecimal color strings (`#RGB`, `#RRGGBB`, `#ARGB`, `#AARRGGBB`) instead of ARGB integer values (`Int`). Invalid colors fall back to `Color.Unspecified`.
- **Material 3 Migration**: Base components (`Card`, `AlertDialog`, `TopAppBar`) transitioned from Material 2 (`androidx.compose.material`) to Material 3 (`androidx.compose.material3`).
- **Hierarchical Node ID Generation**: Updated node ID generation in `ComposeNode` to counter-based positional IDs (`root_N`, `${parent.id}_${type}_N`).
- **`TextFieldProps` Reactive Binding**: Replaced legacy `onTextChangeEventName` with `valueStateHostName` and `value` fields using `StateHost<String>`.
- **`BoxProps` Constraints**: Changed `propagateMinConstraints` default to `null` and added `propagateMinConstraintsStateHostName` support.

### Breaking Changes
- **`ComposeNode.properties` backward compatibility restored (no migration needed)**:
  - In `1.1.0`, `ComposeNode.properties` was nullable and optional (`val properties: NodeProperties? = null`).
  - During `1.2.0` development it briefly became a required non-null parameter with no default, which broke Kotlin code omitting `properties` and JSON payloads without the `"properties"` field.
  - In the final `1.2.0`, the default is restored per component type (`val properties: NodeProperties = type.createDefaultProperties()`): `ComposeNode(type = ComposeType.Text)` compiles again and JSON without `"properties"` deserializes with sensible defaults, exactly as in `1.1.0`.
- **Hex Color Representation**:
  - Color attributes previously defined as ARGB integers (`Int`) in `NodeProperties` now expect hexadecimal color strings (`String`, e.g., `"#FF0000"` or `"#8000FF00"`). Payloads and code providing integer colors must migrate to string hex format.
- **Material 3 Migration Defaults**:
  - `Card`, `AlertDialog`, and `TopAppBar` now render Material 3 widgets. Color and style defaults now resolve against M3 `MaterialTheme.colorScheme` and `AlertDialogDefaults`/`TopAppBarDefaults`.
- **TextField State Model**:
  - `TextFieldProps.onTextChangeEventName` was removed. Text field updates must be bound through `valueStateHostName` using a `StateHost<String>`.

## [1.1.0]

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
- **Behavior interface (BREAKING)** - Changed `fun onClick()` to `operator fun invoke()` to make it more idiomatic in Kotlin.
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
        override fun invoke() { }
    }
)

// Or using lambda syntax:
val behaviors = mapOf(
    "button_clicked" to Behavior { println("Clicked!") }
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

### From 1.1.0 to 1.2.0

#### Hex color strings in properties
Colors across `NodeProperties` (such as `TextProps.color`, `BottomBarProps.backgroundColor`, `ButtonProps.color`, etc.) are now specified as hexadecimal strings instead of ARGB integer values.

**Before (1.1.0):**
```json
{
  "type": "Text",
  "properties": {
    "type": "TextProps",
    "text": "Hello World",
    "color": -65536
  }
}
```

**After (1.2.0):**
```json
{
  "type": "Text",
  "properties": {
    "type": "TextProps",
    "text": "Hello World",
    "color": "#FFFF0000"
  }
}
```

#### TextField reactive state management
`TextFieldProps.onTextChangeEventName` has been removed. State updates are now bound reactively using `valueStateHostName` (paired with `StateHost<String>` in `LocalStateHost`).

**Before (1.1.0):**
```json
{
  "type": "TextField",
  "properties": {
    "type": "TextFieldProps",
    "onTextChangeEventName": "on_text_change"
  }
}
```

**After (1.2.0):**
```json
{
  "type": "TextField",
  "properties": {
    "type": "TextFieldProps",
    "valueStateHostName": "search_query"
  }
}
```

#### ComposeNode construction
Backward compatibility was restored: `properties` is optional again with per-type defaults, so no changes are needed.

```kotlin
// In 1.1.0 and 1.2.0 (unchanged):
val node = ComposeNode(type = ComposeType.Text)
// properties defaults to NodeProperties.TextProps() via type.createDefaultProperties()
```

JSON payloads without the `"properties"` field also deserialize as in `1.1.0`.