# Local GitHub Management (Issues & Projects)

This document explains how to manage GitHub issues and projects directly from your local environment using the command line and Visual Studio Code.

## 1. GitHub CLI (`gh`)

The GitHub CLI is the official tool for managing your repositories from the terminal. It is already installed on your system.

### Managing Issues

Create, list, and view issues without leaving your terminal.

```bash
# List open issues in the current repository
gh issue list

# Create a new issue
gh issue create --title "Short description" --body "Long description"

# View a specific issue by number
gh issue view 123
```

### Managing Projects (v2)

Manage project boards and items using the `project` extension of the CLI.

```bash
# List projects for a specific owner (user or organization)
gh project list --owner <username>

# View details of a project
gh project view <project-number> --owner <username>

# Add a pull request or issue to a project
gh project item-add <project-number> --owner <username> --url <issue-url>
```

---

## 2. VS Code Extension: "GitHub Pull Requests and Issues"

For a graphical interface within the editor, use the official extension.

- **Integrated Issues View:** Browse and filter issues in a dedicated sidebar tab.
- **Workflow Integration:** Use the "Start working on issue" feature to automatically create and link branches.
- **Contextual Actions:** Create issues from `// TODO` comments or highlighted text.

## 3. Benefits of Local Management

- **Efficiency:** Context switching between your editor and browser is minimized.
- **Automation:** Scripts can be used to automate repetitive tasks like project card creation.
- **Consistency:** Maintain a unified workflow for both code and project management.

---

_For more information, refer to the [GitHub CLI Manual](https://cli.github.com/manual/) or the [VS Code Extension Marketplace](https://marketplace.visualstudio.com/items?itemName=GitHub.vscode-pull-request-github)._
