Feature: Multi-platform compilation and code organization
  As a developer using json-to-compose
  I want the demo app to compile on all 4 target platforms with organized code
  To ensure the showcase app is accessible and maintainable

  Background:
    The json-to-compose library supports Android, iOS, Desktop, and WASM.
    The demo app must compile and run on all platforms. Code should be
    organized into separate files by screen for maintainability.

  Scenario: Compiles on Android
    Given the demo app code is complete
    When I build the Android target
    Then the build completes without errors
    And the app can be installed and launched on an Android device or emulator

  Scenario: Compiles on Desktop
    Given the demo app code is complete
    When I build the Desktop target
    Then the build completes without errors
    And the app can be launched as a desktop application

  Scenario: Compiles on iOS
    Given the demo app code is complete
    When I build the iOS target
    Then the build completes without errors
    And the app can be installed and launched on an iOS device or simulator

  Scenario: Compiles on WASM
    Given the demo app code is complete
    When I build the WASM target
    Then the build completes without errors
    And the app can be served and opened in a web browser

  Scenario: Code organized into separate files
    Given the demo app refactoring is complete
    When I inspect the composeApp source directory
    Then App.kt contains the app shell, navigation setup, and CompositionLocals
    And CatalogScreen.kt contains the catalog home screen content
    And ComponentsScreen.kt contains the component demos
    And StylesScreen.kt contains the modifier gallery
    And DemoHelpers.kt contains shared helper functions and color palette

  Scenario: No navigation dependency added to library module
    Given the navigation-compose dependency is added to composeApp
    When I inspect the library module's build.gradle.kts
    Then the library module does not have a dependency on navigation-compose
    And the library module's public API remains unchanged
