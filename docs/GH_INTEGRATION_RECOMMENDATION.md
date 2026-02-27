# Recommendation: Integrating GitHub with your Planning Process

Based on your [5-Step AI Planning Process](docs/AI_PLANNING_PROCESS.md) and current [Roadmap](docs/projects/PROGRESS.md), here is the most efficient way to manage your work on GitHub as a solo developer.

## The Strategy: "High-Level Roadmap + Low-Level Execution"

For a solo developer, the goal is to **minimize administrative overhead** while maintaining **visibility**.

### 1. GitHub Projects (v2): The Roadmap (Phases)

**Use for:** Step 1 & Step 4 (High-level tracking)

Create a single GitHub Project (v2) for the repository.

- **Views:**
  - **Table View:** List all **Phases** (Phase 1, 2, 3...) with status (Done, In Progress, Planned).
  - **Roadmap View:** Visualize the timeline and dependencies between phases.
- **Why:** It gives you a bird's-eye view of the project without cluttering your issue tracker with high-level items.

### 2. GitHub Issues: The Unit of Work (Features)

**Use for:** Step 2 & Step 3 (Features from Gherkin)

Each `.feature` file should correspond to a **GitHub Issue**.

- **Description:** Paste the Gherkin `Feature` description.
- **Task List:** Convert Gherkin `Scenarios` into a Markdown task list (`- [ ]`).
- **Automation:** Use the GitHub CLI to create these issues quickly:
  ```bash
  gh issue create --title "Feature: Text Enhancement" --body-file docs/features/text_enhancement.feature
  ```

### 3. Git Commits: The Traceability (Scenarios)

**Use for:** Step 5 (Development)

Reference the Issue number in your atomic commits for each scenario.

- **Commit message example:** `feat: support textAlign property [text_enhancement.feature:Scenario 4] #12`
- **Why:** This automatically links your code changes to the Issue (the Feature) and the specific scenario you were working on.

---

## Issue vs. Project: Which one to use?

| Level           | Granularity                     | Recommended Tool                  |
| --------------- | ------------------------------- | --------------------------------- |
| **Roadmap**     | Global (All Phases)             | **GitHub Project**                |
| **Milestone**   | One Phase (Solidify, Expand...) | **GitHub Milestone** (optional)   |
| **Logic Unit**  | One Feature (.feature file)     | **GitHub Issue**                  |
| **Action Unit** | One Scenario                    | **Task List item** (inside Issue) |

## Suggested Hybrid Workflow

1.  **Plan locally** using your current 5-step process (Natural language -> Gherkin -> Files).
2.  **Sync to GitHub** only when a phase or feature is ready to be implemented:
    - Update the **Project** status to "In Progress".
    - Create/Open the **Issue** for the current Feature.
3.  **Develop** locally. Your commits will "speak" to your GitHub Issues.
4.  **Close** the Issue when all scenarios are checked off.

> [!TIP]
> Since you already use the GitHub CLI (`gh`), you can integrate the issue creation into your "Step 3" of the planning process. This way, documenting on GitHub becomes a 1-second task instead of a manual chore.
