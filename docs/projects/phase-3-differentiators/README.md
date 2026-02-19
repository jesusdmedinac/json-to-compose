# Phase 3: Differentiators

## Idea (Step 1)

With the library solidified (Phase 1) and the editor functional (Phase 2), the project needs differentiators that separate it from other SDUI tools and make it something developers want to use and share.

This phase adds four capabilities that transform composy from "useful" to "impressive":

- **AI Integration for layout generation from prompts**: The chat panel already existing in composy connects to an LLM so the developer can describe what they want ("a login form with email, password, and submit button") and the AI generates the JSON component tree directly in the editor.
- **Functional Demo Server**: A Ktor server serving JSON on REST endpoints. Demo apps consume these endpoints live, demonstrating the real Server-Driven UI use case: changing the UI from the backend without deployment.
- **IntelliJ/Android Studio Plugin for preview**: A plugin allowing preview of a json-to-compose JSON directly in the IDE, without opening the web editor. For the developer's day-to-day workflow.
- **Public Web Playground**: A deployed version of composy accessible from the browser where anyone can try the library without installing anything. With pre-loaded examples and a "copy JSON" button to take it to their project.

## Scope

This phase touches multiple modules: `composy/` (AI chat), `server/` (demo server), a new `intellij-plugin/` module, and the deployment of composy as a web app.

## Success Criteria

- A developer can describe a layout in natural language and get the generated component tree
- The demo server serves JSON that apps consume and render in real-time
- A developer can preview json-to-compose JSON inside IntelliJ/Android Studio
- Anyone can access the web playground, create a design, and copy the JSON without installing anything