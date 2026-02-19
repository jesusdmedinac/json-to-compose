Feature: Unit tests for Arrangement mappers
  As a library maintainer
  I want unit tests for arrangement string-to-Compose mappers
  To ensure all arrangement values are correctly mapped and invalid values throw exceptions

  Scenario: toArrangement maps all 4 generic arrangements
    Given the strings "Center", "SpaceEvenly", "SpaceBetween", "SpaceAround"
    When each is converted via toArrangement()
    Then each maps to the corresponding Arrangement constant

  Scenario: toArrangement throws ArrangementException for invalid value
    Given the string "InvalidArrangement"
    When toArrangement() is called
    Then an ArrangementException is thrown with a message containing "InvalidArrangement"

  Scenario: toHorizontalArrangement maps all 6 standard arrangements
    Given the strings "Start", "End", "Center", "SpaceEvenly", "SpaceBetween", "SpaceAround"
    When each is converted via toHorizontalArrangement()
    Then each maps to the corresponding Arrangement.Horizontal constant

  Scenario: toHorizontalArrangement maps all 6 absolute arrangements
    Given the strings "AbsoluteLeft", "AbsoluteCenter", "AbsoluteRight", "AbsoluteSpaceBetween", "AbsoluteSpaceEvenly", "AbsoluteSpaceAround"
    When each is converted via toHorizontalArrangement()
    Then each maps to the corresponding Arrangement.Absolute constant

  Scenario: toHorizontalArrangement throws ArrangementException for invalid value
    Given the string "Bottom"
    When toHorizontalArrangement() is called
    Then an ArrangementException is thrown

  Scenario: toVerticalArrangement maps all 6 vertical arrangements
    Given the strings "Top", "Bottom", "Center", "SpaceEvenly", "SpaceBetween", "SpaceAround"
    When each is converted via toVerticalArrangement()
    Then each maps to the corresponding Arrangement.Vertical constant

  Scenario: toVerticalArrangement throws ArrangementException for invalid value
    Given the string "Start"
    When toVerticalArrangement() is called
    Then an ArrangementException is thrown
