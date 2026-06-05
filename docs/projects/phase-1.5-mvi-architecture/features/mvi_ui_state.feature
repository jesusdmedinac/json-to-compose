Feature: MVI UI State
  As a developer
  I want to handle UI interactions (panels, viewports, search) through Intents
  So that the UI layout is strictly driven by EditorState

  Scenario: Migrate Panel Display toggles
    Given the left and right panels
    When SetLeftPanelDisplayed or SetRightPanelDisplayed intents are dispatched
    Then the EditorState boolean flags should update

  Scenario: Migrate Viewport Modes
    Given the device preview
    When SetViewportMode intent is dispatched
    Then the EditorState should update its viewport enum

  Scenario: Migrate Keyword Search
    Given the components drawer
    When SetKeyword intent is dispatched
    Then the EditorState should filter the displayed types based on the keyword
