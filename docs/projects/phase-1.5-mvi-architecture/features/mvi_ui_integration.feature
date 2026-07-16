Feature: MVI UI Integration
  As a developer
  I want the editor UI to dispatch EditorIntents and render from EditorState
  So that the centralized MVI flow drives the application and legacy ScreenModels can be removed

  Scenario: Tree panel dispatches intents
    Given the compose tree panel
    When the user selects, adds, deletes, or reorders a node
    Then the corresponding EditorIntent should be dispatched to EditorScreenModel
    And the tree should render from EditorState.rootNode

  Scenario: Node editor dispatches intents
    Given the node editor panel with a selected node
    When the user changes the node type, text, or modifiers
    Then EditorIntent.UpdateNodeType, UpdateNodeText, or the modifier intents should be dispatched
    And the editor fields should render from the selected node in EditorState

  Scenario: Preview renders from EditorState
    Given the real-time preview
    When any EditorIntent updates the root node
    Then the preview should recompose from EditorState.rootNode
    And changing a node type should never make the preview disappear (regression for #86)

  Scenario: Legacy ScreenModels removed
    Given all UI components consume EditorScreenModel
    When the legacy ScreenModels are deleted
    Then the project should compile and all tests should pass
    And no code should reference ComposeTreeScreenModel, EditNodeScreenModel, ComposeComponentsScreenModel, or MainScreenModel
    And AuthScreenModel should remain, as authentication is outside the editor's MVI scope
