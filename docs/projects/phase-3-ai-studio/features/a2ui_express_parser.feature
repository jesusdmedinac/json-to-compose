Feature: A2UI Express DSL Engine
  As a developer and AI agent
  I want a fast, linear, error-resilient A2UI Express DSL parser
  So that LLMs can generate and stream Compose component trees with minimal token overhead

  Scenario: Parse standard linear components
    Given an A2UI Express DSL snippet defining a Column with a Text child
    When the parser compiles the snippet
    Then a ComposeNode of type Column should be created with the Text child

  Scenario: Parse modifier chains in A2UI Express
    Given an A2UI Express DSL snippet with modifiers fillMaxWidth and padding
    When the parser compiles the snippet
    Then the resulting ComposeNode should contain the corresponding typed ComposeModifier operations

  Scenario: Incremental streaming resilience
    Given a partial streaming A2UI Express buffer with an incomplete trailing line
    When the compiler processes the partial buffer
    Then all completed lines are parsed into the tree and the incomplete line is safely ignored

  Scenario: Bidirectional conversion from ComposeNode to A2UI Express
    Given an existing ComposeNode tree
    When the tree is exported to A2UI Express DSL
    Then a concise, linear, line-oriented string representation should be produced
