Feature: Full Property Panel per Component
  As a composy editor user
  I want to edit all properties of each component type
  To fully configure my components without editing JSON manually

  Scenario: Edit Text properties
    Given a selected Text node
    When the property panel is shown
    Then I see editable fields for: text
    And the text field shows the current value of the node

  Scenario: Edit Column properties
    Given a selected Column node
    When the property panel is shown
    Then I see dropdowns for: verticalArrangement, horizontalAlignment
    And the dropdowns show the current values

  Scenario: Edit Column verticalArrangement
    Given a selected Column node with verticalArrangement = "Top"
    When the user changes verticalArrangement to "SpaceBetween"
    Then the node updates with verticalArrangement = "SpaceBetween"
    And the preview reflects the arrangement change

  Scenario: Edit Row properties
    Given a selected Row node
    When the property panel is shown
    Then I see dropdowns for: horizontalArrangement, verticalAlignment
    And the dropdowns show the current values

  Scenario: Edit Box properties
    Given a selected Box node
    When the property panel is shown
    Then I see dropdown for: contentAlignment
    And I see checkbox for: propagateMinConstraints

  Scenario: Edit Button properties
    Given a selected Button node
    When the property panel is shown
    Then I see editable field for: onClickEventName
    And the field shows the current value

  Scenario: Edit Image properties
    Given a selected Image node
    When the property panel is shown
    Then I see editable fields for: url, resourceName, contentDescription
    And I see dropdown for: contentScale

  Scenario: Edit TextField properties
    Given a selected TextField node
    When the property panel is shown
    Then I see editable field for: valueStateHostName

  Scenario: Edit Scaffold properties
    Given a selected Scaffold node
    When the property panel is shown
    Then I see information about the current child of the Scaffold

  Scenario: Change component type maintains compatible children
    Given a selected Column node with 3 Text children
    When the user changes the type to Row
    Then the node converts to Row maintaining the 3 Text children
    And properties adapt from ColumnProps to RowProps

  Scenario: Change component type with incompatible children shows warning
    Given a selected Column node with 3 Text children
    When the user attempts to change the type to Text (which does not support children)
    Then a warning is shown indicating that children will be lost
    And the user must confirm before proceeding