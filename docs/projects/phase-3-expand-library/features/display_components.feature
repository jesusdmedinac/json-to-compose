Feature: Display Components rendered from JSON
  As a developer using json-to-compose
  I want to use Icon, Badge, Chips, ProgressIndicators, and Tooltip from JSON
  So that I can build rich informational UIs from the backend

  # Icon Component
  Scenario: Render an Icon by name
    Given a JSON with a node of type "Icon" with IconProps(name = "favorite")
    When the node is processed by the renderer
    Then a Material icon matching "favorite" is shown

  Scenario: Render an Icon with custom tint color
    Given a JSON with an Icon with IconProps(name = "star", tint = "#FFFFD700")
    When the node is processed by the renderer
    Then a gold-colored star icon is shown

  Scenario: Render an Icon with custom size via modifier
    Given a JSON with an Icon with IconProps(name = "home") and a Size modifier of 48
    When the node is processed by the renderer
    Then a 48dp home icon is shown

  Scenario: Icon name controlled by state
    Given a JSON with an Icon with IconProps(nameStateHostName = "currentIcon")
    And a StateHost "currentIcon" with value "favorite_border"
    When the state changes to "favorite"
    Then the icon changes from outline to filled favorite

  # Badge Component
  Scenario: Render a Badge with no content
    Given a JSON with a node of type "Badge"
    When the node is processed by the renderer
    Then a small dot Badge is shown

  Scenario: Render a Badge with count text
    Given a JSON with a Badge with BadgeProps(text = "5")
    When the node is processed by the renderer
    Then a Badge with "5" text is shown

  Scenario: Render a BadgedBox with icon and badge
    Given a JSON with a BadgedBox containing an Icon("notifications") and Badge("3")
    When the node is processed by the renderer
    Then a notifications icon with a "3" badge overlay is shown

  # Chip Components
  Scenario: Render an AssistChip
    Given a JSON with an AssistChip with ChipProps(label = "Help", iconName = "help")
    When the node is processed by the renderer
    Then a Material 3 AssistChip with "Help" label and help icon is shown

  Scenario: Render a FilterChip selected
    Given a JSON with a FilterChip with FilterChipProps(label = "Active", selected = true)
    When the node is processed by the renderer
    Then a Material 3 FilterChip with "Active" label in selected state is shown

  Scenario: FilterChip selection controlled by state
    Given a JSON with a FilterChip with FilterChipProps(selectedStateHostName = "filterActive")
    And a StateHost "filterActive" with value false
    When the node is processed by the renderer
    Then an unselected FilterChip is shown

  Scenario: FilterChip onClick toggles selection
    Given a ComposeDocument with a FilterChip with onClickEventName = "toggleFilter"
    And an action "toggleFilter" that toggles state "filterActive"
    When the user clicks the FilterChip
    Then the state "filterActive" is toggled

  Scenario: Render an InputChip with trailing delete icon
    Given a JSON with an InputChip with InputChipProps(label = "Tag1", onCloseEventName = "removeTag1")
    When the node is processed by the renderer
    Then an InputChip with "Tag1" label and a close/delete icon is shown

  Scenario: Render a SuggestionChip
    Given a JSON with a SuggestionChip with ChipProps(label = "Try this")
    When the node is processed by the renderer
    Then a Material 3 SuggestionChip with "Try this" label is shown

  # Progress Indicators
  Scenario: Render a CircularProgressIndicator indeterminate
    Given a JSON with a node of type "CircularProgressIndicator"
    When the node is processed by the renderer
    Then a spinning circular progress indicator is shown

  Scenario: Render a CircularProgressIndicator with progress value
    Given a JSON with a CircularProgressIndicator with ProgressProps(progress = 0.75)
    When the node is processed by the renderer
    Then a circular progress indicator at 75% is shown

  Scenario: CircularProgressIndicator progress controlled by state
    Given a JSON with a CircularProgressIndicator with ProgressProps(progressStateHostName = "uploadProgress")
    And a StateHost "uploadProgress" with value 0.5
    When the node is processed by the renderer
    Then a circular progress indicator at 50% is shown

  Scenario: Render a LinearProgressIndicator indeterminate
    Given a JSON with a node of type "LinearProgressIndicator"
    When the node is processed by the renderer
    Then an animated horizontal progress bar is shown

  Scenario: Render a LinearProgressIndicator with progress value
    Given a JSON with a LinearProgressIndicator with ProgressProps(progress = 0.3)
    When the node is processed by the renderer
    Then a horizontal progress bar at 30% is shown

  Scenario: Render a LinearProgressIndicator with custom colors
    Given a JSON with a LinearProgressIndicator with ProgressProps(color = "#FF4CAF50", trackColor = "#FFE8F5E9")
    When the node is processed by the renderer
    Then a green progress bar on a light green track is shown

  # Tooltip
  Scenario: Render a PlainTooltip on an Icon
    Given a JSON with a TooltipBox with TooltipProps(text = "Add to favorites") wrapping an IconButton
    When the user long-presses the IconButton
    Then a plain tooltip with "Add to favorites" is shown

  Scenario: Render a RichTooltip with title and action
    Given a JSON with a RichTooltipBox with RichTooltipProps(title = "Info", text = "This is a detailed explanation", actionLabel = "Learn more")
    When the user long-presses the target
    Then a rich tooltip with title, description, and action button is shown

  Scenario: Serialize and deserialize all display components
    Given JSON strings for Icon, Badge, BadgedBox, AssistChip, FilterChip, InputChip, SuggestionChip, CircularProgressIndicator, LinearProgressIndicator, PlainTooltip, and RichTooltip
    When each is deserialized to ComposeNode and serialized back
    Then each resulting JSON maintains all display component properties
