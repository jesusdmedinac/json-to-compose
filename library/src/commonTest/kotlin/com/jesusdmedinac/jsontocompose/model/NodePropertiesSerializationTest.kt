package com.jesusdmedinac.jsontocompose.model

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class NodePropertiesSerializationTest {

    private val json = Json { encodeDefaults = true }

    // --- Scenario 1: TextProps serialization with all fields ---

    @Test
    fun textPropsSerialization() {
        val original = NodeProperties.TextProps(text = "Hello")
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.TextProps>(decoded)
        assertEquals("Hello", decoded.text)
        assertTrue(encoded.contains("\"type\":\"TextProps\""))
    }

    // --- Scenario 2: TextProps default values ---

    @Test
    fun textPropsDefaultValues() {
        val jsonString = """{"type":"TextProps"}"""
        val decoded = json.decodeFromString<NodeProperties>(jsonString)

        assertIs<NodeProperties.TextProps>(decoded)
        assertNull(decoded.text)
    }

    // --- Scenario 3: ButtonProps serialization with child ---

    @Test
    fun buttonPropsSerializationWithChild() {
        val original = NodeProperties.ButtonProps(
            onClickEventName = "click_me",
            child = ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(text = "Click")
            )
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.ButtonProps>(decoded)
        assertEquals("click_me", decoded.onClickEventName)
        assertNotNull(decoded.child)
        assertEquals(ComposeType.Text, decoded.child.type)
        val childProps = decoded.child.properties as? NodeProperties.TextProps
        assertEquals("Click", childProps?.text)
    }

    // --- Scenario 4: ColumnProps serialization with children and layout options ---

    @Test
    fun columnPropsSerializationWithChildrenAndLayout() {
        val original = NodeProperties.ColumnProps(
            children = listOf(
                ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "A")),
                ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "B")),
            ),
            verticalArrangement = "SpaceBetween",
            horizontalAlignment = "CenterHorizontally",
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.ColumnProps>(decoded)
        assertEquals(2, decoded.children?.size)
        assertEquals("SpaceBetween", decoded.verticalArrangement)
        assertEquals("CenterHorizontally", decoded.horizontalAlignment)
        val firstChild = decoded.children?.get(0)?.properties as? NodeProperties.TextProps
        assertEquals("A", firstChild?.text)
    }

    // --- Scenario 5: RowProps serialization with children and layout options ---

    @Test
    fun rowPropsSerializationWithChildrenAndLayout() {
        val original = NodeProperties.RowProps(
            children = listOf(
                ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "X")),
            ),
            verticalAlignment = "CenterVertically",
            horizontalArrangement = "SpaceAround",
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.RowProps>(decoded)
        assertEquals(1, decoded.children?.size)
        assertEquals("CenterVertically", decoded.verticalAlignment)
        assertEquals("SpaceAround", decoded.horizontalArrangement)
    }

    // --- Scenario 6: BoxProps serialization with all fields ---

    @Test
    fun boxPropsSerializationWithAllFields() {
        val original = NodeProperties.BoxProps(
            children = listOf(
                ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Inside")),
            ),
            contentAlignment = "Center",
            propagateMinConstraints = true,
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.BoxProps>(decoded)
        assertEquals(1, decoded.children?.size)
        assertEquals("Center", decoded.contentAlignment)
        assertEquals(true, decoded.propagateMinConstraints)
    }

    @Test
    fun boxPropsPropagateMinConstraintsDefaultsToNull() {
        val jsonString = """{"type":"BoxProps"}"""
        val decoded = json.decodeFromString<NodeProperties>(jsonString)

        assertIs<NodeProperties.BoxProps>(decoded)
        assertEquals(null, decoded.propagateMinConstraints)
    }

    // --- Scenario 7: ImageProps serialization with URL ---

    @Test
    fun imagePropsSerializationWithUrl() {
        val original = NodeProperties.ImageProps(
            url = "https://example.com/img.png",
            contentDescription = "A photo",
            contentScale = "Crop",
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.ImageProps>(decoded)
        assertEquals("https://example.com/img.png", decoded.url)
        assertEquals("A photo", decoded.contentDescription)
        assertEquals("Crop", decoded.contentScale)
    }

    @Test
    fun imagePropsContentScaleDefaultsToNull() {
        val jsonString = """{"type":"ImageProps"}"""
        val decoded = json.decodeFromString<NodeProperties>(jsonString)

        assertIs<NodeProperties.ImageProps>(decoded)
        assertEquals(null, decoded.contentScale)
    }

    // --- Scenario 8: TextFieldProps serialization with valueStateHostName ---

    @Test
    fun textFieldPropsSerializationWithValueStateHostName() {
        val original = NodeProperties.TextFieldProps(valueStateHostName = "my_field")
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.TextFieldProps>(decoded)
        assertEquals("my_field", decoded.valueStateHostName)
        assertTrue(encoded.contains("\"valueStateHostName\""))
    }

    // --- Scenario 9: ScaffoldProps serialization with child ---

    @Test
    fun scaffoldPropsSerializationWithChild() {
        val original = NodeProperties.ScaffoldProps(
            child = ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(text = "Body")
            )
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.ScaffoldProps>(decoded)
        assertNotNull(decoded.child)
        assertEquals(ComposeType.Text, decoded.child.type)
    }

    // --- Scenario 10: CardProps serialization with all fields ---

    @Test
    fun cardPropsSerializationWithAllFields() {
        val original = NodeProperties.CardProps(
            child = ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(text = "Card content")
            ),
            elevation = 8,
            cornerRadius = 16,
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.CardProps>(decoded)
        assertNotNull(decoded.child)
        assertEquals(8, decoded.elevation)
        assertEquals(16, decoded.cornerRadius)
    }

    // --- Scenario 10.5: OutlinedCardProps serialization ---

    @Test
    fun outlinedCardPropsSerializationWithAllFields() {
        val original = NodeProperties.OutlinedCardProps(
            child = ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(text = "Outlined content")
            ),
            borderColor = "#FF000000",
            cornerRadius = 24,
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.OutlinedCardProps>(decoded)
        assertNotNull(decoded.child)
        assertEquals("#FF000000", decoded.borderColor)
        assertEquals(24, decoded.cornerRadius)
    }

    // --- Scenario 11: DialogProps serialization with all fields ---

    @Test
    fun dialogPropsSerializationWithAllFields() {
        val original = NodeProperties.AlertDialogProps(
            title = ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(text = "Delete")
            ),
            text = ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(text = "Are you sure?")
            ),
            confirmButton = ComposeNode(
                type = ComposeType.Button,
                properties = NodeProperties.ButtonProps(
                    onClickEventName = "evt_confirm",
                    child = ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Yes")
                    )
                )
            ),
            dismissButton = ComposeNode(
                type = ComposeType.Button,
                properties = NodeProperties.ButtonProps(
                    onClickEventName = "evt_dismiss",
                    child = ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "No")
                    )
                )
            ),
            visibilityStateHostName = "dlg_visible",
            onDismissRequestEventName = "evt_dismiss_request",
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.AlertDialogProps>(decoded)
        assertNotNull(decoded.title)
        assertNotNull(decoded.text)
        assertNotNull(decoded.confirmButton)
        assertNotNull(decoded.dismissButton)
        assertEquals("dlg_visible", decoded.visibilityStateHostName)
        assertEquals("evt_dismiss_request", decoded.onDismissRequestEventName)
    }

    // --- Scenario 12: CustomProps serialization with customData ---

    @Test
    fun customPropsSerializationWithCustomData() {
        val original = NodeProperties.CustomProps(
            customType = "MyWidget",
            customData = buildJsonObject {
                put("key", JsonPrimitive("value"))
                put("count", JsonPrimitive(42))
            }
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.CustomProps>(decoded)
        assertEquals("MyWidget", decoded.customType)
        assertNotNull(decoded.customData)
        assertEquals("value", (decoded.customData["key"] as JsonPrimitive).content)
        assertEquals("42", (decoded.customData["count"] as JsonPrimitive).content)
    }

    // --- Scenario 13: NodeProperties polymorphic deserialization ---

    @Test
    fun nodePropertiesPolymorphicDeserialization() {
        val jsonString = """{"type":"TextProps","text":"Poly"}"""
        val decoded = json.decodeFromString<NodeProperties>(jsonString)

        assertIs<NodeProperties.TextProps>(decoded)
        assertEquals("Poly", decoded.text)
    }

    // --- Scenario: NavigationBarProps serialization ---
    @Test
    fun navigationBarPropsSerialization() {
        val original = NodeProperties.NavigationBarProps(
            children = listOf(
                ComposeNode(type = ComposeType.NavigationBarItem, properties = NodeProperties.NavigationBarItemProps())
            ),
            containerColor = "#FF0000",
            contentColor = "#FFFFFF"
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.NavigationBarProps>(decoded)
        assertEquals(1, decoded.children?.size)
        assertEquals("#FF0000", decoded.containerColor)
        assertEquals("#FFFFFF", decoded.contentColor)
    }

    // --- Scenario: NavigationBarItemProps serialization ---
    @Test
    fun navigationBarItemPropsSerialization() {
        val original = NodeProperties.NavigationBarItemProps(
            selected = true,
            selectedStateHostName = "tab",
            onClickEventName = "click",
            label = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Label")),
            icon = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Icon")),
            enabled = false,
            enabledStateHostName = "enabled_state",
            alwaysShowLabel = false,
            alwaysShowLabelStateHostName = "show_label"
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.NavigationBarItemProps>(decoded)
        assertEquals(true, decoded.selected)
        assertEquals("tab", decoded.selectedStateHostName)
        assertEquals("click", decoded.onClickEventName)
        assertEquals("Label", (decoded.label?.properties as? NodeProperties.TextProps)?.text)
        assertEquals(false, decoded.enabled)
        assertEquals(false, decoded.alwaysShowLabel)
    }

    // --- Scenario: NavigationRailProps serialization ---
    @Test
    fun navigationRailPropsSerialization() {
        val original = NodeProperties.NavigationRailProps(
            children = listOf(ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "RailItem"))),
            containerColor = "#00FF00",
            contentColor = "#000000",
            header = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Header"))
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.NavigationRailProps>(decoded)
        assertEquals(1, decoded.children?.size)
        assertEquals("#00FF00", decoded.containerColor)
        assertEquals("Header", (decoded.header?.properties as? NodeProperties.TextProps)?.text)
    }

    // --- Scenario: NavigationRailItemProps serialization ---
    @Test
    fun navigationRailItemPropsSerialization() {
        val original = NodeProperties.NavigationRailItemProps(selected = true, label = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "RailItem")))
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.NavigationRailItemProps>(decoded)
        assertEquals(true, decoded.selected)
        assertEquals("RailItem", (decoded.label?.properties as? NodeProperties.TextProps)?.text)
    }

    // --- Scenario: NavigationDrawerProps serialization ---
    @Test
    fun navigationDrawerPropsSerialization() {
        val original = NodeProperties.NavigationDrawerProps(
            isOpen = true,
            isOpenStateHostName = "drawer_open",
            drawerContent = listOf(ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "DrawerItem"))),
            child = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Main"))
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.NavigationDrawerProps>(decoded)
        assertEquals(true, decoded.isOpen)
        assertEquals("drawer_open", decoded.isOpenStateHostName)
        assertEquals(1, decoded.drawerContent?.size)
        assertEquals("Main", (decoded.child?.properties as? NodeProperties.TextProps)?.text)
    }

    // --- Scenario: NavigationDrawerItemProps serialization ---
    @Test
    fun navigationDrawerItemPropsSerialization() {
        val original = NodeProperties.NavigationDrawerItemProps(
            selected = true,
            label = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Item")),
            badge = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Badge"))
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.NavigationDrawerItemProps>(decoded)
        assertEquals(true, decoded.selected)
        assertEquals("Item", (decoded.label?.properties as? NodeProperties.TextProps)?.text)
        assertEquals("Badge", (decoded.badge?.properties as? NodeProperties.TextProps)?.text)
    }

    // --- Scenario: TabRowProps serialization ---
    @Test
    fun tabRowPropsSerialization() {
        val original = NodeProperties.TabRowProps(
            selectedTabIndex = 2,
            selectedTabIndexStateHostName = "tab_index",
            children = listOf(ComposeNode(type = ComposeType.Tab, properties = NodeProperties.TabProps())),
            containerColor = "#0000FF",
            contentColor = "#FFFFFF"
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.TabRowProps>(decoded)
        assertEquals(2, decoded.selectedTabIndex)
        assertEquals("tab_index", decoded.selectedTabIndexStateHostName)
        assertEquals(1, decoded.children?.size)
        assertEquals("#0000FF", decoded.containerColor)
    }

    // --- Scenario: TabProps serialization ---
    @Test
    fun tabPropsSerialization() {
        val original = NodeProperties.TabProps(
            selected = true,
            text = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Tab"))
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.TabProps>(decoded)
        assertEquals(true, decoded.selected)
        assertEquals("Tab", (decoded.text?.properties as? NodeProperties.TextProps)?.text)
    }

    // --- Scenario: BadgeProps serialization ---
    @Test
    fun badgePropsSerialization() {
        val original = NodeProperties.BadgeProps(
            text = "3",
            textStateHostName = "badge_text",
            containerColor = "#FF0000",
            contentColor = "#FFFFFF"
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.BadgeProps>(decoded)
        assertEquals("3", decoded.text)
        assertEquals("badge_text", decoded.textStateHostName)
        assertEquals("#FF0000", decoded.containerColor)
        assertEquals("#FFFFFF", decoded.contentColor)
    }

    // --- Scenario: BadgedBoxProps serialization ---
    @Test
    fun badgedBoxPropsSerialization() {
        val original = NodeProperties.BadgedBoxProps(
            badge = ComposeNode(type = ComposeType.Badge, properties = NodeProperties.BadgeProps(text = "New")),
            child = ComposeNode(type = ComposeType.Icon, properties = NodeProperties.IconProps(iconName = "mail"))
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.BadgedBoxProps>(decoded)
        assertEquals("New", (decoded.badge?.properties as? NodeProperties.BadgeProps)?.text)
        assertEquals("mail", (decoded.child?.properties as? NodeProperties.IconProps)?.iconName)
    }

    // --- Scenario: ChipProps serialization ---
    @Test
    fun chipPropsSerialization() {
        val original = NodeProperties.ChipProps(
            label = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Assist")),
            leadingIcon = ComposeNode(type = ComposeType.Icon, properties = NodeProperties.IconProps(iconName = "star")),
            onClickEventName = "assist_click",
            enabled = false,
            enabledStateHostName = "assist_enabled"
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.ChipProps>(decoded)
        assertEquals("Assist", (decoded.label?.properties as? NodeProperties.TextProps)?.text)
        assertEquals("star", (decoded.leadingIcon?.properties as? NodeProperties.IconProps)?.iconName)
        assertEquals("assist_click", decoded.onClickEventName)
        assertEquals(false, decoded.enabled)
        assertEquals("assist_enabled", decoded.enabledStateHostName)
    }

    // --- Scenario: FilterChipProps serialization ---
    @Test
    fun filterChipPropsSerialization() {
        val original = NodeProperties.FilterChipProps(
            selected = true,
            selectedStateHostName = "selected_state",
            label = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Filter")),
            leadingIcon = ComposeNode(type = ComposeType.Icon, properties = NodeProperties.IconProps(iconName = "check")),
            onClickEventName = "filter_click",
            enabled = true,
            enabledStateHostName = "filter_enabled"
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.FilterChipProps>(decoded)
        assertEquals(true, decoded.selected)
        assertEquals("selected_state", decoded.selectedStateHostName)
        assertEquals("Filter", (decoded.label?.properties as? NodeProperties.TextProps)?.text)
        assertEquals("check", (decoded.leadingIcon?.properties as? NodeProperties.IconProps)?.iconName)
        assertEquals("filter_click", decoded.onClickEventName)
        assertEquals(true, decoded.enabled)
        assertEquals("filter_enabled", decoded.enabledStateHostName)
    }

    // --- Scenario: InputChipProps serialization ---
    @Test
    fun inputChipPropsSerialization() {
        val original = NodeProperties.InputChipProps(
            selected = false,
            selectedStateHostName = "input_selected",
            label = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Input")),
            leadingIcon = ComposeNode(type = ComposeType.Icon, properties = NodeProperties.IconProps(iconName = "avatar")),
            trailingIcon = ComposeNode(type = ComposeType.Icon, properties = NodeProperties.IconProps(iconName = "close")),
            onClickEventName = "input_click",
            enabled = false,
            enabledStateHostName = "input_enabled"
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.InputChipProps>(decoded)
        assertEquals(false, decoded.selected)
        assertEquals("input_selected", decoded.selectedStateHostName)
        assertEquals("Input", (decoded.label?.properties as? NodeProperties.TextProps)?.text)
        assertEquals("avatar", (decoded.leadingIcon?.properties as? NodeProperties.IconProps)?.iconName)
        assertEquals("close", (decoded.trailingIcon?.properties as? NodeProperties.IconProps)?.iconName)
        assertEquals("input_click", decoded.onClickEventName)
        assertEquals(false, decoded.enabled)
        assertEquals("input_enabled", decoded.enabledStateHostName)
    }

    // --- Scenario: ProgressIndicatorProps serialization ---
    @Test
    fun progressIndicatorPropsSerialization() {
        val original = NodeProperties.ProgressIndicatorProps(
            progress = 0.6f,
            progressStateHostName = "progress_state",
            color = "#0000FF",
            trackColor = "#CCCCCC"
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.ProgressIndicatorProps>(decoded)
        assertEquals(0.6f, decoded.progress)
        assertEquals("progress_state", decoded.progressStateHostName)
        assertEquals("#0000FF", decoded.color)
        assertEquals("#CCCCCC", decoded.trackColor)
    }

    // --- Scenario: PlainTooltipProps serialization ---
    @Test
    fun plainTooltipPropsSerialization() {
        val original = NodeProperties.PlainTooltipProps(
            text = "Tooltip hint",
            anchor = ComposeNode(type = ComposeType.Icon, properties = NodeProperties.IconProps(iconName = "help"))
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.PlainTooltipProps>(decoded)
        assertEquals("Tooltip hint", decoded.text)
        assertEquals("help", (decoded.anchor?.properties as? NodeProperties.IconProps)?.iconName)
    }

    // --- Scenario: RichTooltipProps serialization ---
    @Test
    fun richTooltipPropsSerialization() {
        val original = NodeProperties.RichTooltipProps(
            title = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Title")),
            text = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Description")),
            action = ComposeNode(type = ComposeType.Button, properties = NodeProperties.ButtonProps(onClickEventName = "action")),
            anchor = ComposeNode(type = ComposeType.Icon, properties = NodeProperties.IconProps(iconName = "settings"))
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.RichTooltipProps>(decoded)
        assertEquals("Title", (decoded.title?.properties as? NodeProperties.TextProps)?.text)
        assertEquals("Description", (decoded.text?.properties as? NodeProperties.TextProps)?.text)
        assertEquals("action", (decoded.action?.properties as? NodeProperties.ButtonProps)?.onClickEventName)
        assertEquals("settings", (decoded.anchor?.properties as? NodeProperties.IconProps)?.iconName)
    }

    // --- Scenario: ListItemProps serialization ---
    @Test
    fun listItemPropsSerialization() {
        val original = NodeProperties.ListItemProps(
            headlineContent = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Headline")),
            supportingContent = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Supporting")),
            overlineContent = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Overline")),
            leadingContent = ComposeNode(type = ComposeType.Icon, properties = NodeProperties.IconProps(iconName = "star")),
            trailingContent = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Trailing")),
            onClickEventName = "list_item_click"
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.ListItemProps>(decoded)
        assertEquals("Headline", (decoded.headlineContent?.properties as? NodeProperties.TextProps)?.text)
        assertEquals("Supporting", (decoded.supportingContent?.properties as? NodeProperties.TextProps)?.text)
        assertEquals("Overline", (decoded.overlineContent?.properties as? NodeProperties.TextProps)?.text)
        assertEquals("star", (decoded.leadingContent?.properties as? NodeProperties.IconProps)?.iconName)
        assertEquals("Trailing", (decoded.trailingContent?.properties as? NodeProperties.TextProps)?.text)
        assertEquals("list_item_click", decoded.onClickEventName)
    }
}
