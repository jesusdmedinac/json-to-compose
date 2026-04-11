# Git Worktrees Guide for AI Agents

> [!IMPORTANT]
> This guide outlines how AI agents should use Git Worktrees to work on concurrent features or tasks without stepping on each other's branches or causing file conflicts in the main working directory.

## What is a Git Worktree?

A Git Worktree allows multiple working trees (directories) to be attached to the same Git repository. This means you can have multiple branches checked out simultaneously in different folders on your computer, all sharing the same local repository history, configuration, and state.

**Why is this important for AI Agents?**
If two AI agents are working on the same repository in the same folder, and one switches branches (e.g., `git checkout feature-B`), the files on disk change for the other agent (who might have been on `feature-A`). This causes confusion, lost work, and immediate conflicts. Worktrees solve this by giving each agent its own physical isolated folder.

## Standard Workflow for New Features

Whenever an AI agent is asked to implement a new feature or task that requires a separate branch, follow this process:

### 1. Create a New Worktree

Instead of cloning the repository or checking out a branch in the main project folder, create a new worktree in a directory **adjacent** to the main project folder.

```bash
# Assuming you are currently in the main repository folder (e.g., ~/path/to/project)
git worktree add ../<project-name>-<feature-name> -b <feature-branch-name>
```

**Example:**
If the project is `json-to-compose` and the feature is `add-padding`:

```bash
git worktree add ../json-to-compose-add-padding -b feat/add-padding
```

### 2. Work within the Worktree

Once created, the AI agent must `cd` into the new worktree directory or set it as its active workspace for all subsequent commands and file edits.

```bash
cd ../<project-name>-<feature-name>
```

**Important:** All standard Git commands (`add`, `commit`, `push`, `status`) work exactly the same way inside the worktree folder as they do in the main repository folder.

### 2.5 Switching between Worktrees

If you need to switch your current workspace to a different feature that already has a worktree set up:

1. **List existing worktrees:**
   ```bash
   git worktree list
   ```

2. **Change directory to the desired worktree:**
   ```bash
   cd <path-from-list>
   ```

### 3. Sync and Push

Commit your changes frequently as per the standard atomic commit guidelines. Push the branch to the remote repository when ready for review or merging.

```bash
git push -u origin <feature-branch-name>
```

**CRITICAL: Create a Pull Request (PR)**
After pushing, always use the GitHub CLI (`gh pr create`) to create a Pull Request linked to the original Issue. **The PR is a fundamental step** so the developer can review the implemented code before it is merged into the main branch. Do not merge it yourself.

### 4. Cleanup (Post-Merge or Post-Task)

Once the feature is merged, or the worktree is no longer needed, it is crucial to clean it up to save disk space and remove dangling references.

1. **Delete the physical folder:**

   ```bash
   # From the main repository folder or the parent directory
   rm -rf ../<project-name>-<feature-name>
   ```

2. **Prune the worktree reference from Git:**

   ```bash
   # Must be run from inside any valid repository folder (main or another worktree)
   git worktree prune
   ```

3. **Delete the local branch (Optional, but recommended after merge):**
   ```bash
   git branch -d <feature-branch-name>
   ```

## Best Practices

- **Naming Convention:** Always name the worktree folder clearly by appending the feature or agent identifier to the project name (e.g., `../project-agent1`, `../project-feat-x`).
- **Never nest worktrees:** Always create the worktree folder _outside_ the main repository folder (using `../`) to prevent Git from treating the new worktree as untracked files in the main repository.
- **Shared History:** Remember that all worktrees share the same local Git database. Fetching in one worktree updates the references for all of them.
