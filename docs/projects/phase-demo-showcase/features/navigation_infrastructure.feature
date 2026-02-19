Feature: Navigation infrastructure for multi-screen demo app
  As a developer using json-to-compose
  I want the demo app to have navigation between multiple screens
  To showcase the library's capabilities in an organized, professional way

  Background:
    The demo app currently renders all components in a single LazyColumn.
    Adding navigation-compose enables a multi-screen experience with
    a BottomBar for switching between Catalog, Components, and Styles screens.

  Scenario: Add navigation-compose dependency to composeApp
    Given the composeApp module exists
    When I add the navigation-compose dependency to gradle/libs.versions.toml
    And I add the dependency to composeApp/build.gradle.kts
    Then the project syncs successfully with the new dependency
    And the library module does not depend on navigation-compose

  Scenario: Define screen routes as serializable objects
    Given navigation-compose is available in composeApp
    When I define screen routes as @Serializable objects
    Then there are routes for Catalog, Components, and Styles
    And each route is a serializable data object

  Scenario: Provide NavHostController via CompositionLocal
    Given screen routes are defined
    When I create a LocalNavController CompositionLocal
    And I provide a NavHostController at the app root
    Then any composable in the tree can access the NavHostController

  Scenario: Register NavHost as a Custom renderer
    Given NavHostController is provided via CompositionLocal
    When I register a "NavHost" custom renderer in LocalCustomRenderers
    Then the NavHost renderer navigates to a destination using the NavHostController
    And the NavHost contains composable destinations for each screen route

  Scenario: Bottom bar navigates between screens
    Given NavHost is registered as a custom renderer
    And the BottomBar has three BottomNavigationItems
    When a BottomNavigationItem is clicked
    Then the corresponding Behavior triggers navigation via NavHostController
    And the NavHost displays the target screen

  Scenario: Bottom navigation reflects the currently selected screen
    Given navigation between screens is working
    When the user navigates to a screen
    Then the corresponding BottomNavigationItem shows as selected
    And the other BottomNavigationItems show as unselected
    And the selected state is managed via StateHost
