Feature: Unit tests for ComposeType helpers
  As a library maintainer
  I want unit tests for ComposeType enum helper functions
  To ensure isLayout() and hasChild() return correct values for all types

  Scenario: isLayout returns true for layout types
    Given the ComposeType values Column, Row, Box
    When isLayout() is called on each
    Then each returns true

  Scenario: isLayout returns false for non-layout types
    Given the ComposeType values Text, Button, Image, TextField, LazyColumn, LazyRow, Scaffold, Card, Dialog, Custom
    When isLayout() is called on each
    Then each returns false

  Scenario: hasChild returns true for single-child types
    Given the ComposeType values Button, Card
    When hasChild() is called on each
    Then each returns true

  Scenario: hasChild returns false for non-single-child types
    Given the ComposeType values Column, Row, Box, Text, Image, TextField, LazyColumn, LazyRow, Scaffold, Dialog, Custom
    When hasChild() is called on each
    Then each returns false
