# AGENTS.md - Guide for AI Agents

This document provides essential context for AI agents to collaborate effectively on this project.

## Overview

**json-to-compose** is a Server-Driven UI (SDUI) framework for Kotlin Multiplatform that converts JSON structures into Jetpack Compose components at runtime.

**Main use case:** The backend controls the application's UI without requiring app updates.

## Project Structure

```
json-to-compose/
├── library/       → Core library (published on Maven Central v1.0.3)
├── composy/       → Visual editor web/desktop
├── composeApp/    → Multiplatform demo app
├── server/        → Ktor Backend
└── shared/        → Shared utilities
```

### Main Module: `/library`

Location of core code:
```
library/src/commonMain/kotlin/com/jesusdmedinac/jsontocompose/
├── JsonToCompose.kt              → Entry point, CompositionLocals, router
├── model/
│   ├── ComposeNode.kt            → Serializable UI tree node
│   ├── ComposeType.kt            → Component types enum
│   ├── ComposeModifier.kt        → Serializable modifiers
│   └── NodeProperties.kt         → Component-specific props (sealed interface)
├── renderer/
│   ├── ColumnRenderer.kt         → ComposeNode.ToColumn()
│   ├── RowRenderer.kt            → ComposeNode.ToRow()
│   ├── BoxRenderer.kt            → ComposeNode.ToBox()
│   ├── TextRenderer.kt           → ComposeNode.ToText()
│   ├── ButtonRenderer.kt         → ComposeNode.ToButton()
│   ├── ImageRenderer.kt          → ComposeNode.ToImage()
│   ├── TextFieldRenderer.kt      → ComposeNode.ToTextField()
│   ├── LazyColumnRenderer.kt     → ComposeNode.ToLazyColumn()
│   ├── LazyRowRenderer.kt        → ComposeNode.ToLazyRow()
│   ├── ScaffoldRenderer.kt       → ComposeNode.ToScaffold()
│   ├── CardRenderer.kt           → ComposeNode.ToCard()
│   ├── AlertDialogRenderer.kt    → ComposeNode.ToAlertDialog()
│   ├── TopAppBarRenderer.kt      → ComposeNode.ToTopAppBar()
│   ├── CustomRenderer.kt         → ComposeNode.ToCustom()
│   ├── Alignment.kt              → String → Compose Alignment mappers
│   └── Arrangment.kt             → String → Compose Arrangement mappers
├── modifier/
│   └── ModifierMapper.kt         → Applies modifier operations
├── behavior/
│   └── Behavior.kt               → Interface for click events
└── state/
    └── StateHost.kt              → Interface for state management
```

## Core Architecture

### Data Flow

```
JSON String → kotlinx.serialization → ComposeNode → Router → Specific Renderer → Compose UI
```

### Main Router (`JsonToCompose.kt:34-47`)

```kotlin
@Composable
fun ComposeNode.ToCompose() {
    when (type) {
        ComposeType.Column -> ToColumn()
        ComposeType.Row -> ToRow()
        ComposeType.Box -> ToBox()
        ComposeType.Text -> ToText()
        ComposeType.Button -> ToButton()
        ComposeType.Image -> ToImage()
        ComposeType.TextField -> ToTextField()
        ComposeType.LazyColumn -> ToLazyColumn()
        ComposeType.LazyRow -> ToLazyRow()
        ComposeType.Scaffold -> ToScaffold()
        ComposeType.Card -> ToCard()
        ComposeType.AlertDialog -> ToAlertDialog()
        ComposeType.TopAppBar -> ToTopAppBar()
        ComposeType.Custom -> ToCustom()
    }
}
```

### Supported Components

| Type | Props Class | Category |
|------|-------------|-----------|
| Text | TextProps | Leaf |
| Image | ImageProps | Leaf |
| TextField | TextFieldProps | Leaf |
| Button | ButtonProps | Single Child |
| Scaffold | ScaffoldProps | Single Child |
| Card | CardProps | Single Child |
| Column | ColumnProps | Container |
| Row | RowProps | Container |
| Box | BoxProps | Container |
| LazyColumn | ColumnProps | Container (lazy) |
| LazyRow | RowProps | Container (lazy) |
| AlertDialog | AlertDialogProps | Dialog |
| TopAppBar | TopAppBarProps | Navigation |
| Custom | CustomProps | Extensible |

## How to Add a New Component

### Step 1: Add to `ComposeType` enum

File: `model/ComposeType.kt`

```kotlin
enum class ComposeType {
    // ... existing
    NewComponent;

    fun isLayout(): Boolean = when (this) {
        Column, Row, Box, NewComponent -> true  // if it is a layout
        else -> false
    }

    fun hasChild(): Boolean = when (this) {
        Button, NewComponent -> true  // if it has a single child
        else -> false
    }
}
```

### Step 2: Create Props in `NodeProperties`

File: `model/NodeProperties.kt`

```kotlin
@Serializable
@SerialName("NewComponentProps")
data class NewComponentProps(
    val property1: String? = null,
    val property2: Int? = null,
    val children: List<ComposeNode>? = null,  // if it is a container
    val child: ComposeNode? = null,            // if it is single child
) : NodeProperties
```

**Important:** Use `@SerialName` for correct JSON serialization.

### Step 3: Create the Renderer

File: `renderer/{Name}Renderer.kt` (one file per renderer)

```kotlin
@Composable
fun ComposeNode.ToNewComponent() {
    val props = properties as? NodeProperties.NewComponentProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)

    // Implement the composable
    NewComponent(
        modifier = modifier,
        property1 = props.property1 ?: "default",
    ) {
        props.children?.forEach { it.ToCompose() }
        // or for single child:
        props.child?.ToCompose()
    }
}
```

**Important:** Always apply `.testTag(type.name)` to the modifier so the component can be found in tests via `onNodeWithTag("NewComponent")`.

### Step 4: Add to Router

File: `JsonToCompose.kt`

```kotlin
@Composable
fun ComposeNode.ToCompose() {
    when (type) {
        // ... existing
        ComposeType.NewComponent -> ToNewComponent()
    }
}
```

### Step 5: Update `ComposeNode.children()` (if applicable)

File: `model/ComposeNode.kt:25-30`

```kotlin
private fun NodeProperties?.children(): List<ComposeNode> = when(this) {
    is NodeProperties.ColumnProps -> children
    is NodeProperties.RowProps -> children
    is NodeProperties.BoxProps -> children
    is NodeProperties.NewComponentProps -> children  // add if it is container
    else -> null
} ?: emptyList()
```

### Step 6: Add example to demo app

File: `composeApp/src/commonMain/kotlin/.../App.kt`

Add a `ComposeNode` using the new component to the demo catalog in `App.kt`. The example should demonstrate the component's key properties so developers evaluating the library can see it in action.

```kotlin
ComposeNode(
    type = ComposeType.NewComponent,
    properties = NodeProperties.NewComponentProps(
        property1 = "Example value",
        child = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Inside NewComponent")
        )
    )
),
```

**Important:** Every new component must be visible in the demo app. This ensures the component is validated visually in a real app, not just in headless tests.

## Modifiers System

### Available Operations

| Operation | Parameters | Effect |
|-----------|------------|--------|
| Padding | `value: Int` | Padding in dp |
| FillMaxSize | - | Fills width and height |
| FillMaxWidth | - | Fills width |
| FillMaxHeight | - | Fills height |
| Width | `value: Int` | Fixed width in dp |
| Height | `value: Int` | Fixed height in dp |
| BackgroundColor | `hexColor: String` | ARGB Color (#AARRGGBB) |

### Add New Modifier Operation

1. **Add to enum** (`modifier/ModifierMapper.kt:15-23`):
```kotlin
enum class ModifierOperation {
    // ... existing
    NewOperation;
}
```

2. **Create sealed class** (`model/ComposeModifier.kt`):
```kotlin
@Serializable
@SerialName("NewOperation")
data class NewOperation(val value: Int) : Operation(
    modifierOperation = ModifierOperation.NewOperation
)
```

3. **Implement in mapper** (`modifier/ModifierMapper.kt:25-39`):
```kotlin
is ComposeModifier.Operation.NewOperation -> result.newOperation(operation.value.dp)
```

## Dependency Injection (CompositionLocal)

### Drawable Resources
```kotlin
val LocalDrawableResources = staticCompositionLocalOf<Map<String, DrawableResource>> { emptyMap() }
```

### Behaviors (Clicks)
```kotlin
val LocalBehavior = staticCompositionLocalOf<Map<String, Behavior>> { emptyMap() }

interface Behavior {
    fun onClick()
}
```

### State (TextField)
```kotlin
val LocalStateHost = staticCompositionLocalOf<Map<String, StateHost<*>>> { emptyMap() }

interface StateHost<T> {
    val state: T
    fun onStateChange(newState: T)
}
```

### Usage in App
```kotlin
CompositionLocalProvider(
    LocalDrawableResources provides mapOf("icon" to Res.drawable.icon),
    LocalBehavior provides mapOf("btn_click" to myBehavior),
    LocalStateHost provides mapOf("text_state" to myStateHost),
) {
    jsonNode.ToCompose()
}
```

## Alignment and Arrangement

### 2D Alignment (Box)
`TopStart`, `TopCenter`, `TopEnd`, `CenterStart`, `Center`, `CenterEnd`, `BottomStart`, `BottomCenter`, `BottomEnd`

### Vertical Alignment (Row)
`Top`, `CenterVertically`, `Bottom`

### Horizontal Alignment (Column)
`Start`, `CenterHorizontally`, `End`

### Vertical Arrangement (Column/LazyColumn)
`Top`, `Bottom`, `Center`, `SpaceEvenly`, `SpaceBetween`, `SpaceAround`

### Horizontal Arrangement (Row/LazyRow)
`Start`, `End`, `Center`, `SpaceEvenly`, `SpaceBetween`, `SpaceAround`

## Type Mapping: Compose Parameters → NodeProperties

When mapping parameters from a Jetpack Compose composable function to a `NodeProperties` data class, follow these rules to determine the correct type in json-to-compose:

### 1. Primitive-like types (`Boolean`, `String`, `Int`, `Float`, `Double`) → Dual properties (inline value + StateHost name)

These values support **two modes**: static (inline value from JSON) and dynamic (StateHost managed by the host app). Always declare **both** fields in `NodeProperties`:

- `val foo: T? = null` — inline value for static use cases
- `val fooStateHostName: String? = null` — StateHost name for dynamic use cases

**Resolution order** (implemented by `resolveStateHostValue()` in `state/StateHostResolver.kt`):
1. StateHost registered with the given name → use its `.state` value
2. StateHost name provided but not registered → log warning, fall back to inline value
3. Inline value provided → use it
4. Neither provided → use sensible default (same as the original Compose parameter default)

**In the renderer**, use the helper:
```kotlin
val (selected, _) = resolveStateHostValue(
    stateHostName = props.selectedStateHostName,
    inlineValue = props.selected,
    defaultValue = false,
)
```

**Examples:**
| Compose parameter | NodeProperties fields | StateHost type |
|---|---|---|
| `selected: Boolean` | `selected: Boolean?` + `selectedStateHostName: String?` | `StateHost<Boolean>` |
| `enabled: Boolean` | `enabled: Boolean?` + `enabledStateHostName: String?` | `StateHost<Boolean>` |
| `alwaysShowLabel: Boolean` | `alwaysShowLabel: Boolean?` + `alwaysShowLabelStateHostName: String?` | `StateHost<Boolean>` |
| `checked: Boolean` | `checked: Boolean?` + `checkedStateHostName: String?` | `StateHost<Boolean>` |
| `value: String` (TextField) | `value: String?` + `valueStateHostName: String?` | `StateHost<String>` |

### 2. `@Composable` content lambdas (`() -> Unit`, `RowScope.() -> Unit`) → `ComposeNode?`

Composable content slots are always mapped to `ComposeNode?` (or `List<ComposeNode>?` for containers). These are rendered recursively via `child.ToCompose()`.

**Examples:**
| Compose parameter | NodeProperties field |
|---|---|
| `icon: @Composable () -> Unit` | `icon: ComposeNode?` |
| `label: @Composable () -> Unit` | `label: ComposeNode?` |
| `title: @Composable () -> Unit` | `title: ComposeNode?` |
| `content: @Composable () -> Unit` | `child: ComposeNode?` |
| `content: @Composable () -> Unit` (layout) | `children: List<ComposeNode>?` |

### 3. Event callbacks (`() -> Unit`, `(T) -> Unit`) → `String?` event name

Non-composable lambdas (click handlers, change listeners) are mapped to **behavior event names** resolved via `LocalBehavior`.

- In `NodeProperties`: declare as `val onFooEventName: String? = null`
- In the renderer: resolve via `LocalBehavior.current[eventName]?.invoke()`

**Examples:**
| Compose parameter | NodeProperties field |
|---|---|
| `onClick: () -> Unit` | `onClickEventName: String?` |
| `onDismissRequest: () -> Unit` | `onDismissRequestEventName: String?` |
| `onCheckedChange: (Boolean) -> Unit` | `onCheckedChangeEventName: String?` |

### 4. Compose-library complex types (`Color`, `Modifier`, `MutableInteractionSource`, etc.) → Special handling

These types belong to the Compose framework and cannot be serialized directly to JSON. Each requires its own mapping strategy:

| Compose type | json-to-compose strategy |
|---|---|
| `Modifier` | Handled via `ComposeModifier` (list of serializable `Operation`s) |
| `Color` | Mapped as `Int?` (ARGB integer, e.g. `0xFFFF0000`), converted in renderer with `Color(value)` |
| `Dp` / `Dp.Elevation` | Mapped as `Int?` or `Float?`, converted with `.dp` |
| `MutableInteractionSource` | **Not yet supported** — mark with `// TODO: Support interactionSource` |
| `ContentColor` | Mapped as `Int?`, same as Color |
| `TextStyle`, `FontWeight` | Require dedicated mapping (see existing `TextProps` for reference) |

### 5. Summary decision tree

```
Compose parameter type?
├── @Composable lambda      → ComposeNode? or List<ComposeNode>?
├── Non-composable lambda   → String? (event name, resolved via LocalBehavior)
├── Boolean/String/Int/...  → T? (inline) + String? (StateHost name)
│                              Resolved via resolveStateHostValue() helper
│                              Precedence: StateHost > inline > default
├── Color                   → Int? (ARGB)
├── Dp                      → Int? or Float?
├── Modifier                → ComposeModifier (automatic via composeModifier field)
└── Other Compose types     → TODO / special implementation needed
```

See [ADR-003](docs/adr/ADR-003-optional-statehost-with-inline-defaults.md) for the full rationale behind the dual-property pattern.

## Code Conventions

### Naming
- **Props:** `{ComponentName}Props` (e.g., `ColumnProps`, `TextProps`)
- **Renderers:** `To{ComponentName}()` as `ComposeNode` extension function
- **Mappers:** `to{TargetType}()` as String extension function

### Serialization
- Use `@Serializable` in all data classes
- Use `@SerialName("ExplicitName")` for JSON control
- Use `@Transient` for fields that should not be serialized

### Patterns
- Nullable props with default values: `val prop: Type? = null`
- Early return if props don't match: `val props = properties as? Props ?: return`
- Modifier always applied: `val modifier = Modifier from composeModifier`

## Planning and Development Process with AI

This project follows a **5-step process** for AI-assisted planning and development. All agents collaborating on this project must follow this methodology. The full guide is located at [`docs/AI_PLANNING_PROCESS.md`](docs/AI_PLANNING_PROCESS.md).

### Process Summary

1. **Define the idea in natural language** - Describe the milestone or functionality at a high level.
2. **Generate Gherkin features and scenarios** - Ask the agent to propose features and scenarios in Gherkin format.
3. **Persist in `.feature` files** - Create the files in `docs/features/` to free up conversation context.
4. **Maintain `PROGRESS.md`** - Use a development tracker with a checklist of completed scenarios.
5. **Develop scenario by scenario** - Implement, test, and commit each scenario independently.

### CRITICAL Instructions for the Agent

- **BEFORE STARTING:** Always read `docs/projects/PROGRESS.md` and the specific phase file to know the current status.
- **DURING DEVELOPMENT:** Update the specific phase `PROGRESS.md` as you complete each scenario.
- **UPON COMPLETION:** **You MUST update BOTH** the specific phase `PROGRESS.md` AND the main `docs/projects/PROGRESS.md` file to ensure they stay in sync. This is mandatory for every completed task or scenario.
- **When receiving a new idea:** Generate Gherkin features and scenarios, persist them in `docs/features/`, and update `PROGRESS.md`.
- **When receiving "Develop the next scenario":** Read `PROGRESS.md`, identify the next pending scenario, read the corresponding `.feature`, implement the code, run the tests, and update `PROGRESS.md`.
- **When completing a scenario:** Commit with a message referencing the scenario (e.g., `feat: render basic Text from JSON [text_rendering.feature:Scenario 1]`).
- **If you lose context:** `PROGRESS.md` and `docs/features/*.feature` files contain everything needed to resume work.

### Planning Files Structure

```
json-to-compose/
├── PROGRESS.md                      → Development tracker (scenario checklist)
├── docs/
│   ├── AI_PLANNING_PROCESS.md       → Full guide to the 5-step process
│   └── features/                    → .feature files with Gherkin scenarios
│       ├── text_rendering.feature
│       ├── column_layout.feature
│       └── ...
```

## Useful Gradle Commands

```bash
# Build library
./gradlew :library:build

# Tests
./gradlew :library:test

# Publish to Maven Local (for testing)
./gradlew :library:publishToMavenLocal

# Run demo app (Desktop)
./gradlew :composeApp:run

# Run Composy editor (Desktop)
./gradlew :composy:run

# Ktor Server
./gradlew :server:run
```

## Full JSON Example

```json
{
  "type": "Column",
  "properties": {
    "type": "ColumnProps",
    "children": [
      {
        "type": "Text",
        "properties": {
          "type": "TextProps",
          "text": "Hello World"
        },
        "composeModifier": {
          "operations": [
            { "type": "Padding", "value": 16 }
          ]
        }
      },
      {
        "type": "Button",
        "properties": {
          "type": "ButtonProps",
          "onClickEventName": "my_button",
          "child": {
            "type": "Text",
            "properties": {
              "type": "TextProps",
              "text": "Click me"
            }
          }
        }
      }
    ],
    "verticalArrangement": "Center",
    "horizontalAlignment": "CenterHorizontally"
  },
  "composeModifier": {
    "operations": [
      { "type": "FillMaxSize" }
    ]
  }
}
```

## Supported Platforms

- Android (Min SDK per `libs.versions`)
- iOS (x64, arm64, simulator arm64)
- Desktop (JVM)
- WebAssembly (wasmJs)

## Main Dependencies

- **Jetpack Compose** - UI framework
- **kotlinx.serialization** - JSON parsing
- **Coil** - Image loading (with Ktor support)
- **Ktor** - HTTP Client

## New Component Checklist

- [ ] Add type to `ComposeType` enum
- [ ] Create `{Name}Props` in `NodeProperties` sealed interface
- [ ] Implement `To{Name}()` renderer (with `.testTag(type.name)`)
- [ ] Add case to router in `ComposeNode.ToCompose()`
- [ ] Update `children()` / `asList()` if it has children or a single child
- [ ] Add tests
- [ ] Add example to demo app (`composeApp/` `App.kt`)
- [ ] Update this document if there are new patterns
