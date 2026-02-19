Feature: Unit tests for Alignment mappers
  As a library maintainer
  I want unit tests for alignment string-to-Compose mappers
  To ensure all alignment values are correctly mapped and invalid values throw exceptions

  Scenario: toAlignment maps all 9 two-dimensional alignments
    Given the strings "TopStart", "TopCenter", "TopEnd", "CenterStart", "Center", "CenterEnd", "BottomStart", "BottomCenter", "BottomEnd"
    When each is converted via toAlignment()
    Then each maps to the corresponding Alignment constant

  Scenario: toAlignment throws AlignmentException for invalid value
    Given the string "InvalidAlignment"
    When toAlignment() is called
    Then an AlignmentException is thrown with a message containing "InvalidAlignment"

  Scenario: toVerticalAlignment maps all 3 vertical alignments
    Given the strings "Top", "CenterVertically", "Bottom"
    When each is converted via toVerticalAlignment()
    Then each maps to the corresponding Alignment.Vertical constant

  Scenario: toVerticalAlignment throws AlignmentException for invalid value
    Given the string "Left"
    When toVerticalAlignment() is called
    Then an AlignmentException is thrown

  Scenario: toHorizontalsAlignment maps all 3 horizontal alignments
    Given the strings "Start", "CenterHorizontally", "End"
    When each is converted via toHorizontalsAlignment()
    Then each maps to the corresponding Alignment.Horizontal constant

  Scenario: toHorizontalsAlignment throws AlignmentException for invalid value
    Given the string "Top"
    When toHorizontalsAlignment() is called
    Then an AlignmentException is thrown
