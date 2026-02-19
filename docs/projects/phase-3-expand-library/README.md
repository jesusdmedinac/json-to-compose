# Phase 3: Expand Library — Full Component & Modifier Coverage

## Idea (Step 1)

The json-to-compose library currently supports 18 components and 13 modifiers. While this covers the essentials (Column, Row, Box, Text, Button, Image, TextField, etc.), a real production app needs many more Material 3 components and modifiers to build complete interfaces from JSON without relying on Custom renderers for common use cases.

The goal of this phase is to expand the library to cover the full breadth of Material 3 components, add missing modifiers that any real layout requires, enhance existing components with properties they're missing, and add powerful new action types for richer server-driven interactions.

What it needs:

- **Enhance existing components**: Text is missing fontSize/fontWeight/color/textAlign. TextField is missing placeholder/label/icons/error state. Button has no enabled/disabled state. Image has no alignment. Column/Row lack Arrangement.spacedBy.
- **New component variants**: OutlinedButton, TextButton, IconButton, FloatingActionButton, ElevatedCard, OutlinedCard, OutlinedTextField.
- **Navigation components**: NavigationBar, NavigationRail, NavigationDrawer, TabRow, Tab — essential for any multi-screen app.
- **Input components**: Slider, RadioButton, SegmentedButton, DatePicker, TimePicker, SearchBar — for forms and data entry.
- **Layout components**: Spacer, Divider, FlowRow, FlowColumn, Surface — fundamental building blocks.
- **Pager components**: HorizontalPager, VerticalPager — for swipeable content.
- **Bottom Sheet**: ModalBottomSheet — a standard interaction pattern.
- **Display components**: Icon, Badge, Chips (Assist, Filter, Input, Suggestion), ProgressIndicators, Tooltip.
- **Snackbar**: SnackbarHost integration with Scaffold and ShowSnackbar action.
- **ListItem**: The standard Material 3 list item with headline, supporting, leading, trailing content.
- **Missing modifiers**: Clickable, Weight, Scroll, Offset, Size, AspectRatio, ZIndex, AnimateContentSize, WrapContent, MinSize, MaxSize.
- **Advanced actions**: Navigate, Conditional, Delay, ShowSnackbar, IncrementState, LaunchUrl.

## Scope

This project focuses on the `library/` module. Each new component requires:
1. Add to `ComposeType` enum
2. Create `NodeProperties.*Props` data class
3. Implement renderer composable
4. Add JSON serialization/deserialization
5. Unit tests

Each new modifier requires:
1. Add to `ComposeModifier.Operation` sealed class
2. Implement in `ModifierMapper`
3. Add JSON serialization/deserialization
4. Unit tests

## Success Criteria

- All new components render correctly from JSON on all 4 platforms (Android, iOS, Desktop, WASM)
- All new modifiers apply correctly and are serializable
- All existing components gain their missing properties without breaking backward compatibility
- Every new action type is serializable and executable via ActionDispatcher
- JSON round-trip fidelity: serialize -> deserialize -> serialize produces identical output
- No regressions in existing 240 phase-1 scenarios
- Unit test coverage for every new component, modifier, property, and action
