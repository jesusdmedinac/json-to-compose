Feature: Pager Components rendered from JSON
  As a developer using json-to-compose
  I want to use HorizontalPager and VerticalPager from JSON
  So that I can create swipeable page layouts from the backend

  Scenario: Render a HorizontalPager with pages
    Given a JSON with a HorizontalPager containing 3 page children (Column nodes)
    When the node is processed by the renderer
    Then a horizontally swipeable pager with 3 pages is shown

  Scenario: HorizontalPager current page controlled by state
    Given a JSON with a HorizontalPager with PagerProps(currentPageStateHostName = "currentPage")
    And a StateHost "currentPage" with value 1
    When the node is processed by the renderer
    Then the HorizontalPager displays the second page

  Scenario: HorizontalPager page change updates state
    Given a ComposeDocument with a HorizontalPager with onPageChangedEventName = "pageChanged"
    And an action "pageChanged" that sets state "currentPage"
    When the user swipes to the next page
    Then the state "currentPage" is updated

  Scenario: Render a VerticalPager with pages
    Given a JSON with a VerticalPager containing 4 page children
    When the node is processed by the renderer
    Then a vertically swipeable pager with 4 pages is shown

  Scenario: VerticalPager current page controlled by state
    Given a JSON with a VerticalPager with PagerProps(currentPageStateHostName = "verticalPage")
    And a StateHost "verticalPage" with value 2
    When the node is processed by the renderer
    Then the VerticalPager displays the third page

  Scenario: HorizontalPager with beyondViewportPageCount
    Given a JSON with a HorizontalPager with PagerProps(beyondViewportPageCount = 1)
    When the node is processed by the renderer
    Then the pager pre-composes 1 page on each side of the current page

  Scenario: HorizontalPager with userScrollEnabled = false
    Given a JSON with a HorizontalPager with PagerProps(userScrollEnabled = false)
    When the node is processed by the renderer
    Then the pager displays content but cannot be swiped by the user

  Scenario: Serialize and deserialize pager components
    Given JSON strings for HorizontalPager and VerticalPager with all their properties
    When each is deserialized to ComposeNode and serialized back
    Then each resulting JSON maintains all pager component properties
