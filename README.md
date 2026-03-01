# json-to-compose

A library that dynamically converts JSON into Jetpack Compose components.

## 📝 Description

json-to-compose is an Android library that allows you to convert JSON structures into Jetpack Compose components at runtime. This makes it easy to create dynamic interfaces and update UI based on remote data.

## 💡 Use case

### 1. Server-Driven UI

This library is perfect for implementing Server-Driven UI patterns, where your backend controls the UI layout and content. Some benefits include:

- Update your app's UI without deploying new versions
- A/B testing different layouts and components
- Dynamic content presentation based on user segments
- Consistent UI across platforms by sharing the same JSON structure

Example:

```kotlin
@Composable
fun App() {
    var uiDefinition by remember { mutableStateOf(api.fetchUIDefinition()) }

    LaunchedEffect(Unit) {
        uiDefinition = api.fetchUIDefinition()
    }

    uiDefinition.ToCompose()
}
```

### 2. Generative UI

Integrate with AI services to create dynamic UIs based on user input or context:

- AI-powered layout suggestions
- Dynamic form generation based on user preferences
- Context-aware UI adaptations
- Prototype testing with AI-generated interfaces

Example:

```kotlin
@Composable
fun App(userContext: String) {
    val aiGeneratedUI = aiService.generateUIForUser(userContext)
    aiGeneratedUI.ToCompose()
}
```

## ⚡️ Installation

Add the following dependency to your `build.gradle.kts` file:

```kotlin
dependencies {
  implementation("com.jesusdmedinac:json-to-compose:1.1.0")
}
```

## 🚀 Usage

### Basic Usage

```kotlin
@Composable
fun App() {
    MaterialTheme {
        JSON_AS_STRING.ToCompose()
    }
}

val JSON_AS_STRING = """
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
        }
      }
    ]
  }
}
""".trimIndent()
```

### With Behaviors (Click Events)

```kotlin
@Composable
fun App() {
    val behaviors = mapOf(
        "button_clicked" to object : Behavior {
            override fun onClick() {
                println("Button was clicked!")
            }
        }
    )

    CompositionLocalProvider(LocalBehavior provides behaviors) {
        MaterialTheme {
            JSON_AS_STRING.ToCompose()
        }
    }
}
```

### With State (TextField)

```kotlin
@Composable
fun App() {
    var textValue by remember { mutableStateOf("") }

    val stateHosts = mapOf(
        "my_text_field" to object : StateHost<String> {
            override val state: String get() = textValue
            override fun onStateChange(state: String) {
                textValue = state
            }
        }
    )

    CompositionLocalProvider(LocalStateHost provides stateHosts) {
        JSON_AS_STRING.ToCompose()
    }
}
```

### With Drawable Resources

```kotlin
@Composable
fun App() {
    val drawableResources = mapOf(
        "my_icon" to Res.drawable.my_icon
    )

    CompositionLocalProvider(LocalDrawableResources provides drawableResources) {
        JSON_AS_STRING.ToCompose()
    }
}
```

## 🔧 Supported Components

| Component                | Description                           | Properties                                                                                                                                                                                    |
| ------------------------ | ------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Text**                 | Display text                          | `text`, `fontSize`, `fontWeight`, `fontStyle`, `color`, `textAlign`, `maxLines`, `overflow`, `letterSpacing`, `lineHeight`, `textDecoration`, `minLines`                                      |
| **Button**               | Standard filled button                | `child`, `enabled`, `onClickEventName`                                                                                                                                                        |
| **ElevatedButton**       | Button with elevation                 | `child`, `enabled`, `onClickEventName`                                                                                                                                                        |
| **FilledTonalButton**    | Button with secondary container color | `child`, `enabled`, `onClickEventName`                                                                                                                                                        |
| **OutlinedButton**       | Button with border                    | `child`, `enabled`, `onClickEventName`                                                                                                                                                        |
| **TextButton**           | Button without border or background   | `child`, `enabled`, `onClickEventName`                                                                                                                                                        |
| **IconButton**           | Button containing an icon             | `child`, `enabled`, `onClickEventName`                                                                                                                                                        |
| **FloatingActionButton** | Circular FAB                          | `child`, `containerColor`, `onClickEventName`                                                                                                                                                 |
| **ExtendedFAB**          | FAB with icon and text                | `icon`, `text`, `containerColor`, `onClickEventName`                                                                                                                                          |
| **Icon**                 | Material Design icon                  | `iconName`, `tint`                                                                                                                                                                            |
| **Column**               | Vertical layout container             | `verticalArrangement`, `horizontalAlignment`, `children`                                                                                                                                      |
| **Row**                  | Horizontal layout container           | `horizontalArrangement`, `verticalAlignment`, `children`                                                                                                                                      |
| **Box**                  | Stacking layout container             | `contentAlignment`, `children`                                                                                                                                                                |
| **Image**                | Display images                        | `url`, `resourceName`, `contentDescription`, `contentScale`                                                                                                                                   |
| **TextField**            | Text input field                      | `value`, `placeholder`, `label`, `leadingIcon`, `trailingIcon`, `isError`, `supportingText`, `singleLine`, `maxLines`, `keyboardType`, `visualTransformation`, `readOnly`, `prefix`, `suffix` |
| **OutlinedTextField**    | Outlined text input field             | `value`, `placeholder`, `label`, `leadingIcon`, `trailingIcon`, `isError`, `supportingText`, `singleLine`, `maxLines`, `keyboardType`, `visualTransformation`, `readOnly`, `prefix`, `suffix` |
| **LazyColumn**           | Lazy vertical scrolling list          | `children`                                                                                                                                                                                    |
| **LazyRow**              | Lazy horizontal scrolling list        | `children`                                                                                                                                                                                    |
| **Scaffold**             | Material Design scaffold              | `topBar`, `bottomBar`, `floatingActionButton`, `child`                                                                                                                                        |
| **Card**                 | Material Design card                  | `elevation`, `cornerRadius`, `child`                                                                                                                                                          |
| **ElevatedCard**         | Card with elevated shadow             | `elevation`, `cornerRadius`, `child`                                                                                                                                                          |
| **OutlinedCard**         | Card with border outline              | `borderColor`, `cornerRadius`, `child`                                                                                                                                                        |
| **AlertDialog**          | Material Design alert dialog          | `title`, `text`, `confirmButton`, `dismissButton`, `visibilityState`                                                                                                                          |
| **TopAppBar**            | Material Design top app bar           | `title`, `backgroundColor`                                                                                                                                                                    |
| **Custom**               | User-defined components               | `customType`, `customData`                                                                                                                                                                    |

## 🧩 Custom Components

Extend the library with your own components without forking:

```kotlin
// 1. Define your custom renderer
val customRenderers = mapOf(
    "ProductCard" to { node: ComposeNode ->
        val props = node.properties as? NodeProperties.CustomProps
        val data = props?.customData
        val title = data?.get("title")?.jsonPrimitive?.content ?: ""
        val price = data?.get("price")?.jsonPrimitive?.content ?: ""

        Card {
            Column {
                Text(text = title)
                Text(text = "$$price")
            }
        }
    }
)

// 2. Provide it to the composition
CompositionLocalProvider(LocalCustomRenderers provides customRenderers) {
    JSON_AS_STRING.ToCompose()
}
```

**JSON for custom component:**

```json
{
  "type": "Custom",
  "properties": {
    "type": "CustomProps",
    "customType": "ProductCard",
    "customData": {
      "title": "My Product",
      "price": "99.99"
    }
  }
}
```

## 🎨 Modifiers

Apply modifiers to any component using `composeModifier`:

```json
{
  "type": "Box",
  "properties": { "type": "BoxProps" },
  "composeModifier": {
    "operations": [
      { "type": "FillMaxWidth" },
      { "type": "Height", "value": 200 },
      { "type": "Padding", "value": 16 },
      { "type": "BackgroundColor", "color": "#FF5722" }
    ]
  }
}
```

**Available operations:**

- `Padding` - Add padding (value in dp)
- `FillMaxSize` - Fill maximum available size
- `FillMaxWidth` - Fill maximum width
- `FillMaxHeight` - Fill maximum height
- `Width` - Set specific width (value in dp)
- `Height` - Set specific height (value in dp)
- `BackgroundColor` - Set background color (hex color)

## 📐 Alignment & Arrangement

### Box Alignment

```json
{
  "type": "Box",
  "properties": {
    "type": "BoxProps",
    "contentAlignment": "Center"
  }
}
```

**Values:** `TopStart`, `TopCenter`, `TopEnd`, `CenterStart`, `Center`, `CenterEnd`, `BottomStart`, `BottomCenter`, `BottomEnd`

### Column Arrangement

```json
{
  "type": "Column",
  "properties": {
    "type": "ColumnProps",
    "verticalArrangement": "SpaceBetween",
    "horizontalAlignment": "CenterHorizontally"
  }
}
```

**Vertical Arrangement:** `Top`, `Bottom`, `Center`, `SpaceEvenly`, `SpaceBetween`, `SpaceAround`

**Horizontal Alignment:** `Start`, `CenterHorizontally`, `End`

### Row Arrangement

```json
{
  "type": "Row",
  "properties": {
    "type": "RowProps",
    "horizontalArrangement": "SpaceEvenly",
    "verticalAlignment": "CenterVertically"
  }
}
```

**Horizontal Arrangement:** `Start`, `End`, `Center`, `SpaceEvenly`, `SpaceBetween`, `SpaceAround`

**Vertical Alignment:** `Top`, `CenterVertically`, `Bottom`

## 🧩 Composy

Composy is a web and desktop app that facilitates the creation of JSON structures for json-to-compose. It provides a compose-tree editor, real-time preview, and export functionality. Composy is perfect for designing dynamic UIs and generating JSON definitions for your app.

### Features

- Compose-tree editor
- Real-time preview
- Export functionality

## 🧪 Testing & Testability

This library prioritizes rigorous UI testing. Beyond basic existence checks, it exposes visual properties (like `fontSize`, `color`, `padding`, and layout arrangements) through **Jetpack Compose Semantics**.

For detailed guidance, see:
- [Compose Multiplatform UI Testing](docs/COMPOSE_MULTIPLATFORM_TESTING.md) - General overview and tutorial.
  - **Note:** Do NOT use `./gradlew :library:test` for UI tests as it will cause a `NullPointerException` on Android Unit Tests. Use `./gradlew :library:desktopTest` instead.
- [Testing Guide (Phase 1)](docs/projects/phase-1-solidify-library/TESTING_GUIDE.md) - Project-specific testing patterns.
- [Semantics Phase](docs/projects/phase-4-semantics-testability/README.md) - Details on semantic-based testing.

## 📚 Documentation

WIP

## 🤝 Contributing

WIP

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Contact

If you have questions or suggestions, feel free to:

- Open an [issue](https://github.com/jesusdmedinac/json-to-compose/issues/new)
- Follow me on Social network:
  - [YouTube](https://www.youtube.com/@jesusdmedinac/)
  - [LinkedIn](https://www.linkedin.com/in/jesusdmedinac/)
  - [TikTok](https://www.tiktok.com/@jesusdmedinac)
  - [Facebook](https://www.facebook.com/jesusdmedinac)
  - [Instagram](https://www.instagram.com/jesusdmedinac)
  - [X](https://x.com/JesusDMedinaC)
  - [Github](https://github.com/jesusdmedinac)
  - [Medium](https://blog.jesusdmedinac.com)
  - [Discord](https://discord.gg/v5jv8k5b)
  - [Twitch](https://www.twitch.tv/jesusdmedinac)
- Send me an email at [hi@jesusdmedinac.com]

### Advanced Action Types

The library supports several advanced actions to help orchestrate more complex behavior directly from JSON.

**Navigate**
```json
{
  "action": "navigate",
  "route": "profile",
  "args": { "id": "123" }
}
```

**NavigateBack**
```json
{
  "action": "navigateBack"
}
```

**Delay**
```json
{
  "action": "delay",
  "durationMs": 1500
}
```

**Conditional**
Evaluates a boolean state (identified by `conditionStateKey`). If true, it executes the `thenActions`; otherwise, it executes the `elseActions`.
```json
{
  "action": "conditional",
  "conditionStateKey": "isLoggedIn",
  "thenActions": [{ "action": "navigate", "route": "home" }],
  "elseActions": [{ "action": "navigate", "route": "login" }]
}
```

**IncrementState** and **DecrementState**
For numeric state values (Float, Int), increments or decrements by the specified `amount`.
```json
{
  "action": "incrementState",
  "stateKey": "counter",
  "amount": 1.0
}
```

**LaunchUrl**
Opens an external URL.
```json
{
  "action": "launchUrl",
  "url": "https://example.com"
}
```

**UpdateList**
Adds or removes an item from a List state host. The `action` can be either `add` or `remove`.
```json
{
  "action": "updateList",
  "stateKey": "itemsList",
  "action": "add",
  "item": "New Item"
}
```
