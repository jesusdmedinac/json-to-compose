package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.ComposeType.AlertDialog
import com.jesusdmedinac.jsontocompose.model.ComposeType.AssistChip
import com.jesusdmedinac.jsontocompose.model.ComposeType.Badge
import com.jesusdmedinac.jsontocompose.model.ComposeType.BadgedBox
import com.jesusdmedinac.jsontocompose.model.ComposeType.BottomBar
import com.jesusdmedinac.jsontocompose.model.ComposeType.BottomNavigationItem
import com.jesusdmedinac.jsontocompose.model.ComposeType.Box
import com.jesusdmedinac.jsontocompose.model.ComposeType.Button
import com.jesusdmedinac.jsontocompose.model.ComposeType.Card
import com.jesusdmedinac.jsontocompose.model.ComposeType.CenterAlignedTopAppBar
import com.jesusdmedinac.jsontocompose.model.ComposeType.Checkbox
import com.jesusdmedinac.jsontocompose.model.ComposeType.CircularProgressIndicator
import com.jesusdmedinac.jsontocompose.model.ComposeType.Column
import com.jesusdmedinac.jsontocompose.model.ComposeType.Custom
import com.jesusdmedinac.jsontocompose.model.ComposeType.DatePicker
import com.jesusdmedinac.jsontocompose.model.ComposeType.ElevatedButton
import com.jesusdmedinac.jsontocompose.model.ComposeType.ElevatedCard
import com.jesusdmedinac.jsontocompose.model.ComposeType.ExtendedFloatingActionButton
import com.jesusdmedinac.jsontocompose.model.ComposeType.FilledTonalButton
import com.jesusdmedinac.jsontocompose.model.ComposeType.FilterChip
import com.jesusdmedinac.jsontocompose.model.ComposeType.FloatingActionButton
import com.jesusdmedinac.jsontocompose.model.ComposeType.FlowColumn
import com.jesusdmedinac.jsontocompose.model.ComposeType.FlowRow
import com.jesusdmedinac.jsontocompose.model.ComposeType.HorizontalDivider
import com.jesusdmedinac.jsontocompose.model.ComposeType.HorizontalPager
import com.jesusdmedinac.jsontocompose.model.ComposeType.Icon
import com.jesusdmedinac.jsontocompose.model.ComposeType.IconButton
import com.jesusdmedinac.jsontocompose.model.ComposeType.Image
import com.jesusdmedinac.jsontocompose.model.ComposeType.InputChip
import com.jesusdmedinac.jsontocompose.model.ComposeType.LargeTopAppBar
import com.jesusdmedinac.jsontocompose.model.ComposeType.LazyColumn
import com.jesusdmedinac.jsontocompose.model.ComposeType.LazyRow
import com.jesusdmedinac.jsontocompose.model.ComposeType.LinearProgressIndicator
import com.jesusdmedinac.jsontocompose.model.ComposeType.ListItem
import com.jesusdmedinac.jsontocompose.model.ComposeType.MediumTopAppBar
import com.jesusdmedinac.jsontocompose.model.ComposeType.ModalBottomSheet
import com.jesusdmedinac.jsontocompose.model.ComposeType.ModalNavigationDrawer
import com.jesusdmedinac.jsontocompose.model.ComposeType.MultiChoiceSegmentedButtonRow
import com.jesusdmedinac.jsontocompose.model.ComposeType.NavigationBar
import com.jesusdmedinac.jsontocompose.model.ComposeType.NavigationBarItem
import com.jesusdmedinac.jsontocompose.model.ComposeType.NavigationDrawerItem
import com.jesusdmedinac.jsontocompose.model.ComposeType.NavigationRail
import com.jesusdmedinac.jsontocompose.model.ComposeType.NavigationRailItem
import com.jesusdmedinac.jsontocompose.model.ComposeType.OutlinedButton
import com.jesusdmedinac.jsontocompose.model.ComposeType.OutlinedCard
import com.jesusdmedinac.jsontocompose.model.ComposeType.OutlinedTextField
import com.jesusdmedinac.jsontocompose.model.ComposeType.PlainTooltip
import com.jesusdmedinac.jsontocompose.model.ComposeType.RadioButton
import com.jesusdmedinac.jsontocompose.model.ComposeType.RichTooltip
import com.jesusdmedinac.jsontocompose.model.ComposeType.Row
import com.jesusdmedinac.jsontocompose.model.ComposeType.Scaffold
import com.jesusdmedinac.jsontocompose.model.ComposeType.ScrollableTabRow
import com.jesusdmedinac.jsontocompose.model.ComposeType.SearchBar
import com.jesusdmedinac.jsontocompose.model.ComposeType.SegmentedButton
import com.jesusdmedinac.jsontocompose.model.ComposeType.SingleChoiceSegmentedButtonRow
import com.jesusdmedinac.jsontocompose.model.ComposeType.Slider
import com.jesusdmedinac.jsontocompose.model.ComposeType.SnackbarHost
import com.jesusdmedinac.jsontocompose.model.ComposeType.Spacer
import com.jesusdmedinac.jsontocompose.model.ComposeType.SuggestionChip
import com.jesusdmedinac.jsontocompose.model.ComposeType.Surface
import com.jesusdmedinac.jsontocompose.model.ComposeType.Switch
import com.jesusdmedinac.jsontocompose.model.ComposeType.Tab
import com.jesusdmedinac.jsontocompose.model.ComposeType.TabRow
import com.jesusdmedinac.jsontocompose.model.ComposeType.Text
import com.jesusdmedinac.jsontocompose.model.ComposeType.TextButton
import com.jesusdmedinac.jsontocompose.model.ComposeType.TextField
import com.jesusdmedinac.jsontocompose.model.ComposeType.TimePicker
import com.jesusdmedinac.jsontocompose.model.ComposeType.TopAppBar
import com.jesusdmedinac.jsontocompose.model.ComposeType.VerticalDivider
import com.jesusdmedinac.jsontocompose.model.ComposeType.VerticalPager
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties

/**
 * Creates a default, non-null properties instance for this component type.
 * Optionally preserves existing children when changing a node's type.
 */
fun ComposeType.createDefaultProperties(
    preservedChildren: List<ComposeNode>? = null,
    preservedChild: ComposeNode? = null
): NodeProperties = when (this) {
    Column, LazyColumn -> NodeProperties.ColumnProps(children = preservedChildren)
    Row, LazyRow -> NodeProperties.RowProps(children = preservedChildren)
    Box -> NodeProperties.BoxProps(children = preservedChildren)
    Spacer -> NodeProperties.SpacerProps
    Text -> NodeProperties.TextProps(
        text = "Text"
    )
    Icon -> NodeProperties.IconProps(
        iconName = "Add"
    )
    Button, OutlinedButton, TextButton, ElevatedButton, FilledTonalButton, IconButton -> NodeProperties.ButtonProps(child = preservedChild ?: preservedChildren?.firstOrNull())
    FloatingActionButton -> NodeProperties.FabProps(icon = preservedChild ?: preservedChildren?.firstOrNull())
    ExtendedFloatingActionButton -> NodeProperties.ExtendedFabProps()
    Image -> NodeProperties.ImageProps()
    TextField, OutlinedTextField -> NodeProperties.TextFieldProps()
    Scaffold -> NodeProperties.ScaffoldProps(child = preservedChild ?: preservedChildren?.firstOrNull())
    Card, ElevatedCard -> NodeProperties.CardProps(child = preservedChild ?: preservedChildren?.firstOrNull())
    OutlinedCard -> NodeProperties.OutlinedCardProps(child = preservedChild ?: preservedChildren?.firstOrNull())
    AlertDialog -> NodeProperties.AlertDialogProps()
    TopAppBar, CenterAlignedTopAppBar, MediumTopAppBar, LargeTopAppBar -> NodeProperties.TopAppBarProps()
    NavigationBar -> NodeProperties.NavigationBarProps()
    NavigationBarItem -> NodeProperties.NavigationBarItemProps()
    NavigationRail -> NodeProperties.NavigationRailProps()
    NavigationRailItem -> NodeProperties.NavigationRailItemProps()
    ModalNavigationDrawer -> NodeProperties.NavigationDrawerProps()
    NavigationDrawerItem -> NodeProperties.NavigationDrawerItemProps()
    TabRow, ScrollableTabRow -> NodeProperties.TabRowProps(children = preservedChildren)
    Tab -> NodeProperties.TabProps(text = preservedChild ?: preservedChildren?.firstOrNull())
    BottomBar -> NodeProperties.BottomBarProps(children = preservedChildren)
    BottomNavigationItem -> NodeProperties.BottomNavigationItemProps()
    Switch -> NodeProperties.SwitchProps(checked = false)
    Checkbox -> NodeProperties.CheckboxProps(checked = false)
    Slider -> NodeProperties.SliderProps(value = 0f)
    RadioButton -> NodeProperties.RadioButtonProps(selected = false)
    SingleChoiceSegmentedButtonRow, MultiChoiceSegmentedButtonRow -> NodeProperties.SegmentedButtonRowProps()
    SegmentedButton -> NodeProperties.SegmentedButtonProps()
    DatePicker -> NodeProperties.DatePickerProps()
    TimePicker -> NodeProperties.TimePickerProps()
    SearchBar -> NodeProperties.SearchBarProps()
    HorizontalDivider, VerticalDivider -> NodeProperties.DividerProps()
    FlowRow -> NodeProperties.FlowRowProps(children = preservedChildren)
    FlowColumn -> NodeProperties.FlowColumnProps(children = preservedChildren)
    Surface -> NodeProperties.SurfaceProps(child = preservedChild ?: preservedChildren?.firstOrNull())
    HorizontalPager, VerticalPager -> NodeProperties.PagerProps()
    ModalBottomSheet -> NodeProperties.ModalBottomSheetProps()
    Badge -> NodeProperties.BadgeProps()
    BadgedBox -> NodeProperties.BadgedBoxProps()
    AssistChip, SuggestionChip -> NodeProperties.ChipProps()
    FilterChip -> NodeProperties.FilterChipProps()
    InputChip -> NodeProperties.InputChipProps()
    CircularProgressIndicator, LinearProgressIndicator -> NodeProperties.ProgressIndicatorProps()
    PlainTooltip -> NodeProperties.PlainTooltipProps()
    RichTooltip -> NodeProperties.RichTooltipProps()
    SnackbarHost -> NodeProperties.SnackbarHostProps()
    ListItem -> NodeProperties.ListItemProps()
    Custom -> NodeProperties.CustomProps(customType = "")
}