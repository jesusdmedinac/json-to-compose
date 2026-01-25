Feature: KDoc on all public API
  As a developer consuming the json-to-compose library
  I want KDoc documentation on all public classes, functions, and properties
  To understand the API from IDE autocompletion and generate automatic documentation

  Scenario: KDoc on ComposeNode data class
    Given the public class ComposeNode
    When a developer accesses the class from their IDE
    Then they see KDoc documentation describing it is a component tree node
    And each property (type, properties, composeModifier, parent, id) has documentation

  Scenario: KDoc on ComposeType enum
    Given the public enum ComposeType
    When a developer accesses the enum from their IDE
    Then they see KDoc documentation describing each component type available
    And each entry (Text, Column, Row, Box, etc.) has a brief description

  Scenario: KDoc on NodeProperties sealed interface
    Given the sealed interface NodeProperties and all its implementations
    When a developer accesses NodeProperties from their IDE
    Then they see KDoc documentation describing the property system
    And each implementation (TextProps, ColumnProps, etc.) documents its parameters

  Scenario: KDoc on ComposeModifier and ModifierOperation
    Given the public classes ComposeModifier and ModifierOperation
    When a developer accesses them from their IDE
    Then they see KDoc documentation describing the modifier system
    And each operation (Padding, Width, BackgroundColor, etc.) documents its parameters and expected values

  Scenario: KDoc on Behavior interface
    Given the public interface Behavior
    When a developer accesses it from their IDE
    Then they see KDoc documentation explaining how to handle component events
    And the onEvent method documents the eventName parameter

  Scenario: KDoc on StateHost interface
    Given the public interface StateHost
    When a developer accesses it from their IDE
    Then they see KDoc documentation explaining how to handle component state
    And each method documents its parameters and expected behavior

  Scenario: KDoc on CompositionLocals
    Given the public CompositionLocals (LocalBehavior, LocalStateHost, LocalDrawableResources, LocalCustomRenderers)
    When a developer accesses them from their IDE
    Then they see KDoc documentation explaining how to provide dependencies to the component tree
    And includes usage example with CompositionLocalProvider

  Scenario: KDoc on extension functions
    Given the public extension functions (String.ToCompose(), ComposeNode.ToCompose())
    When a developer accesses them from their IDE
    Then they see KDoc documentation explaining the entry point for rendering JSON
    And includes basic usage example

  Scenario: Documentation generation with Dokka
    Given all public classes have KDoc
    When the Dokka task is executed
    Then navigable HTML documentation is generated without warnings of undocumented elements