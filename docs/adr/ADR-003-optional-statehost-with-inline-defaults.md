# ADR-003: Optional StateHost with Inline Defaults

**Status:** Proposed
**Date:** 2026-02-01
**Decision makers:** Project maintainers

## Context

Currently, 4 out of 5 components that use `StateHost` references **require** them to render. If a developer does not provide a `StateHost` name in the JSON (or forgets to register the corresponding `StateHost` in `LocalStateHost`), the component silently refuses to render due to early returns.

### Components affected

| Component | Required StateHost properties | Default if missing |
|---|---|---|
| **TextField** | `valueStateHostName` (`StateHost<String>`) | `""` (empty string) |
| **Switch** | `checkedStateHostName` (`StateHost<Boolean>`), `enabledStateHostName` (`StateHost<Boolean>`) | `false`, `true` |
| **Checkbox** | `checkedStateHostName` (`StateHost<Boolean>`), `enabledStateHostName` (`StateHost<Boolean>`) | `false`, `true` |
| **BottomNavigationItem** | `selectedStateHostName` (`StateHost<Boolean>`), `enabledStateHostName` (`StateHost<Boolean>`), `alwaysShowLabelStateHostName` (`StateHost<Boolean>`) | `false`, `true`, `true` |
| **AlertDialog** | `visibilityStateHostName` (`StateHost<Boolean>`) | Already optional (defaults to `true`) |

**Total: 9 StateHost properties across 5 components. 8 are strictly required, 1 is already optional.**

### The problem

Consider a developer who just wants to display a static bottom navigation bar from JSON:

```json
{
  "type": "BottomNavigationItem",
  "properties": {
    "type": "BottomNavigationItemProps",
    "selectedStateHostName": "home_selected",
    "enabledStateHostName": "home_enabled",
    "alwaysShowLabelStateHostName": "home_show_label",
    "label": { "type": "Text", "properties": { "type": "TextProps", "text": "Home" } },
    "icon": { "type": "Text", "properties": { "type": "TextProps", "text": "H" } }
  }
}
```

The host app must register **3 StateHost instances** just to render this item, even if none of those values will ever change:

```kotlin
LocalStateHost provides mapOf(
    "home_selected" to booleanStateHost(true),
    "home_enabled" to booleanStateHost(true),
    "home_show_label" to booleanStateHost(true),
)
```

This is boilerplate-heavy for what should be a simple, static use case. The `AlertDialog` component already handles this well — its `visibilityStateHostName` is optional, defaulting to `true` (visible) when not provided. The other components should follow the same pattern.

### Two distinct use cases

1. **Static rendering:** The developer wants to display a component with fixed values controlled by the server JSON. No host-side state is needed.
2. **Dynamic rendering:** The developer wants the host app to control the component's state at runtime (e.g., toggling selection, enabling/disabling).

The current API only supports use case 2, forcing the overhead of use case 2 onto use case 1.

## Options Evaluated

### Option 1: Do Nothing

Keep all StateHost properties as required. Developers must always register StateHost instances.

**Pros:**
- No code changes.
- Consistent behavior — every stateful property is always backed by a StateHost.

**Cons:**
- High boilerplate for static use cases.
- Components silently don't render when a StateHost name is missing, which is a poor developer experience.
- Discourages adoption — new developers hit invisible failures.

### Option 2: Dual Properties (StateHost name + inline value) (Selected)

For each property that currently requires a StateHost, add an **inline value** counterpart. The renderer checks the StateHost first; if not provided, it falls back to the inline value; if neither is provided, it uses a sensible default.

**JSON with StateHost (dynamic):**
```json
{
  "type": "BottomNavigationItemProps",
  "selectedStateHostName": "home_selected",
  "enabledStateHostName": "home_enabled",
  "alwaysShowLabelStateHostName": "home_show_label"
}
```

**JSON with inline values (static):**
```json
{
  "type": "BottomNavigationItemProps",
  "selected": true,
  "enabled": true,
  "alwaysShowLabel": true
}
```

**Mixed (some dynamic, some static):**
```json
{
  "type": "BottomNavigationItemProps",
  "selectedStateHostName": "home_selected",
  "enabled": true,
  "alwaysShowLabel": true
}
```

**Resolution order in renderer:**
```
StateHostName provided and registered? → use StateHost value
StateHostName provided but NOT registered? → log warning, use inline value
Inline value provided? → use inline value
Neither provided? → use default (false/true/""/etc.)
```

**Pros:**
- Static use cases require zero StateHost registration.
- Dynamic use cases work exactly as before.
- Mixed use cases are supported (e.g., `selected` is dynamic, `enabled` is static).
- Components always render — worst case they use defaults.
- Backward compatible — existing JSON with StateHost names still works.
- Follows the principle of progressive complexity: simple things are simple, complex things are possible.

**Cons:**
- Each stateful property now has two fields in `NodeProperties` (e.g., `selected: Boolean?` + `selectedStateHostName: String?`).
- Renderer logic becomes slightly more complex (resolution chain).
- If both `selected` and `selectedStateHostName` are provided, the StateHost wins — this precedence must be documented.

### Option 3: Make StateHost Names Optional with Defaults Only

Remove the early returns. If a StateHost name is `null`, use a hardcoded default directly (e.g., `selected = false`). No inline values in JSON.

**Pros:**
- Minimal code change — just remove early returns and add defaults.
- Components always render.

**Cons:**
- Static values cannot be controlled from JSON — they're always the hardcoded default.
- A server wanting to render a "selected" nav item with `selected = true` cannot do so without a StateHost.
- Eliminates the static use case benefit — the server would still need StateHosts for anything non-default.

### Option 4: Auto-create StateHost from Inline Values

If a StateHost name is not provided but an inline value is, the renderer internally creates a local `StateHost` backed by `mutableStateOf(inlineValue)`. This makes the inline value reactive within the composition.

**Pros:**
- Same JSON flexibility as Option 2.
- Even inline values become reactive (e.g., an animation could change them).

**Cons:**
- Creates hidden state that the host app cannot observe or control.
- Violates the principle that state ownership is explicit.
- More complex implementation.
- The auto-created state is lost on recomposition scope changes.

## Decision

**We will implement Option 2: Dual Properties (StateHost name + inline value).**

### Rationale

1. **Progressive complexity.** Simple use cases (static UI from JSON) should not require StateHost registration. Option 2 is the only option that makes static rendering trivially easy while keeping dynamic rendering fully supported.

2. **The server controls everything.** In an SDUI framework, the server should be able to express a fully static screen without the host app needing any setup beyond `jsonNode.ToCompose()`. Option 2 enables this.

3. **AlertDialog already proves the pattern.** Its `visibilityStateHostName` is optional, defaulting to `true` (visible). This ADR simply extends that pattern to all stateful properties.

4. **Backward compatible.** Existing JSON using `selectedStateHostName` continues to work. The inline `selected` field is additive.

5. **Clear precedence.** The rule "StateHost wins over inline value" is intuitive — if you've set up dynamic state management, it should take priority.

### Implementation Plan

#### Phase 1: Update `NodeProperties`

Add inline value fields alongside each StateHost name field:

```kotlin
data class BottomNavigationItemProps(
    val selected: Boolean? = null,
    val selectedStateHostName: String? = null,
    val enabled: Boolean? = null,
    val enabledStateHostName: String? = null,
    val alwaysShowLabel: Boolean? = null,
    val alwaysShowLabelStateHostName: String? = null,
    val onClickEventName: String? = null,
    val label: ComposeNode? = null,
    val icon: ComposeNode? = null,
) : NodeProperties

data class SwitchProps(
    val checked: Boolean? = null,
    val checkedStateHostName: String? = null,
    val enabled: Boolean? = null,
    val enabledStateHostName: String? = null,
    val onCheckedChangeEventName: String? = null,
) : NodeProperties

data class CheckboxProps(
    val checked: Boolean? = null,
    val checkedStateHostName: String? = null,
    val enabled: Boolean? = null,
    val enabledStateHostName: String? = null,
    val onCheckedChangeEventName: String? = null,
) : NodeProperties

data class TextFieldProps(
    val value: String? = null,
    val valueStateHostName: String? = null,
) : NodeProperties
```

#### Phase 2: Create a resolution helper

Extract the StateHost-or-inline resolution into a reusable utility to avoid duplicating the logic in every renderer:

```kotlin
@Composable
inline fun <reified T> resolveStateHostValue(
    stateHostName: String?,
    inlineValue: T?,
    defaultValue: T,
): Pair<T, StateHost<T>?> {
    val currentStateHost = LocalStateHost.current
    if (stateHostName != null) {
        val raw = currentStateHost[stateHostName]
        if (raw != null) {
            @Suppress("UNCHECKED_CAST")
            val typed = raw as? StateHost<T>
            if (typed != null) {
                val value = runCatching { typed.state }.getOrElse { defaultValue }
                return value to typed
            }
            println("Warning: StateHost \"$stateHostName\" type mismatch. Expected StateHost<${T::class.simpleName}>.")
        } else {
            println("Warning: No StateHost registered with name \"$stateHostName\".")
        }
    }
    return (inlineValue ?: defaultValue) to null
}
```

#### Phase 3: Simplify renderers

Replace the verbose null-check chains with the helper:

```kotlin
@Composable
fun ComposeNode.ToBottomNavigationItem() {
    val props = properties as? NodeProperties.BottomNavigationItemProps ?: return
    val rowScope = LocalRowScope.current ?: return
    val behaviors = LocalBehavior.current

    val (selected, _) = resolveStateHostValue(
        stateHostName = props.selectedStateHostName,
        inlineValue = props.selected,
        defaultValue = false,
    )
    val (enabled, _) = resolveStateHostValue(
        stateHostName = props.enabledStateHostName,
        inlineValue = props.enabled,
        defaultValue = true,
    )
    val (alwaysShowLabel, _) = resolveStateHostValue(
        stateHostName = props.alwaysShowLabelStateHostName,
        inlineValue = props.alwaysShowLabel,
        defaultValue = true,
    )

    with(rowScope) {
        BottomNavigationItem(
            selected = selected,
            onClick = { props.onClickEventName?.let { behaviors[it]?.invoke() } },
            modifier = (Modifier from composeModifier).testTag(type.name),
            enabled = enabled,
            label = props.label?.let { label -> { label.ToCompose() } },
            alwaysShowLabel = alwaysShowLabel,
            icon = { props.icon?.ToCompose() ?: Text("") },
        )
    }
}
```

#### Phase 4: Update tests

Add test cases for:
- Rendering with inline values only (no StateHost).
- Rendering with StateHost only (no inline values).
- Rendering with both (StateHost takes precedence).
- Rendering with neither (defaults apply).

#### Phase 5: Update AGENTS.md type mapping rules

Update the type mapping section to document the dual-property pattern and resolution order.

## Consequences

### Positive

- Components always render regardless of StateHost registration.
- Static SDUI screens work without any host-side setup.
- Reduced boilerplate for simple use cases.
- The `resolveStateHostValue` helper eliminates ~50 lines of duplicated null-checking per renderer.
- AlertDialog's existing pattern becomes the standard.

### Negative

- Each stateful property now has two fields, increasing the surface area of `NodeProperties`.
- Developers must understand the precedence rule (StateHost > inline > default).
- JSON payload for static use cases has inline values that could have been avoided with a "default" convention. (Mitigated: the fields are nullable and optional.)

### Risks

- If a developer provides both `selected: false` and `selectedStateHostName: "home_selected"` (where the StateHost value is `true`), the component renders as `true`. This could be confusing if the developer expects the inline value. Mitigation: document the precedence clearly and log a debug message when both are provided.

## References

- ADR-002: StateHost type information — establishes the naming convention for `*StateHostName` properties.
- `AlertDialogRenderer.kt` — existing implementation of optional StateHost with default.
- `StateHost<T>` interface: `library/src/commonMain/kotlin/.../state/StateHost.kt`
- Current renderers: `SwitchRenderer.kt`, `CheckboxRenderer.kt`, `TextFieldRenderer.kt`, `BottomNavigationItemRenderer.kt`
