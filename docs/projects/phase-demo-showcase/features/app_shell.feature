Feature: App shell with Scaffold and design language
  As a developer using json-to-compose
  I want a consistent app shell with TopAppBar and BottomBar
  To provide a polished, professional demo experience

  Background:
    The app shell wraps all screens in a Scaffold with a TopAppBar
    displaying the app title and a BottomBar with navigation items.
    A consistent color palette and helper functions ensure visual
    coherence across all screens.

  Scenario: App shell renders a Scaffold with TopAppBar
    Given the App composable is the entry point
    When the app renders
    Then a Scaffold is displayed with a TopAppBar
    And the TopAppBar displays the title "JSON to Compose"
    And the TopAppBar uses the primary color from the design palette

  Scenario: App shell renders a BottomBar with three items
    Given the Scaffold is rendered
    When the BottomBar is displayed
    Then it contains three BottomNavigationItems
    And the items are labeled "Catalog", "Components", and "Styles"
    And each item has a distinct icon or visual indicator

  Scenario: Consistent color palette applied across screens
    Given the app defines a color palette with primary, secondary, and accent colors
    When any screen renders section headers, backgrounds, or highlights
    Then the colors used match the defined palette
    And the palette is defined in DemoHelpers.kt as reusable constants

  Scenario: Helper functions produce reusable styled nodes
    Given DemoHelpers.kt defines helper functions
    When a screen needs a section header, divider, or demo label
    Then it uses sectionHeader(), sectionDivider(), or demoLabel() from DemoHelpers
    And the helpers return ComposeNode trees with consistent styling
    And the helpers are shared across all three screens
