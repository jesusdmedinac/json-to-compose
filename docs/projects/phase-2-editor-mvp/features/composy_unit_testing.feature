Feature: Composy Unit Testing
  As a developer
  I want comprehensive unit tests for screen models in composy
  So that bugs and regressions in tree manipulation and state management are prevented

  Scenario: Test adding nodes to various container types
    Given the editor is running
    When I add a child node to a Button, Column, and Scaffold
    Then the children should be correctly appended without silent failures

  Scenario: Test updating nodes recursively
    Given a deeply nested component tree
    When I update the properties of a deeply nested leaf node
    Then the tree should reflect the updated properties and preserve other nodes

  Scenario: Test deleting nodes and selection behavior
    Given a tree with multiple siblings
    When I delete the currently selected node
    Then the node should be removed and the selection should jump to the next sibling or parent

  Scenario: Test node selection and expansion state
    Given a node is selected and collapsed
    When I expand the node
    Then the node's expansion state should be toggled correctly in the collapsedNodes list
