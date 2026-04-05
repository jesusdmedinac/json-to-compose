package com.jesusdmedinac.jsontocompose.model

/**
 * Enumerates all supported Compose component types that can be rendered from JSON.
 *
 * Each entry maps to a specific Compose widget and expects a corresponding
 * [NodeProperties] subclass for its configuration.
 */
enum class ComposeType {
    /** Vertical layout container. Uses [NodeProperties.ColumnProps]. */
    Column,
    /** Horizontal layout container. Uses [NodeProperties.RowProps]. */
    Row,
    /** Overlay/stacking layout container. Uses [NodeProperties.BoxProps]. */
    Box,
    /** Spacer with fixed height and width. Uses [NodeProperties.SpacerProps]. */
    Spacer,
    /** Displays a text string. Uses [NodeProperties.TextProps]. */
    Text,
    /** Displays an icon. Uses [NodeProperties.IconProps]. */
    Icon,
    /** Clickable button with a single child. Uses [NodeProperties.ButtonProps]. */
    Button,
    /** Outlined button with a single child. Uses [NodeProperties.ButtonProps]. */
    OutlinedButton,
    /** Text button with no background and a single child. Uses [NodeProperties.ButtonProps]. */
    TextButton,
    /** Elevated button with shadow and a single child. Uses [NodeProperties.ButtonProps]. */
    ElevatedButton,
    /** Filled tonal button with tonal color and a single child. Uses [NodeProperties.ButtonProps]. */
    FilledTonalButton,
    /** Icon button with an icon child. Uses [NodeProperties.ButtonProps]. */
    IconButton,
    /** Floating action button. Uses [NodeProperties.FabProps]. */
    FloatingActionButton,
    /** Extended floating action button. Uses [NodeProperties.ExtendedFabProps]. */
    ExtendedFloatingActionButton,
    /** Displays an image from URL or local resource. Uses [NodeProperties.ImageProps]. */
    Image,
    /** Editable text input field. Uses [NodeProperties.TextFieldProps]. */
    TextField,
    /** Outlined editable text input field. Uses [NodeProperties.TextFieldProps]. */
    OutlinedTextField,
    /** Vertically scrolling lazy list. Uses [NodeProperties.ColumnProps]. */
    LazyColumn,
    /** Horizontally scrolling lazy list. Uses [NodeProperties.RowProps]. */
    LazyRow,
    /** Material Scaffold with topBar, bottomBar, and content slots. Uses [NodeProperties.ScaffoldProps]. */
    Scaffold,
    /** Material Card container with elevation and shape. Uses [NodeProperties.CardProps]. */
    Card,
    /** Material ElevatedCard container. Uses [NodeProperties.CardProps]. */
    ElevatedCard,
    /** Material OutlinedCard container. Uses [NodeProperties.OutlinedCardProps]. */
    OutlinedCard,
    /** Material AlertDialog with confirm/dismiss buttons. Uses [NodeProperties.AlertDialogProps]. */
    AlertDialog,
    /** Material TopAppBar with title, navigation icon, and actions. Uses [NodeProperties.TopAppBarProps]. */
    TopAppBar,
    /** Material 3 NavigationBar container. Uses [NodeProperties.NavigationBarProps]. */
    NavigationBar,
    /** Single item inside a NavigationBar. Uses [NodeProperties.NavigationBarItemProps]. */
    NavigationBarItem,
    /** Material 3 NavigationRail container. Uses [NodeProperties.NavigationRailProps]. */
    NavigationRail,
    /** Single item inside a NavigationRail. Uses [NodeProperties.NavigationRailItemProps]. */
    NavigationRailItem,
    /** Material 3 ModalNavigationDrawer. Uses [NodeProperties.NavigationDrawerProps]. */
    ModalNavigationDrawer,
    /** Single item inside a NavigationDrawer. Uses [NodeProperties.NavigationDrawerItemProps]. */
    NavigationDrawerItem,
    /** Material 3 TabRow container. Uses [NodeProperties.TabRowProps]. */
    TabRow,
    /** Material 3 ScrollableTabRow container. Uses [NodeProperties.TabRowProps]. */
    ScrollableTabRow,
    /** Single tab item. Uses [NodeProperties.TabProps]. */
    Tab,
    /** Bottom navigation bar container. Uses [NodeProperties.BottomBarProps]. */
    BottomBar,
    /** Single item inside a bottom navigation bar. Uses [NodeProperties.BottomNavigationItemProps]. */
    BottomNavigationItem,
    /** Material toggle switch. Uses [NodeProperties.SwitchProps]. */
    Switch,
    /** Material checkbox. Uses [NodeProperties.CheckboxProps]. */
    Checkbox,

    // --- Phase 3: Input Components ---
    /** Material 3 Slider for selecting a value from a range. Uses [NodeProperties.SliderProps]. */
    Slider,
    /** Material 3 RadioButton for single selection. Uses [NodeProperties.RadioButtonProps]. */
    RadioButton,
    /** Material 3 SingleChoiceSegmentedButtonRow container. Uses [NodeProperties.SegmentedButtonRowProps]. */
    SingleChoiceSegmentedButtonRow,
    /** Material 3 MultiChoiceSegmentedButtonRow container. Uses [NodeProperties.SegmentedButtonRowProps]. */
    MultiChoiceSegmentedButtonRow,
    /** Material 3 SegmentedButton item. Uses [NodeProperties.SegmentedButtonProps]. */
    SegmentedButton,
    /** Material 3 DatePicker. Uses [NodeProperties.DatePickerProps]. */
    DatePicker,
    /** Material 3 TimePicker. Uses [NodeProperties.TimePickerProps]. */
    TimePicker,
    /** Material 3 SearchBar. Uses [NodeProperties.SearchBarProps]. */
    SearchBar,

    // --- Phase 3: Layout Components ---
    /** Material 3 HorizontalDivider. Uses [NodeProperties.DividerProps]. */
    HorizontalDivider,
    /** Material 3 VerticalDivider. Uses [NodeProperties.DividerProps]. */
    VerticalDivider,
    /** Layout that arranges children horizontally and wraps to the next line. Uses [NodeProperties.FlowRowProps]. */
    FlowRow,
    /** Layout that arranges children vertically and wraps to the next column. Uses [NodeProperties.FlowColumnProps]. */
    FlowColumn,
    /** Material 3 Surface container. Uses [NodeProperties.SurfaceProps]. */
    Surface,

    // --- Phase 3: Pager Components ---
    /** Horizontally scrolling pager. Uses [NodeProperties.PagerProps]. */
    HorizontalPager,
    /** Vertically scrolling pager. Uses [NodeProperties.PagerProps]. */
    VerticalPager,

    // --- Phase 3: ModalBottomSheet ---
    /** Material 3 ModalBottomSheet. Uses [NodeProperties.ModalBottomSheetProps]. */
    ModalBottomSheet,

    // --- Phase 3: Display Components ---
    /** Material 3 Badge. Uses [NodeProperties.BadgeProps]. */
    Badge,
    /** Material 3 BadgedBox. Uses [NodeProperties.BadgedBoxProps]. */
    BadgedBox,
    /** Material 3 AssistChip. Uses [NodeProperties.ChipProps]. */
    AssistChip,
    /** Material 3 FilterChip. Uses [NodeProperties.FilterChipProps]. */
    FilterChip,
    /** Material 3 InputChip. Uses [NodeProperties.InputChipProps]. */
    InputChip,
    /** Material 3 SuggestionChip. Uses [NodeProperties.ChipProps]. */
    SuggestionChip,
    /** Material 3 CircularProgressIndicator. Uses [NodeProperties.ProgressIndicatorProps]. */
    CircularProgressIndicator,
    /** Material 3 LinearProgressIndicator. Uses [NodeProperties.ProgressIndicatorProps]. */
    LinearProgressIndicator,
    /** Material 3 PlainTooltip. Uses [NodeProperties.PlainTooltipProps]. */
    PlainTooltip,
    /** Material 3 RichTooltip. Uses [NodeProperties.RichTooltipProps]. */
    RichTooltip,

    // --- Phase 3: Snackbar ---
    /** Material 3 SnackbarHost. Uses [NodeProperties.SnackbarHostProps]. */
    SnackbarHost,

    // --- Phase 3: ListItem ---
    /** Material 3 ListItem. Uses [NodeProperties.ListItemProps]. */
    ListItem,

    /** Custom component rendered by a user-provided composable. Uses [NodeProperties.CustomProps]. */
    Custom;

    /**
     * Returns `true` if this type is a layout container (Column, Row, or Box).
     */
    fun isLayout(): Boolean = when (this) {
        Column, Row, Box, NavigationBar, NavigationRail, TabRow, ScrollableTabRow,
        FlowRow, FlowColumn,
        SingleChoiceSegmentedButtonRow, MultiChoiceSegmentedButtonRow -> true
        else -> false
    }

    /**
     * Returns `true` if this type holds a single child (Button, Card, etc.).
     */
    fun hasChild(): Boolean = when (this) {
        Button,
        OutlinedButton,
        TextButton,
        ElevatedButton,
        FilledTonalButton,
        IconButton,
        FloatingActionButton,
        Card,
        ElevatedCard,
        OutlinedCard,
        ModalNavigationDrawer,
        Surface,
        ModalBottomSheet,
        BadgedBox -> true

        else -> false
    }
}
