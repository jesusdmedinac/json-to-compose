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
│   ├── ComponentRenderers.kt     → @Composable functions per component
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
| Column | ColumnProps | Container |
| Row | RowProps | Container |
| Box | BoxProps | Container |
| LazyColumn | ColumnProps | Container (lazy) |
| LazyRow | RowProps | Container (lazy) |

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

File: `renderer/ComponentRenderers.kt`

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

### Instructions for the Agent

- **When starting a new functionality:** Read `PROGRESS.md` to know where the project stands. If it doesn't exist, create it.
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
