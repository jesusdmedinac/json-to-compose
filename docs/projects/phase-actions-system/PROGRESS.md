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
- [ ] Dispatcher executes SetState and updates the corresponding StateHost
- [ ] Dispatcher executes ToggleState and flips a boolean StateHost
- [ ] Dispatcher executes Log and outputs the message
- [ ] Dispatcher executes Sequence and runs all child actions in order
- [ ] Dispatcher executes Custom action via registered handler
- [ ] Dispatcher warns on SetState for non-existent state key
- [ ] Dispatcher warns on ToggleState for non-boolean state key

## Feature 4: Auto-Wiring ComposeDocument to Compose
- [ ] ComposeDocument.ToCompose creates MutableStateHost for each initialState entry
- [ ] ComposeDocument.ToCompose creates Behavior for each actions entry
- [ ] Auto-created Behaviors dispatch their action lists through the dispatcher
- [ ] Auto-created StateHosts are accessible by renderers via LocalStateHost
- [ ] Auto-created Behaviors are accessible by renderers via LocalBehavior
- [ ] TextField updates work end-to-end with auto-wired String StateHost

## Feature 5: Custom Action Handlers
- [ ] LocalCustomActionHandlers CompositionLocal accepts a handler map
- [ ] Custom action type is dispatched to its registered handler
- [ ] Custom action with unregistered type logs a warning
- [ ] Custom action handler receives action params map

## Feature 6: JSON Serialization
- [ ] ComposeDocument serializes to a JSON string
- [ ] ComposeDocument deserializes from a JSON string
- [ ] Round-trip serialization preserves all data
- [ ] Malformed JSON returns a validation error
- [ ] ComposeDocument JSON integrates with existing ComposeNode JSON format

## Feature 7: Backward Compatibility
- [ ] Existing Behavior interface works unchanged with LocalBehavior
- [ ] Existing StateHost interface works unchanged with LocalStateHost
- [ ] ComposeNode.ToCompose works without a ComposeDocument wrapper
- [ ] Manual and auto-wired state and behaviors can coexist
