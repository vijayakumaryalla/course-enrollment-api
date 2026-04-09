Feature: List All Courses API Testing
  As an external user, I want to list all courses in the system

  Scenario: Verify all courses API returns valid data
    When I send a GET request to "/courses/all"
    Then the response status should be 200
    And the response should contain a list of courses
    And each course should have valid fields