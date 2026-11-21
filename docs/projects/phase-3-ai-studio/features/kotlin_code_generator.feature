Feature: Kotlin Compose Code Generator
  As a developer using Composy AI Studio
  I want to view and copy clean, idiomatic Jetpack Compose Kotlin code from my design
  So that I can paste it directly into production Android, iOS, Desktop, or Web apps

  Scenario: Code View Tab toggle
    Given a design rendered in the canvas
    When the user toggles the Code view tab
    Then clean Kotlin Compose code decorated with @Composable is shown

  Scenario: Idiomatic Jetpack Compose translation
    Given a ComposeNode tree with layout, modifiers, and child components
    When the code generator transpiles the tree
    Then valid, idiomatic Kotlin code formatted with standard Compose indentation is output

  Scenario: One-click copy and export
    Given the generated Kotlin code
    When the user clicks the copy code button
    Then the code is copied to the clipboard
