Feature: List Course By Title API Testing
  As a student, I want to login, and list the course by title

  Background:
    Given the student has valid credentials

  Scenario Outline: Verify the course API returns valid data
    # Step 1: Login and get token
    When the student logs in
    Then the student should receive a valid authorization token

    # Step 2: List the course with title
    When the student searches for courses by title "<Course Title>"
    Then the response should include courses with the title "<Course Title>"

    Examples:
      | Course Title                 |
      | Data Structures & Algorithms |