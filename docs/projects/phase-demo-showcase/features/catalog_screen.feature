Feature: Catalog screen as the home landing page
  As a developer using json-to-compose
  I want a visually appealing home screen with categories and highlights
  To provide an overview of the library's capabilities at a glance

  Background:
    The Catalog screen is the first screen users see when opening the app.
    It provides a hero section, category cards, featured component demos,
    and modifier highlights. All content is built using ComposeNode trees.

  Scenario: Hero section with Box + BackgroundColor + centered text
    Given the Catalog screen is displayed
    When the hero section renders
    Then a Box with a gradient-like background color is displayed
    And centered text shows the library name and tagline
    And the hero uses FillMaxWidth and a fixed Height modifier

  Scenario: Component category cards in LazyRow
    Given the Catalog screen is displayed
    When the category cards section renders
    Then a LazyRow displays category cards for Layout, Content, Input, and Containers
    And each card shows the category name and component count
    And the cards use Card components with BackgroundColor and Padding modifiers

  Scenario: Featured components list with mini live demos
    Given the Catalog screen is displayed
    When the featured components section renders
    Then a list shows 3-4 featured components with mini live demos
    And each featured item renders a working ComposeNode demo inline
    And the demos showcase interactive components like Button and Switch

  Scenario: Modifier highlights row with Shadow, Clip, Border, Rotate
    Given the Catalog screen is displayed
    When the modifier highlights section renders
    Then a LazyRow shows 4 modifier highlight cards
    And each card demonstrates one modifier visually (Shadow, Clip, Border, Rotate)
    And the cards use the actual modifiers they are demonstrating

  Scenario: Built entirely with ComposeNode trees
    Given the Catalog screen content is defined
    When the screen renders
    Then all UI elements are built using ComposeNode trees
    And every node is rendered via ComposeNode.toString().ToCompose()
    And no native Compose components are used directly for content
