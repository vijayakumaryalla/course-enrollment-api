Feature: Course Availability API Testing
  As an external user, I want to check the course availability with course code

  Scenario Outline: Check the course availability
    When I check availability of valid course by sending the "<Course Code>"
    And course code should match with "<Course Code>" and course title should match with "<Course Title>"

    Examples:
      | Course Code    | Course Title|
      | DSA_1775553866 | Data Structures & Algorithms |