Feature: Delete course
  As an instructor, I want to login, filter my own courses
  and delete one course

  Background:
    Given the instructor has valid credentials

  Scenario Outline: Delete the course test scenario
    When the instructor logs in
    Then the instructor should receive a valid authorization token
    When the instructor filter courses owned by me
    And delete one course randomly
    Then the response should contain the message <expectedMessage>

    Examples:
      | expectedMessage               |
      | "Course deleted successfully" |
