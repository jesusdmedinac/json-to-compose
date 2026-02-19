Feature: BottomBar Component rendered from JSON
  As a developer using json-to-compose
  I want to be able to define a BottomBar in JSON
  To create bottom navigation bars controlled from the backend

  Scenario: Render a BottomBar with navigation items
    Given a JSON with a node of type "BottomBar" with BottomBarProps and 3 items
    And each item has label, iconName, and eventName
    When the node is processed by the renderer
    Then a Material 3 NavigationBar with 3 NavigationBarItems is shown

  Scenario: Render a BottomBar with selected item
    Given a JSON with a BottomBar with BottomBarProps(selectedIndex = 1)
    When the node is processed by the renderer
    Then the second item is shown as selected

  Scenario: BottomBar emits event when item is selected
    Given a rendered BottomBar with items having eventNames ["tab_home", "tab_search", "tab_profile"]
    And a registered Behavior
    When the user presses the third item
    Then Behavior.onEvent is invoked with eventName "tab_profile"

  Scenario: BottomBar integrated with Scaffold
    Given a JSON with a Scaffold that has a BottomBar as bottomBar
    When the tree is processed by the renderer
    Then a Scaffold with the BottomBar positioned at the bottom is shown

  Scenario: Serialize and deserialize a BottomBar from JSON
    Given a JSON string with type "BottomBar" and all its properties
    When deserialized to ComposeNode and serialized back
    Then the resulting JSON maintains all BottomBar properties