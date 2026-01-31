Feature: Unit tests for ComposeNode tree functions
  As a library maintainer
  I want unit tests for ComposeNode's public functions
  To ensure tree traversal, ID generation, and serialization work correctly

  Scenario: countLevels for root node
    Given a ComposeNode with no parent
    When countLevels() is called
    Then the result is 0

  Scenario: countLevels for nested node
    Given a ComposeNode with a parent chain of depth 3
    When countLevels() is called
    Then the result is 3

  Scenario: parents for root node
    Given a ComposeNode with no parent
    When parents() is called
    Then the result is an empty list

  Scenario: parents for nested node
    Given a ComposeNode with a grandparent -> parent -> child chain
    When parents() is called on the child
    Then the result contains [parent, grandparent] in order

  Scenario: asList for leaf node
    Given a ComposeNode of type Text with no children
    When asList() is called
    Then the result contains only the node itself

  Scenario: asList for container with children
    Given a Column node with 3 Text children
    When asList() is called
    Then the result contains the Column and all 3 Text children

  Scenario: asList for node with single child
    Given a Button node with a Text child
    When asList() is called
    Then the result contains the Button and the Text child

  Scenario: asList for deep nested tree
    Given a Column with a Row child containing a Box with a Text
    When asList() is called on the Column
    Then the result contains all 4 nodes in depth-first order

  Scenario: id generation for root node
    Given a ComposeNode with no parent
    When the id property is accessed
    Then the id is "0"

  Scenario: id generation for child node
    Given a ComposeNode with a parent
    When the id property is accessed
    Then the id includes the parent id, type name, and sibling index

  Scenario: toString produces valid JSON
    Given a ComposeNode of type Text with TextProps(text = "Hello")
    When toString() is called
    Then the result is a valid JSON string that can be deserialized back to ComposeNode
