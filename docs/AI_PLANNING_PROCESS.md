# The 5-Step AI Planning Process

This document describes a methodical and repeatable process for planning and developing AI-assisted software. Any agent or developer can follow these steps to maintain context, traceability, and efficiency throughout the entire development cycle.

---

## Step 1: Define the Idea in Natural Language (High Level)

Capture the core idea of the project or milestone in plain language. You don't need complex technical specifications. Just the high-level ideas of what the program is expected to do.

**Key rules:**

- Focus on the first milestone, not the entire application lifecycle.
- Use natural language, without unnecessary technical jargon.
- Describe the *what*, not the *how*.

**Example:**

> "I want a library that takes a JSON and converts it into a native user interface using Jetpack Compose, controlled from the backend."

---

## Step 2: Request Features and Scenarios in Gherkin

Pass the idea to the AI agent (Claude, ChatGPT, or another LLM) and ask it to propose a series of **features** and **scenarios** in **Gherkin** format.

### Why Gherkin?

- It is a standard for defining features and scenarios.
- If you practice BDD/TDD, it establishes a direct relationship between functionality and tests.
- It provides a common language between business, development, and AI agents.

**Example prompt:**

> "From this idea, generate features and scenarios in Gherkin format that cover the main functionality of the first milestone."

**Expected output example:**

```gherkin
Feature: Rendering a Text component from JSON
  As a developer
  I want a JSON node of type Text to render as a Compose Text
  So that I can control the displayed text from the backend

  Scenario: Render a basic Text
    Given a JSON with a node of type "Text" and text "Hello World"
    When the node is processed by the renderer
    Then a Text component is displayed with the content "Hello World"

  Scenario: Render a Text with padding modifier
    Given a JSON with a node of type "Text" and a padding modifier of 16dp
    When the node is processed by the renderer
    Then a Text component is displayed with 16dp of padding applied
```

---

## Step 3: Persist Features in .feature Files

Create a `docs/features/` folder and ask the AI to create the `.feature` files with their scenarios.

```
docs/features/
├── text_rendering.feature
├── column_layout.feature
├── button_interaction.feature
└── ...
```

### This is the key to freeing up context

The conversation with the AI is ephemeral, but the complete functionality remains **persisted in your files**. If you lose the LLM's context window, you can resume the conversation without losing progress.

**Example prompt:**

> "Create the .feature files in docs/features/ with all the scenarios you just generated."

---

## Step 4: Implement a PROGRESS.md File

Create a `PROGRESS.md` file at the project root that acts as a **development tracker**, identifying where you are in the project.

### Recommended structure

```markdown
# PROGRESS

## Feature: Rendering a Text component from JSON
- [x] Scenario: Render a basic Text
- [x] Scenario: Render a Text with padding modifier
- [ ] Scenario: Render a Text with custom color

## Feature: Column Layout from JSON
- [x] Scenario: Render a Column with children
- [ ] Scenario: Render a Column with vertical arrangement
```

### Let the AI manage it

Ask the agent to create and maintain this checklist updated every time a step is completed. This way, your subsequent prompts are simply:

> "Develop the next scenario."

And the AI knows exactly where to continue.

---

## Step 5: Scenario-by-Scenario Development (Methodical)

Ask the AI to develop the code **scenario by scenario**. Each scenario is reviewed, tested, and committed independently.

### Flow per scenario

1. The AI reads `PROGRESS.md` to identify the next pending scenario.
2. The AI reads the corresponding `.feature` file to get the full context.
3. The AI implements the code needed to satisfy the scenario.
4. Tests are run (if applying BDD/TDD, the Gherkin scenario becomes a test).
5. The code is reviewed.
6. A commit is made with a message referencing the scenario.
7. The AI updates `PROGRESS.md` marking the scenario as completed.

### Iterative prompt example

> "Read PROGRESS.md, identify the next pending scenario, and develop it."

### Benefits

- **Atomic commits:** Each commit corresponds to a specific scenario.
- **Traceability:** You can trace which commit implements which functionality.
- **Context recovery:** If the LLM loses context, `PROGRESS.md` + the `.feature` files give it all the necessary information.
- **Incremental review:** Each change is small and easy to review.

---

## Summary

| Step | Action | Artifact |
|------|--------|----------|
| 1 | Define the idea in natural language | High-level description |
| 2 | Generate Gherkin features and scenarios | Scenarios in Gherkin format |
| 3 | Persist in `.feature` files | `docs/features/*.feature` |
| 4 | Create progress tracker | `PROGRESS.md` |
| 5 | Develop scenario by scenario | Code + atomic commits |

---

## Investment vs. Return

This process takes approximately 30 minutes of initial planning, but the savings in development time amount to hours. In the end, you achieve a **methodical, predictable, and efficient** system that allows you to focus on architecture and AI supervision, rather than on context management.