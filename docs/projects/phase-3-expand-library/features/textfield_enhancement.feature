Feature: TextField Component Property Enhancement
  As a developer using json-to-compose
  I want TextField to support placeholder, label, icons, error state, and keyboard options
  So that I can build complete forms from the backend

  Scenario: Render TextField with placeholder text
    Given a JSON with a TextField with TextFieldProps(placeholder = "Enter your name")
    When the node is processed by the renderer
    Then a TextField with "Enter your name" placeholder is displayed when empty

  Scenario: Render TextField with label
    Given a JSON with a TextField with TextFieldProps(label = "Email")
    When the node is processed by the renderer
    Then a TextField with "Email" label is displayed

  Scenario: Render TextField with leadingIcon
    Given a JSON with a TextField with TextFieldProps(leadingIcon = Icon("search"))
    When the node is processed by the renderer
    Then a TextField with a search icon on the left is displayed

  Scenario: Render TextField with trailingIcon
    Given a JSON with a TextField with TextFieldProps(trailingIcon = Icon("clear"))
    When the node is processed by the renderer
    Then a TextField with a clear icon on the right is displayed

  Scenario: Render TextField with isError = true
    Given a JSON with a TextField with TextFieldProps(isError = true)
    When the node is processed by the renderer
    Then a TextField with error styling (red border/label) is displayed

  Scenario: Render TextField with state-driven isError
    Given a JSON with a TextField with TextFieldProps(isErrorStateHostName = "emailInvalid")
    And a StateHost "emailInvalid" with value true
    When the node is processed by the renderer
    Then a TextField with error styling is displayed

  Scenario: Render TextField with supportingText
    Given a JSON with a TextField with TextFieldProps(supportingText = "Must be a valid email")
    When the node is processed by the renderer
    Then a TextField with "Must be a valid email" helper text below is displayed

  Scenario: Render TextField with singleLine = true
    Given a JSON with a TextField with TextFieldProps(singleLine = true)
    When the node is processed by the renderer
    Then a TextField that does not expand to multiple lines is displayed

  Scenario: Render TextField with maxLines
    Given a JSON with a TextField with TextFieldProps(maxLines = 5)
    When the node is processed by the renderer
    Then a TextField that expands up to 5 lines is displayed

  Scenario: Render TextField with keyboardType Number
    Given a JSON with a TextField with TextFieldProps(keyboardType = "Number")
    When the node is processed by the renderer
    Then a TextField that shows numeric keyboard on mobile is displayed

  Scenario: Render TextField with keyboardType Email
    Given a JSON with a TextField with TextFieldProps(keyboardType = "Email")
    When the node is processed by the renderer
    Then a TextField that shows email keyboard with @ symbol is displayed

  Scenario: Render TextField with keyboardType Password
    Given a JSON with a TextField with TextFieldProps(keyboardType = "Password", visualTransformation = "Password")
    When the node is processed by the renderer
    Then a TextField that masks input with dots is displayed

  Scenario: Render TextField with readOnly = true
    Given a JSON with a TextField with TextFieldProps(readOnly = true, value = "Fixed value")
    When the node is processed by the renderer
    Then a TextField that displays "Fixed value" but cannot be edited is displayed

  Scenario: Render OutlinedTextField variant
    Given a JSON with a node of type "OutlinedTextField" with TextFieldProps(label = "Username")
    When the node is processed by the renderer
    Then a Material 3 OutlinedTextField with outlined border and "Username" label is displayed

  Scenario: Render TextField with prefix and suffix
    Given a JSON with a TextField with TextFieldProps(prefix = "$", suffix = ".00")
    When the node is processed by the renderer
    Then a TextField with "$" prefix and ".00" suffix is displayed

  Scenario: Serialize and deserialize TextField with all properties
    Given a JSON string with a TextField containing label, placeholder, leadingIcon, isError, supportingText, keyboardType, and maxLines
    When deserialized to ComposeNode and serialized back
    Then the resulting JSON maintains all TextField properties
