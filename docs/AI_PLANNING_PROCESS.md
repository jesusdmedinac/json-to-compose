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

## Step 3: Persist and Sync Features (The Context Anchor)

Create a `docs/features/` folder and ask the AI to create the `.feature` files with their scenarios. **This is also where the local planning is synchronized with GitHub.**

### 3.1. Persist locally
The conversation with the AI is ephemeral, but the complete functionality remains **persisted in your files**. If you lose the LLM's context window, you can resume the conversation without losing progress.

```
docs/features/
├── text_rendering.feature
├── column_layout.feature
└── ...
```

### 3.2. Sync with GitHub (Mandatory)
For every `.feature` file created, a corresponding **GitHub Issue** must be opened to track its progress in the cloud.

1.  **Create Issue:** Use the GitHub CLI to create an issue with the feature description.
    ```bash
    gh issue create --title "Feature: [Name]" --body-file docs/features/[name].feature
    ```
2.  **Assign to Milestone:** Link the issue to the current release target (e.g., `v0.1.0-alpha`).
3.  **Add to Project:** Add the issue to the GitHub Project and set its status to "In Progress".

---

## Step 4: Implement a PROGRESS.md File

Create a `PROGRESS.md` file at the project root that acts as a **development tracker**, identifying where you are in the project.

### Recommended structure

```markdown
# PROGRESS

## Feature: Rendering a Text component from JSON (#12)
- [x] Scenario: Render a basic Text
- [x] Scenario: Render a Text with padding modifier
- [ ] Scenario: Render a Text with custom color

## Feature: Column Layout from JSON (#15)
- [x] Scenario: Render a Column with children
- [ ] Scenario: Render a Column with vertical arrangement
```

**Note:** Always include the GitHub Issue number (#) next to the feature title for traceability.

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
6. A commit is made with a message referencing the scenario and the issue (e.g., `feat: basic Text [text.feature:Scenario 1] #12`).
7. The AI updates `PROGRESS.md` marking the scenario as completed.
8. **Finalize:** Once all scenarios in a feature are checked off, close the corresponding GitHub Issue.

### Iterative prompt example

> "Read PROGRESS.md, identify the next pending scenario, and develop it."

### Benefits

- **Atomic commits:** Each commit corresponds to a specific scenario.
- **Traceability:** You can trace which commit implements which functionality.
- **Context recovery:** If the LLM loses context, `PROGRESS.md` + the `.feature` files give it all the necessary information.
- **Incremental review:** Each change is small and easy to review.

---

## Summary

| Step | Action | Artifact | GitHub Action |
|------|--------|----------|---------------|
| 1 | Define the idea | High-level description | - |
| 2 | Generate Gherkin | Scenarios Gherkin | - |
| 3 | Persist and Sync | `docs/features/*.feature` | **Create Issue + Milestone** |
| 4 | Create tracker | `PROGRESS.md` | **Add to Project** |
| 5 | Develop scenario | Code + atomic commits | **Close Issue on completion** |

---

## Investment vs. Return

This process takes approximately 30 minutes of initial planning, but the savings in development time amount to hours. In the end, you achieve a **methodical, predictable, and efficient** system that allows you to focus on architecture and AI supervision, rather than on context management.