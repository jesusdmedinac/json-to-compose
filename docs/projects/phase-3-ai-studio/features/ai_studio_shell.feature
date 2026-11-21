Feature: AI Studio Shell UI
  As a Composy AI Studio user
  I want a clean 2-pane workspace with conversational prompts and a live Compose preview
  So that I can generate, iterate, and refine UI rapidly without manual forms

  Scenario: Modern Studio Split Layout
    Given the Composy AI Studio application
    When the view is launched
    Then a 2-pane layout should be displayed with chat on the left and live canvas on the right

  Scenario: Prompt submission dispatches LLM Intent
    Given the prompt input field with user text
    When the user submits the prompt
    Then the prompt is added to the conversation history and streaming starts

  Scenario: Live streaming token recomposition
    Given an active streaming response from the AI provider
    When new A2UI Express lines are received
    Then EditorState updates progressively and the canvas recomposes smoothly

  Scenario: Iterative multi-turn refinement
    Given an existing screen layout on the canvas and a follow-up refinement prompt
    When the AI agent processes the update
    Then the requested mutations are applied while preserving unaffected subtrees
