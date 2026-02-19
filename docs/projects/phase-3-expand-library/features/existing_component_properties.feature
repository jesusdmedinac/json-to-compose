Feature: Enhanced Properties for Existing Components
  As a developer using json-to-compose
  I want existing components to expose all their Material 3 properties
  So that I have full control over existing components without needing new component types

  # Image Enhancement
  Scenario: Render Image with alignment
    Given a JSON with an Image with ImageProps(url = "photo.jpg", alignment = "TopCenter")
    When the node is processed by the renderer
    Then the Image is aligned to the top center within its bounds

  Scenario: Render Image with colorFilter tint
    Given a JSON with an Image with ImageProps(resourceName = "icon", colorFilterTint = "#FF4CAF50")
    When the node is processed by the renderer
    Then the Image is rendered with a green tint overlay

  # Button Enhancement
  Scenario: Render Button with custom shape
    Given a JSON with a Button with ButtonProps(shape = RoundedCorner(24))
    When the node is processed by the renderer
    Then a Button with fully rounded (pill) shape is shown

  Scenario: Render Button with containerColor and contentColor
    Given a JSON with a Button with ButtonProps(containerColor = "#FF6200EE", contentColor = "#FFFFFFFF")
    When the node is processed by the renderer
    Then a purple Button with white text/icon content is shown

  # Scaffold Enhancement
  Scenario: Render Scaffold with floatingActionButton
    Given a JSON with a Scaffold with ScaffoldProps(floatingActionButton = FloatingActionButton(Icon("add")))
    When the node is processed by the renderer
    Then a Scaffold with a FAB at the bottom-right is shown

  Scenario: Render Scaffold with containerColor
    Given a JSON with a Scaffold with ScaffoldProps(containerColor = "#FFF5F5F5")
    When the node is processed by the renderer
    Then a Scaffold with a light gray background is shown

  Scenario: Render Scaffold with floatingActionButtonPosition
    Given a JSON with a Scaffold with ScaffoldProps(floatingActionButtonPosition = "Center")
    When the node is processed by the renderer
    Then a Scaffold with a FAB centered at the bottom is shown

  # Card Enhancement
  Scenario: Render Card with custom containerColor
    Given a JSON with a Card with CardProps(containerColor = "#FFFFF3E0")
    When the node is processed by the renderer
    Then a Card with orange-tinted background is shown

  Scenario: Render Card with border
    Given a JSON with a Card with CardProps(borderWidth = 1, borderColor = "#FFE0E0E0")
    When the node is processed by the renderer
    Then a Card with a light gray 1dp border is shown

  # AlertDialog Enhancement
  Scenario: Render AlertDialog with icon
    Given a JSON with an AlertDialog with AlertDialogProps(icon = Icon("warning"))
    When the node is processed by the renderer
    Then an AlertDialog with a warning icon above the title is shown

  Scenario: Render AlertDialog with tonalElevation
    Given a JSON with an AlertDialog with AlertDialogProps(tonalElevation = 6)
    When the node is processed by the renderer
    Then an AlertDialog with M3 tonal elevation color is shown

  # TopAppBar Enhancement
  Scenario: Render TopAppBar with colors
    Given a JSON with a TopAppBar with TopAppBarProps(containerColor = "#FF6200EE", titleContentColor = "#FFFFFFFF")
    When the node is processed by the renderer
    Then a purple TopAppBar with white title text is shown

  Scenario: Render CenterAlignedTopAppBar
    Given a JSON with a node of type "CenterAlignedTopAppBar" with title Text("App Title")
    When the node is processed by the renderer
    Then a TopAppBar with the title centered horizontally is shown

  Scenario: Render MediumTopAppBar with larger title
    Given a JSON with a node of type "MediumTopAppBar" with title Text("Section")
    When the node is processed by the renderer
    Then a TopAppBar with a medium-sized title that collapses on scroll is shown

  Scenario: Render LargeTopAppBar with prominent title
    Given a JSON with a node of type "LargeTopAppBar" with title Text("Welcome")
    When the node is processed by the renderer
    Then a TopAppBar with a large prominent title that collapses on scroll is shown

  # Box Enhancement
  Scenario: Render Box with propagateMinConstraints controlled by state
    Given a JSON with a Box with BoxProps(propagateMinConstraintsStateHostName = "propagate")
    And a StateHost "propagate" with value true
    When the node is processed by the renderer
    Then the Box propagates its minimum constraints to its children

  Scenario: Serialize and deserialize all enhanced properties
    Given JSON strings for Image, Button, Scaffold, Card, AlertDialog, TopAppBar, CenterAlignedTopAppBar, MediumTopAppBar, LargeTopAppBar, and Box with enhanced properties
    When each is deserialized to ComposeNode and serialized back
    Then each resulting JSON maintains all enhanced properties
