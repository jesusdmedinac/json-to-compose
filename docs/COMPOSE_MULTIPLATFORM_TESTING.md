# Testing Compose Multiplatform UI

UI testing in Compose Multiplatform is implemented using the same finders, assertions, actions, and matchers as the Jetpack Compose testing API.

> [!IMPORTANT]
> The API is Experimental and may change in the future.

## How Compose Multiplatform testing is different from Jetpack Compose

Compose Multiplatform common test API **does not rely on JUnit's TestRule class**. Instead, you call the `runComposeUiTest` function and invoke the test functions on the `ComposeUiTest` receiver.

However, JUnit-based API is available for desktop targets if needed.

## Setup for Tests

To use Compose Multiplatform testing, ensure the following dependencies are in your `build.gradle.kts`:

```kotlin
kotlin {
    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
        }
        val jvmTest by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}
```

For Android instrumented tests:
```kotlin
android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}
dependencies {
    androidTestImplementation("androidx.compose.ui:ui-test-junit4-android:1.10.2")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.10.2")
}
```

## Writing Common Tests

In the `src/commonTest/kotlin` directory, use `runComposeUiTest` to wrap your test logic.

### Example Basic Test

```kotlin
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import kotlin.test.Test

class ExampleTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun myTest() = runComposeUiTest {
        setContent {
            var text by remember { mutableStateOf("Hello") }
            Text(
                text = text,
                modifier = Modifier.testTag("text")
            )
            Button(
                onClick = { text = "Compose" },
                modifier = Modifier.testTag("button")
            ) {
                Text("Click me")
            }
        }

        onNodeWithTag("text").assertTextEquals("Hello")
        onNodeWithTag("button").performClick()
        onNodeWithTag("text").assertTextEquals("Compose")
    }
}
```

### Testing json-to-compose Renderers

When testing renderers in this project, you typically use `CompositionLocalProvider` to inject dependencies like `LocalStateHost` or `LocalBehavior`.

```kotlin
@Test
fun textFieldRendersAndEmitsStateChange() = runComposeUiTest {
    var currentValue by mutableStateOf("initial")
    val mockStateHost = object : StateHost<String> {
        override val state: String get() = currentValue
        override fun onStateChange(state: String) {
            currentValue = state
        }
    }

    val node = ComposeNode(
        type = ComposeType.TextField,
        properties = NodeProperties.TextFieldProps(
            valueStateHostName = "search"
        )
    )

    setContent {
        CompositionLocalProvider(
            LocalStateHost provides mapOf("search" to mockStateHost)
        ) {
            node.ToTextField()
        }
    }

    onNodeWithText("initial").assertExists()
    onNodeWithText("initial").performTextReplacement("kotlin")
    assertEquals("kotlin", currentValue)
}
```

## Platform-Specific Configuration

### Android Instrumented Tests

If you want your `commonTest` UI tests to run as instrumented tests on Android devices or emulators, you must configure the `androidTarget` in `build.gradle.kts`:

```kotlin
kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    }
}
```

This ensures that the tests in `commonTest` are included in the Android instrumented test variant.

## Running Tests

### Mandatory Testing Rules for Agents

> [!CAUTION]
> **DO NOT** use `./gradlew :library:test` to run UI tests. This command attempts to run tests as standard Android Unit Tests, which lacks a UI environment and will result in a `NullPointerException` (typically at the import or initialization level).

To verify UI changes, you **must** use target-specific commands:

| Platform | Command | Recommended Use |
|----------|---------|-----------------|
| **Desktop (JVM)** | `./gradlew :library:desktopTest` | **Primary validation.** Fast, headless-capable, and reliable for common logic. |
| iOS Simulator | `./gradlew :library:iosSimulatorArm64Test` | Platform-specific UI validation. |
| Android (Inst.) | `./gradlew :library:connectedDebugAndroidTest` | Real device/emulator validation. |
| Wasm (Browser) | `./gradlew :library:wasmJsBrowserTest` | Web environment validation. |

### Why `./gradlew :library:test` fails?

In a Kotlin Multiplatform project, the `:library:test` task is a meta-task that triggers all platform unit tests. On Android, the `test` task (e.g., `testDebugUnitTest`) runs in a "headless" JVM environment without the Compose UI machinery or Android resources. When `runComposeUiTest` is invoked in this environment, it fails to initialize the UI host, leading to a `NullPointerException`.

## Best Practices for Agents

1. **Always use `testTag`**: Every renderer must apply `.testTag(type.name)` to its root component to facilitate testing.
2. **Prefer `commonTest`**: Write tests in `commonTest` so they validate logic across all targets.
3. **Use Semantic Assertions**: Instead of just checking existence, check properties like font size or color using custom semantics if necessary (see `GEMINI.md`).
4. **Mock Behaviors and State**: Use the `CompositionLocalProvider` pattern to isolate components from real backend or navigation logic.

For more details on the specific testing strategy of this project, see [Testing Guide (Phase 1)](projects/phase-1-solidify-library/TESTING_GUIDE.md).
