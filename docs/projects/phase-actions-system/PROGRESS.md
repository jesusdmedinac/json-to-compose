# Phase: Actions System — Progress

## Feature 1: ComposeAction Model
- [x] SetState action holds a stateKey and a value
- [x] ToggleState action holds a stateKey
- [x] Log action holds a message string
- [x] Sequence action holds a list of child actions
- [x] Custom action holds a type and a params map
- [x] ComposeAction is serializable to and from JSON

## Feature 2: ComposeDocument Model
- [x] ComposeDocument contains an initialState map
- [x] ComposeDocument contains an actions map of name to action list
- [x] ComposeDocument contains a root ComposeNode
- [x] ComposeDocument is serializable to and from JSON
- [x] ComposeDocument with empty state and empty actions is valid

## Feature 3: Action Dispatcher
- [x] Dispatcher executes SetState and updates the corresponding StateHost
- [x] Dispatcher executes ToggleState and flips a boolean StateHost
- [x] Dispatcher executes Log and outputs the message
- [x] Dispatcher executes Sequence and runs all child actions in order
- [x] Dispatcher executes Custom action via registered handler
- [x] Dispatcher warns on SetState for non-existent state key
- [x] Dispatcher warns on ToggleState for non-boolean state key

## Feature 4: Auto-Wiring ComposeDocument to Compose
- [x] ComposeDocument.ToCompose creates MutableStateHost for each initialState entry
- [x] ComposeDocument.ToCompose creates Behavior for each actions entry
- [x] Auto-created Behaviors dispatch their action lists through the dispatcher
- [x] Auto-created StateHosts are accessible by renderers via LocalStateHost
- [x] Auto-created Behaviors are accessible by renderers via LocalBehavior
- [x] TextField updates work end-to-end with auto-wired String StateHost

## Feature 5: Custom Action Handlers
- [x] LocalCustomActionHandlers CompositionLocal accepts a handler map
- [x] Custom action type is dispatched to its registered handler
- [x] Custom action with unregistered type logs a warning
- [x] Custom action handler receives action params map

## Feature 6: JSON Serialization
- [x] ComposeDocument serializes to a JSON string
- [x] ComposeDocument deserializes from a JSON string
- [x] Round-trip serialization preserves all data
- [x] Malformed JSON returns a validation error
- [x] ComposeDocument JSON integrates with existing ComposeNode JSON format

## Feature 7: Backward Compatibility
- [x] Existing Behavior interface works unchanged with LocalBehavior
- [x] Existing StateHost interface works unchanged with LocalStateHost
- [x] ComposeNode.ToCompose works without a ComposeDocument wrapper
- [x] Manual and auto-wired state and behaviors can coexist
