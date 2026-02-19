# Phase 2: Functional Editor (MVP) - Composy

## Idea (Step 1)

The visual editor composy already has a functional base: a component tree with real-time preview, component palette with search, property editor, and export to JSON. However, it has critical bugs and missing features that make it unusable for a real workflow.

The goal of this phase is to turn composy from a buggy prototype into a functional MVP editor that a developer can use from start to finish to design interfaces and export JSON consumable by json-to-compose.

What it needs:

- **Fix critical bugs**: The delete modifiers button doesn't work (empty onClick), collapse state is lost when updating nodes, no input validation.
- **Basic tree operations**: Delete nodes, reorder nodes (move up/down within the same parent), move nodes between parents.
- **Drag & drop**: Reorder nodes by dragging in the component tree.
- **Complete property panel**: Edit all properties of each component type, not just text and type.
- **Import JSON**: Complete the cycle by loading existing JSON files.
- **Clean up dead code**: Remove or complete stub panels (ChatPanel, ProjectGeneratorPanel, PreviewPanel).

The approach is a form-based editor with preview, not a free canvas like Figma.

## Scope

This project focuses on the `composy/` module. It may require minor changes in `library/` if new extensions or utilities are needed.

## Success Criteria

- A developer can create a component tree from scratch, fully edit it, and export functional JSON
- A developer can import an existing JSON and edit it
- There are no non-functional buttons or broken states
- The editor has no dead code or visible stub features
- The full flow (create > edit > preview > export > import > edit > export) works without errors