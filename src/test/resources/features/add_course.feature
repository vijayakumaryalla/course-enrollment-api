Feature: Add course
  As an instructor, I want to login, and add new courses in the system

  Background:
    Given the instructor has valid credentials

  Scenario Outline: Add course test scenario
    When the instructor logs in
    Then the instructor should receive a valid authorization token

    When the instructor add new courses by entering "<Course Title>", "<Course Code>", "<Category>", "<Total Capacity>", "<Start Date>", "<End Date>"
    Then the response should include the message "<Message>"

    Examples:
      | Course Title                 | Course Code    | Category        |Total Capacity|Start Date|End Date|Http Status|Message|
      | API Automation |     API | Programming | 20     |  2026-03-01        | 2026-06-01       |  201         |    Course added successfully   |
