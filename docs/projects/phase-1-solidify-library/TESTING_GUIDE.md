# Testing Guide - Phase 1: Solidify json-to-compose Library

This guide describes how to set up, write, and run UI tests for the `library/` module using the Compose Multiplatform testing API.

## Key Concepts

UI testing in Compose Multiplatform uses the same **finders**, **assertions**, **actions**, and **matchers** as the Jetpack Compose testing API, with one important difference:

> The common test API does **not** rely on JUnit's `TestRule` class. Instead, you call the `runComposeUiTest` function and invoke test functions on the `ComposeUiTest` receiver.

JUnit-based API is available only for the desktop (JVM) target.

The API is **Experimental** and may change in the future.

## Setup in `library/build.gradle.kts`

The `library` module already has a `commonTest` source set. To enable Compose UI testing, the following dependencies need to be configured:

### 1. Add Compose UI Test dependency to `commonTest`

```kotlin
kotlin {
    sourceSets {
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)

                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.uiTest)
            }
        }
    }
}
```

### 2. Add desktop test dependency

A `desktopTest` source set needs the current OS dependency to run tests on desktop:

```kotlin
kotlin {
    sourceSets {
        val desktopTest by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}
```

### 3. Android instrumented tests (already configured)

The module already has Android instrumented test configuration:

```kotlin
androidInstrumentedTest.dependencies {
    implementation("androidx.test.ext:junit:1.1.5")
    implementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.compose.ui:ui-test-junit4:1.7.0")
}
```

And the `testInstrumentationRunner` is already set:

```kotlin
android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}
```

### 4. iOS test configuration

For iOS simulator tests to work, ensure the `instrumentedTestVariant` is configured if running instrumented tests:

```kotlin
kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    }
}
```

## Test Directory Structure

```
library/src/
├── commonTest/kotlin/com/jesusdmedinac/jsontocompose/
│   ├── renderer/
│   │   ├── TextRendererTest.kt
│   │   ├── ColumnRendererTest.kt
│   │   ├── RowRendererTest.kt
│   │   ├── BoxRendererTest.kt
│   │   ├── ButtonRendererTest.kt
│   │   ├── ImageRendererTest.kt
│   │   ├── TextFieldRendererTest.kt
│   │   ├── ScaffoldRendererTest.kt
│   │   ├── LazyColumnRendererTest.kt
│   │   ├── LazyRowRendererTest.kt
│   │   └── CustomRendererTest.kt
│   ├── modifier/
│   │   └── ModifierMapperTest.kt
│   ├── model/
│   │   ├── ComposeNodeSerializationTest.kt
│   │   └── ComposeModifierSerializationTest.kt
│   ├── pipeline/
│   │   └── JsonToComposePipelineTest.kt
│   └── validation/
│       └── JsonSchemaValidationTest.kt
├── desktopTest/kotlin/
│   └── (desktop-specific tests if needed)
```

## Writing Tests

### Basic Pattern: `runComposeUiTest`

All common tests use `runComposeUiTest` instead of JUnit test rules:

```kotlin
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

class TextRendererTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun textRendersCorrectly() = runComposeUiTest {
        setContent {
            // Set up the composable under test
        }

        // Assert and interact
    }
}
```

### Testing a Component Renderer

To test that a `ComposeNode` renders correctly, build a node, call `ToCompose()` inside `setContent`, and assert the result:

```kotlin
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.test.Test

class TextRendererTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun textDisplaysCorrectContent() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(
                text = "Hello World"
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithText("Hello World").assertExists()
    }
}
```

### Testing with Behaviors (Click Events)

Use `CompositionLocalProvider` to inject mock behaviors:

```kotlin
import androidx.compose.runtime.CompositionLocalProvider
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.behavior.Behavior

@OptIn(ExperimentalTestApi::class)
@Test
fun buttonEmitsClickEvent() = runComposeUiTest {
    var clicked = false
    val mockBehavior = object : Behavior {
        override fun onClick() { clicked = true }
    }

    val node = ComposeNode(
        type = ComposeType.Button,
        properties = NodeProperties.ButtonProps(
            onClickEventName = "test_click",
            child = ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(text = "Click me")
            )
        )
    )

    setContent {
        CompositionLocalProvider(
            LocalBehavior provides mapOf("test_click" to mockBehavior)
        ) {
            node.ToCompose()
        }
    }

    onNodeWithText("Click me").performClick()
    assertTrue(clicked)
}
```

### Testing with StateHost (TextField)

```kotlin
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.state.StateHost

@OptIn(ExperimentalTestApi::class)
@Test
fun textFieldReflectsState() = runComposeUiTest {
    var currentValue = "initial"
    val mockStateHost = object : StateHost<String> {
        override val state: String get() = currentValue
        override fun onStateChange(newState: String) { currentValue = newState }
    }

    val node = ComposeNode(
        type = ComposeType.TextField,
        properties = NodeProperties.TextFieldProps(
            stateHostName = "test_field"
        )
    )

    setContent {
        CompositionLocalProvider(
            LocalStateHost provides mapOf("test_field" to mockStateHost)
        ) {
            node.ToCompose()
        }
    }

    // Assert and interact with the text field
}
```

### Testing Serialization (Unit Tests, No UI)

Serialization tests don't need `runComposeUiTest`:

```kotlin
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlin.test.Test
import kotlin.test.assertEquals

class ComposeNodeSerializationTest {

    @Test
    fun roundtripSerializationPreservesNode() {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Hello")
        )

        val json = Json.encodeToString(node)
        val decoded = Json.decodeFromString<ComposeNode>(json)

        assertEquals(node, decoded)
    }
}
```

### Testing Modifiers

```kotlin
@OptIn(ExperimentalTestApi::class)
@Test
fun paddingModifierApplied() = runComposeUiTest {
    val node = ComposeNode(
        type = ComposeType.Text,
        properties = NodeProperties.TextProps(text = "Padded"),
        composeModifier = ComposeModifier(
            operations = listOf(
                ComposeModifier.Operation.Padding(value = 16)
            )
        )
    )

    setContent {
        node.ToCompose()
    }

    onNodeWithText("Padded").assertExists()
}
```

## Common Finders, Assertions, and Actions

### Finders

| Finder | Description |
|--------|-------------|
| `onNodeWithText("text")` | Find node by displayed text |
| `onNodeWithTag("tag")` | Find node by `testTag` modifier |
| `onNodeWithContentDescription("desc")` | Find by content description |
| `onAllNodesWithText("text")` | Find all nodes matching text |
| `onRoot()` | The root node of the tree |

### Assertions

| Assertion | Description |
|-----------|-------------|
| `.assertExists()` | Node is in the tree |
| `.assertDoesNotExist()` | Node is not in the tree |
| `.assertIsDisplayed()` | Node is visible |
| `.assertTextEquals("text")` | Node text matches exactly |
| `.assertTextContains("text")` | Node text contains substring |
| `.assertIsEnabled()` | Node is enabled |
| `.assertIsNotEnabled()` | Node is disabled |
| `.assertHasClickAction()` | Node is clickable |

### Actions

| Action | Description |
|--------|-------------|
| `.performClick()` | Click the node |
| `.performTextInput("text")` | Type text into a text field |
| `.performTextClearance()` | Clear text in a text field |
| `.performScrollTo()` | Scroll until node is visible |

## Running Tests

### Desktop (recommended for fast iteration)

```bash
./gradlew :library:desktopTest
```

### Android (unit tests)

```bash
./gradlew :library:testDebugUnitTest
```

### Android (instrumented, requires emulator)

```bash
./gradlew :library:connectedDebugAndroidTest
```

### iOS Simulator

```bash
./gradlew :library:iosSimulatorArm64Test
```

### WASM (headless browser)

```bash
./gradlew :library:wasmJsBrowserTest
```

### All platforms

```bash
./gradlew :library:allTests
```

## Test Naming Conventions

Follow the pattern: `{what}_{condition}_{expectedResult}`

```
textDisplaysCorrectContent
columnRendersChildrenInOrder
buttonEmitsClickEvent_whenClicked
modifierPadding_appliesCorrectPadding
serializationRoundtrip_preservesTextNode
invalidJson_producesDescriptiveError
```

## Notes for Phase 1 Testing

1. **Start with serialization tests** — They are pure unit tests, don't require UI, and run fastest.
2. **Then component renderer tests** — Use `runComposeUiTest` to verify each component renders from a `ComposeNode`.
3. **Then integration/pipeline tests** — Test the full JSON string → `ComposeNode` → rendered UI flow.
4. **Snapshot tests last** — These require additional setup and platform-specific configuration.
5. **Run desktop tests first** during development — They are the fastest to execute and provide quick feedback.
6. **All tests should be in `commonTest`** — This ensures they run on all platforms automatically.
7. **Use `@OptIn(ExperimentalTestApi::class)`** — The Compose test API is still experimental.